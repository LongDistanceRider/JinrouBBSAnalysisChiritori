/**
 * 村ごとに保管するための構造体
 * 村情報，配役情報は村一つにつき一つ記録する
 * 日記情報，セリフ情報，投票情報は日数分記録する
 */
package jp.ac.aitech.k13114kk.table;

import java.util.ArrayList;
import java.util.HashMap;

public class Village {
	
	private int endDate = 0;											//ゲーム終了日（エピローグを含まない，三日目まで議論がある村は３になり，エピローグは４日目となる
	private String villageName;
	private String villageNumber;
	private String updateTime;
	private String people;
	private ArrayList<HashMap<String, String>> postMap = new ArrayList<HashMap<String, String>>();	//[人物名]，役割
	
	public void setVillageData(String[] strArray) {
		this.villageName = strArray[0];
		this.villageNumber = strArray[1];
		this.updateTime = strArray[2];
		this.people = strArray[3];
	}

	public String getVillageName() { return villageName; }
	public void setVillageName(String villageName) { this.villageName = villageName; }
	
	public String getVillageNumber() { return villageNumber; }
	public void setVillageNumber(String villageNumber) { this.villageNumber = villageNumber; }
	
	public String getUpdateTime() { return updateTime; }
	public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
	
	public String getPeople() { return people; }
	public void setPeople(String people) { this.people = people; }

	public ArrayList<HashMap<String, String>> getPostMap() { return postMap; }
	public void setPostMap(ArrayList<HashMap<String, String>> postMap) { this.postMap = postMap; }

	public int getEndDate() { return endDate; }
	public void setEndDate(int endDate) { this.endDate = endDate; }
}
