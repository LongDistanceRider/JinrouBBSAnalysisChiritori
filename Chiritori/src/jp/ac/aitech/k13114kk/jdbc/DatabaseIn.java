package jp.ac.aitech.k13114kk.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.ac.aitech.k13114kk.table.Date;
import jp.ac.aitech.k13114kk.table.Village;

public class DatabaseIn {

	/**
	 * villageMapからクエリを生成する
	 * 
	 * @param village
	 *            村情報
	 * @return list クエリ群
	 */
	public static ArrayList<String> villageTableToSQL(Village village) {
		ArrayList<String> list = new ArrayList<String>();
		String sql = "insert into villageTable values (" + 
						village.getVillageNumber() + "," + "\"" +
						village.getVillageName() + "\"" + "," + 
						village.getUpdateTime() + "," + 
						village.getPeople() + 
						")";
		list.add(sql);
		return list;
	}

	/**
	 * serifMapからクエリを生成する
	 * 
	 * @param village
	 *            村情報
	 * @return list クエリ軍
	 */
	public static ArrayList<String> serifTableToSQL(Village village, Date[] date) {
		ArrayList<String> list = new ArrayList<String>();
		int counter = 0;
		// -- リスト作成 --
		for (int i = 0; i < date.length; i++) {
			// 村番号 連番 日数 セリふid 色 発言者 投稿時間 セリフ
			ArrayList<HashMap<String, String>> listMap = date[i].getSerifMap();
			for (HashMap<String, String> hashMap : listMap) {
				String sql = "insert into serifTable values (" + 
								village.getVillageNumber() + "," + // 村番号
						counter +  "," +// 連番
						i +  "," +//  "," +日数
						hashMap.get("serifId") +  "," + "\"" +// セリフid
						hashMap.get("color") + "\"" + "," + "\"" +// 色
						hashMap.get("playerId") + "\"" + "," +// 発言者
						hashMap.get("contribution") + "," + "\"" + // 投稿時間
						hashMap.get("serif") + "\"" + // セリフ
						")";
				list.add(sql);
				counter++;
			}
		}
		return list;
	}

	/**
	 * dateMapからクエリを生成する
	 * 
	 * @param 村情報
	 * @return list クエリ軍
	 */
	public static ArrayList<String> dateTableToSQL(Village village, Date[] date) {
		ArrayList<String> list = new ArrayList<String>();

		// -- リスト作成 --
		for (int i = 0; i < date.length; i++) {
			Map<String, String> map = date[i].getDateMap();
			String sql = "insert into dateTable values (" + village.getVillageNumber() + "," + // 村番号
					i + "," + "\"" + // 日数
					map.get("divination") + "\"" + "," + "\"" + // 占先
					map.get("defend") + "\"" + "," + "\"" + // 守り先
					map.get("attack") + "\"" + // 襲撃先
					")";
			list.add(sql);
		}
		return list;
	}

	/**
	 * voteMapからクエリを生成する
	 * 
	 * @param 村情報
	 * @return list クエリ軍
	 */
	public static ArrayList<String> voteTableToSQL(Village village, Date[] date) {
		ArrayList<String> list = new ArrayList<String>();

		// -- リスト作成 --
		for (int i = 0; i < date.length; i++) {
			ArrayList<HashMap<String, String>> listMap = date[i].getVoteMap();
			for (HashMap<String, String> hashMap : listMap) {
				String sql = "insert into voteTable values (" + village.getVillageNumber() + "," + // 村番号
						i + "," + "\"" + // 日数
						hashMap.get("voter") + "\"" +  "," + "\"" +	// 投票者
						hashMap.get("provideVote") + "\"" +	// 投票先
						")";
				list.add(sql);
			}
		}
		return list;
	}
	
	/**
	 * postMapからクエリを生成する
	 * 
	 * @param village 村情報
	 * @return list クエリ群
	 */
	public static ArrayList<String> postTableToSQL(Village village) {
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<HashMap<String, String>> listMap = village.getPostMap();
		
		for (HashMap<String, String> hashMap : listMap) {
			String sql = "insert into postTable values (" +
					village.getVillageNumber() + "," + "\"" +
					hashMap.get("player") + "\"" + "," + "\"" +	//プレイヤー名
					hashMap.get("post") + "\"" +			//役名
					")";
			list.add(sql);
		}
		
		return list;
	}
}
