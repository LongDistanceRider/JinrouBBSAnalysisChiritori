/**
 * 村情報の日数分いる情報（日記情報，セリフ情報，投票先情報）を保管するための構造体
 */
package jp.ac.aitech.k13114kk.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Date {
	private Map<String, String> dateMap;
	private ArrayList<HashMap<String, String>> serifMap;
	private ArrayList<HashMap<String, String>> voteMap;
	
	public Date() {
		this.dateMap = new HashMap<String, String>();
		this.serifMap = new ArrayList<HashMap<String, String>>();
		this.voteMap = new ArrayList<HashMap<String, String>>();
	}

	public Map<String, String> getDateMap() {
		return dateMap;
	}

	public void setDateMap(Map<String, String> dateMap) {
		this.dateMap = dateMap;
	}

	public ArrayList<HashMap<String, String>> getSerifMap() {
		return serifMap;
	}

	public void setSerifMap(ArrayList<HashMap<String, String>> serifMap) {
		this.serifMap = serifMap;
	}

	public ArrayList<HashMap<String, String>> getVoteMap() {
		return voteMap;
	}

	public void setVoteMap(ArrayList<HashMap<String, String>> voteMap) {
		this.voteMap = voteMap;
	}
}
