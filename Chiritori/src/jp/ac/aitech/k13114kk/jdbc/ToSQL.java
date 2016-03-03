/**
 * SQLへ変換
 */
package jp.ac.aitech.k13114kk.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ToSQL {
	
	/**
	 * クエリをデータベースへ流します
	 * 
	 * @param sqlArrayクエリ群
	 * @return 正常終了でtrue
	 */
	public static boolean jSQL(ArrayList<String> sqlArray) {
    	// ----- データベースへ接続
    	try{
    		Class.forName("org.sqlite.JDBC");
    	} catch (ClassNotFoundException e) {
    		System.err.println(e);
    	}
    	Connection connection = null;
    	Statement statement = null;
    	int i = 0;
    	try{
    		connection = DriverManager.getConnection("jdbc:sqlite:/Users/k13114kk/eclipse/DataBase/jinrouG.db");
    		statement = connection.createStatement();
    		statement.setQueryTimeout(30);
    		
    		for (i = 0; i < sqlArray.size(); i++) {
    			statement.executeUpdate(sqlArray.get(i));		//レコード追加
			}
    	} catch(SQLException e) {
    		System.err.println("SQL Exception:[" + i + "] Message:" + e.getMessage());
    		System.err.println(sqlArray.get(i));
    	} finally {
    		try {
    			if(statement != null) {
    				statement.close();
    			}
    		} catch (SQLException e) {
    			System.err.println(e);
    		}
    		try {
    			if (connection != null) {
    				connection.close();
    			}
    		} catch (SQLException e) {
    			System.err.println(e);
    		}
    	}
		return true;
	}
	public static void resetDB() {
		// ----- データベースへ接続
    	try{
    		Class.forName("org.sqlite.JDBC");
    	} catch (ClassNotFoundException e) {
    		System.err.println(e);
    	}
    	Connection connection = null;
    	Statement statement = null;
    	try{
    		connection = DriverManager.getConnection("jdbc:sqlite:/Users/k13114kk/eclipse/DataBase/jinrouG.db");
    		statement = connection.createStatement();
    		statement.setQueryTimeout(30);
    		
    		String sql = "delete from villageTable";
    		statement.executeUpdate(sql);
    		sql = "delete from serifTable";
    		statement.executeUpdate(sql);
    		sql = "delete from voteTable";
    		statement.executeUpdate(sql);
    		sql = "delete from dateTable";
    		statement.executeUpdate(sql);
    		sql = "delete from postTable";
    		statement.executeUpdate(sql);
    		
    	} catch(SQLException e) {
    		System.err.println(e.getMessage());
    	} finally {
    		try {
    			if(statement != null) {
    				statement.close();
    			}
    		} catch (SQLException e) {
    			System.err.println(e);
    		}
    		try {
    			if (connection != null) {
    				connection.close();
    			}
    		} catch (SQLException e) {
    			System.err.println(e);
    		}
    	}
		
	}
	
}
