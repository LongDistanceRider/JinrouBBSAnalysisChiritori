/**
 * プログラム名「ちりとり」
 * 	人狼BBSのhtmlファイルから情報を取り，人狼DBへ入れ込むプログラム
 * 
 * @author k13114kk
 * @version 1.0
 */
package jp.ac.aitech.k13114kk;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import jp.ac.aitech.k13114kk.analysis.Analysis;
import jp.ac.aitech.k13114kk.jdbc.DatabaseIn;
import jp.ac.aitech.k13114kk.jdbc.ToSQL;
import jp.ac.aitech.k13114kk.table.Date;
import jp.ac.aitech.k13114kk.table.Village;

public class Chiritori {
	// ----- 定数定義 -----
	static final int VILLAGEMIN = 1;					//読み込む村の最初の番号
	static final int VILLAGEMAX = 200;					//読み込む村の最後の番号
	
	// ----- main -----
	public static void main(String[] args) {
		// ----- 処理時間計測 -----
		long start = System.nanoTime();
		
		// ----- データベースのリセット -----
		//ToSQL.resetDB();
		
		// ----- 村ループ -----
		for (int vid = VILLAGEMIN; vid <= VILLAGEMAX; vid++) {		//村をひとつずつループで処理していく
			ArrayList<String> urlList = new ArrayList<String>();	//URLリスト
			Village	village = new Village();						//データを保管するVillageを作成
			
			int endDate = Analysis.gameDays(vid);					//ゲーム終了日を取得
			village.setEndDate(endDate);							//ゲーム終了日を保管
			
			Date[] date = new Date[endDate + 2];					//データを保管するdateを作成
			
			// ---- URLリストを作成 ----
			int meslog = 0;
			urlList.add("http://www.wolfg.x0.com/index.rb?vid=" + vid + "&meslog=000_ready");	//プロローグ
			for (meslog = 0; meslog < endDate; meslog++) {									//ゲーム期間（一日目，二日目...）
				urlList.add("http://www.wolfg.x0.com/index.rb?vid=" + vid + "&meslog=00" + meslog + "_progress");
			}
			urlList.add("http://www.wolfg.x0.com/index.rb?vid=" + vid + "&meslog=00" + meslog + "_party");	//プロローグ
			
			// ---- エピローグ破損を調べる ----
			if(Analysis.isDocumentDamage(urlList)) {
				System.err.println("村番号:" + vid);		//破損していたので，解析をスキップする
				continue;
			}
			
			// ---- URLからドキュメントを取り出し，解析へかける ----
			int page = 0;						//ページ数 0はプロローグ 1は一日目 2は二日目......
			for (String url : urlList) {
				Document document = null;			//ドキュメント宣言
				try {
					document = Jsoup.connect(url).get();		//ドキュメント取得
				} catch (IOException e){
					System.err.println(e);
				}
				// -- 取得したDocumentをページ数に合わせて解析クラスに投げる
				switch(page) {
				case 0:						//プロローグ
					// --　セリフ情報 -- 
					date[0] = new Date();
					date[0].setSerifMap(Analysis.serifAnalysis(document));
					break;
				case 1:						//１日目
					// --　セリフ情報 -- 
					date[1] = new Date();
					date[1].setSerifMap(Analysis.serifAnalysis(document));
					// -- 村情報 --
					village.setVillageData(Analysis.villageAnalysis(document));
					break;
				case 2:						//２日目
					// --　セリフ情報 -- 
					date[2] = new Date();
					date[2].setSerifMap(Analysis.serifAnalysis(document));
					date[2].setDateMap(Analysis.dayAnalysis(document, page));
					break;
				default:
					//エピローグか，ゲーム中か
					if(page == urlList.size() - 1){	//エピローグ処理
						// -- セリフ情報 -- 
						date[page] = new Date();
						date[page].setSerifMap(Analysis.serifAnalysis(document));
						
						// -- 投票先情報 --
						date[page].setVoteMap(Analysis.voteAnalysis(document));
						
						// -- 役割情報 --
						village.setPostMap(Analysis.postAnalysis(document));
					} else {						//ゲーム中処理
						// -- セリフ情報 -- 
						date[page] = new Date();
						date[page].setSerifMap(Analysis.serifAnalysis(document));
						
						// -- 日情報 --
						date[page].setDateMap(Analysis.dayAnalysis(document, page));
						
						//-- 投票先情報 --
						date[page].setVoteMap(Analysis.voteAnalysis(document));
					}
				}
				page++;
			}
			// ---- 村ひとつ処理が終わったので，クエリを生成する ----
			ArrayList<String> villageTable = DatabaseIn.villageTableToSQL(village);
			ArrayList<String> serifTable = DatabaseIn.serifTableToSQL(village, date);
			ArrayList<String> dateTable = DatabaseIn.dateTableToSQL(village, date);
			ArrayList<String> voteTable = DatabaseIn.voteTableToSQL(village, date);
			ArrayList<String> postTable = DatabaseIn.postTableToSQL(village);
			
			// ---- 村ひとつ処理が終わったので，クエリからSQLiteへ
			
			ToSQL.jSQL(villageTable);
			ToSQL.jSQL(serifTable);
			ToSQL.jSQL(dateTable);
			ToSQL.jSQL(voteTable);
			ToSQL.jSQL(postTable);
			
			/* *********************************************************************
			 *  DEBUG
			 * *********************************************************************/
			
			System.out.println("villageNumber:" + village.getVillageNumber());
			/*
			System.out.println("====================");
			System.out.println("EndDay:" + village.getEndDate());
			System.out.println("villageName:" + village.getVillageName());
			System.out.println("villageNumber:" + village.getVillageNumber());
			System.out.println("updateTime:" + village.getUpdateTime());
			System.out.println("people:" + village.getPeople());
			System.out.println("postMap:" + village.getPostMap());
			System.out.println("--------------------");
			for (int i = 0; i < date.length; i++) {
				System.out.println("dateMap:" + "[" + i + "]" + date[i].getDateMap());
				System.out.println("voteMap:" + "[" + i + "]" + date[i].getVoteMap());
				System.out.println("serifMap" + "[" + i + "]" + date[i].getSerifMap());
				System.out.println("-----");
			}
			System.out.println("villageTable:" + villageTable);
			System.out.println("serifTable:" + serifTable);
			System.out.println("dateTable:" + dateTable);
			System.out.println("voteTable:" + voteTable);
			System.out.println("postTable:" + postTable);
			*/
			
			// ----- 村と日をリセット -----
			village = null;
			date = null;
		}
		
		// ----- 処理時間計測 -----
		long end = System.nanoTime();
		System.out.println("Time:" + (end - start) / 1000000f + "ms");
	}
}
