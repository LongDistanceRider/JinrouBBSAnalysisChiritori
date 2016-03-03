package jp.ac.aitech.k13114kk.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Analysis {

	// ----- 定数 -----

	// ----- serifTableの連番 -----
	private static int uniqueNumber = 0;

	/**
	 * 配役情報をdocumentから抜き出す
	 * 
	 * @param document 一ページ分のhtml情報
	 * @return map 配役情報
	 */
	public static ArrayList<HashMap<String, String>> postAnalysis(Document document) {
		ArrayList<HashMap<String, String>> listMap = new ArrayList<HashMap<String, String>>();
		Elements element3 = document.getElementsByAttributeValue("class", "announce");
		Element element4 = element3.get(3);
		String str = element4.text();
		String[] strArray = str.split("。");

		for (int i = 2; i < strArray.length; i += 2) {
			HashMap<String, String> map = new HashMap<String, String>();
			String[] strArrayArray = strArray[i].split(" ", 0); // 空白でパース
			String player = strArrayArray[1]; // 負傷兵とか
			String post = strArray[i + 1]; // 村人だった
			String value = post.substring(0, post.length() - 3); // 「だった」を消す
			
			map.put("player", player);
			map.put("post", value);
			listMap.add(map);
		}
		return listMap;
	}

	/**
	 * 投票先情報をdocumetから抜き出す
	 * 
	 * @param document
	 *            一ページ分のhtml情報
	 * @return map 投票先情報
	 */
	public static ArrayList<HashMap<String, String>> voteAnalysis(Document document) {
		ArrayList<HashMap<String, String>> listMap = new ArrayList<HashMap<String, String>>();
		Elements elementsExtra = document.getElementsByAttributeValue("class", "extra");
		try{
			
		} catch(IndexOutOfBoundsException e) {
			System.err.println("elementExtra:" + elementsExtra);
			System.err.println(document);
		}
		Element elementDivination = elementsExtra.get(0);
		String strVote = elementDivination.text();
		String[] strArrayVote = strVote.split(" ", 0);
		
		for (int i = 0; i < strArrayVote.length; i += 6) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("voter", strArrayVote[i]);
			map.put("provideVote", strArrayVote[i + 3]);
			listMap.add(map);
		}
		return listMap;
	}

	/**
	 * 日情報をdocumentから抜き出す 日情報は「占先」「守り先」「襲撃先」 二日目のみ「占先」だけを抜き出す
	 * 
	 * @param document 一ページ分のhtml情報
	 * @return map 日情報
	 */
	public static Map<String, String> dayAnalysis(Document document, int page) {
		Map<String, String> map = new HashMap<String, String>();
		 // -- class="extra"の情報を取得して，取得できた分をループ
		Elements elementsExtra = document.getElementsByAttributeValue("class", "extra");
		for (int i = 0; i < elementsExtra.size(); i++) {
			Element element1 = elementsExtra.get(i);
			String strElement1 = element1.text();
			if(strElement1.endsWith("占った。")) {
				String[] strArrayDivination = strElement1.split(" ", 0);
				String valueDivination = strArrayDivination[3];
				map.put("divination", valueDivination);
				if(page == 2) {return map;}
				
			} else if(strElement1.endsWith("守っている。")) {
				String[] strArrayDefend = strElement1.split(" ", 0);
				map.put("defend", strArrayDefend[3]);
			}
		}

		// -- 襲撃先を特定（襲撃失敗のときにはnull）
		Elements elementsMesWhisper = document.getElementsByAttributeValue("class", "mes_whisper_body1");
		String strAttack = elementsMesWhisper.text();
		// - 襲撃が失敗しているか確認する
		if(strAttack.endsWith("今日がお前の命日だ！")) {	//成功
			String[] strArrayAttack = strAttack.split(" ", 0);
			map.put("attack", strArrayAttack[0]);
		}
		return map;
	}

	/**
	 * セリフ情報をdocumentから抜き出す 楽天家のセリフも含む 白色「通常発言」灰色「つぶやき」赤色「狼発言」
	 * 
	 * @param document 一ページ分のhtml情報
	 * @return map セリフ情報
	 */
	public static ArrayList<HashMap<String, String>> serifAnalysis(Document document) {
		ArrayList<HashMap<String, String>> listMap = new ArrayList<HashMap<String, String>>();
		Elements messageBalloon = document.getElementsByAttributeValueMatching("class", "message ch");
		for (int i = 0; i < messageBalloon.size(); i++) { // バルーン
			HashMap<String, String> map = new HashMap<String, String>();
			// -- バルーン内のテキストをパースする
			String strMessageBalloon = messageBalloon.get(i).text(); // 1. 楽天家 ゲルト 13:53 人狼なんて......
			String[] strArrayMessageBalloon = strMessageBalloon.split(" ", 0); // 空白でパースする
			int ArraySwitch = 0; // 白なら1. それ以外は0（プレイヤーを特定するときにstrArrayMessageBalloonの配列番号がずれるため）

			// -- 色ごとに処理
			// -- 色を特定する（非効率）[時間がかかりすぎなので改善]
			String colorStr = messageBalloon.get(i).toString();
			if (colorStr.indexOf("say00.jpg") != -1) { // 白
				map.put("color", "white");

				String serifId = strArrayMessageBalloon[0].replaceAll("[^0-9]", ""); // serifId取得
				ArrayList<String> keysSerif = new ArrayList<String>();
				keysSerif.add(String.valueOf(uniqueNumber));
				keysSerif.add("serifId");
				map.put("serifId", serifId);

				ArraySwitch = 1;
			} else if (colorStr.indexOf("think00.jpg") != -1) {
				map.put("color", "gray");
			} else if (colorStr.indexOf("whisper00.jpg") != -1) {
				map.put("color", "red");
			} else if (colorStr.indexOf("groan00.jpg") != -1) {
				map.put("color", "blue");
			} else {
				continue; // システムメッセージなどがここにくる
			}

			// -- プレイヤー登録
			map.put("playerId", strArrayMessageBalloon[0 + ArraySwitch]);

			// -- 投稿時間を登録
			map.put("contribution", strArrayMessageBalloon[2 + ArraySwitch].replaceAll(":", "")); // 投稿時間を登録

			// -- セリフは空白でパースされているかもしれないので連結作業をする
			StringBuffer sb = new StringBuffer();
			for (int j = 3 + ArraySwitch; j < strArrayMessageBalloon.length; j++) {
				sb.append(strArrayMessageBalloon[j]);
			}
			
			// -- セリフにダブルクオーテーションがあった場合、エラーになるので例外処理を施す
			String str = sb.toString();
			str = str.replaceAll("\"", "\"\"");

			// -- セリフ登録
			map.put("serif", str);
			
			//listMapへ追加
			listMap.add(map);
		}
		return listMap;
	}

	/**
	 * 村情報をdocumentから抜き出す 抜き出す情報は「村名」「村の更新時間」「参加人数」「」
	 * 
	 * @param document 一ページ分のhtml情報
	 * @return String[] 村情報
	 */
	public static String[] villageAnalysis(Document document) {
		String[] strArray = new String[4];
		for (int i = 0; i < strArray.length; i++) {
			strArray[i] = new String();
		}

		// -- 村番号と村名
		String title = document.title(); // documentのタイトル部分を抜き出し
		String[] strArrayTitle = title.split(" ", 0); // 空白で文字列を分解

		// -- 村番号の処理("G0000"を"0000"に変換
		String villageNumber = strArrayTitle[1].substring(1);

		// -- 村番号と村名をmapへ保管
		strArray[0] = strArrayTitle[2];
		strArray[1] = villageNumber;

		// -- 更新時間 <strong>から更新時間を取得
		Elements elementUpdateTime = document.getElementsByTag("strong");
		String[] strArrayUpdateTime = elementUpdateTime.toString().split(" ", 0); // 空白で文字列分解

		// -- 更新時間の処理(23:45"を"2345"に変換
		String updateTime = strArrayUpdateTime[1].replaceAll(":", "");

		// -- 更新時間をMapへ保管
		strArray[2] = updateTime;

		// -- 参加人数 <div class="announce">かつ「さあ 自らの姿を」から始まる文を取得
		Elements elementPeople = document.getElementsByAttributeValue("class", "announce");
		String strPeople = elementPeople.text();

		int people = 0;
		if (strPeople.startsWith("さあ、自らの姿を鏡に映してみよう")) {
			int rawValue = Integer.parseInt(strPeople.replaceAll("[^0-9]", ""));
			while (rawValue != 0) {
				people += rawValue % 10;
				rawValue = rawValue / 10;
			}
			strArray[3] = "" + people;
		}
		return strArray;
	}

	/**
	 * 村のゲーム日数を取得（
	 * 
	 * @param vid 村番号
	 * @return 村最後の日付を返します．（プロローグは含まない，３日間ゲームされていれば，"3"を返す）
	 */
	public static int gameDays(int vid) {
		Document document = null;
		String url = "http://www.wolfg.x0.com/index.rb?vid=" + vid;
		try {
			document = Jsoup.connect(url).get();
		} catch (IOException e) {
			System.err.println(e);
		}

		int ret = 0;
		for (ret = 0; ret < 9; ret++) {
			String match = "meslog=00" + ret + "_party";
			Elements element = document.getElementsByAttributeValueContaining("href", match);
			if (element.size() > 0)
				break;
		}
		return ret;
	}
	
	/**
	 * エピローグにてシステム情報が取得できない（破損）しているか調べる
	 * 
	 * @param document 村
	 * @return 破損している = true
	 */
	public static boolean isDocumentDamage(ArrayList<String> list) {
		String url = list.get(list.size() - 1);
		Document document = null;
		try {
			document = Jsoup.connect(url).get();		//ドキュメント取得
		} catch (IOException e){
			System.err.println(e);
		}
				
		Elements elementsExtra = document.getElementsByAttributeValue("class", "extra");
		if(elementsExtra.size() <= 0) {
			System.err.println("エピローグ破損確認");
			return true;
		}
		return false;
	}
}
