package de.flg_informatik.buecherverwaltung;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BVUtils implements Runnable{
	enum todo{
		query
		
		
	}
	
	static Connection connection;
	static BVControl control;
	
	todo what; 
	static void setParams(BVControl control, Connection connection){
		BVUtils.control=control;
		BVUtils.connection=connection;
	}
	public BVUtils(BVUtils.todo what){
		this.what=what;
		
	}
	
	public BVUtils(BVControl control, Connection connection){
		this.control=control;
		this.connection=connection;
	}
	
	
	public void run(){
		
	
	}
	
	public static	ResultSet doQuery(String query) throws SQLException{
			ResultSet rs;
			Statement statement = control.bvs.getStatement();
			rs = statement.executeQuery( query );
			control.bvs.releaseStatement(statement);
			return rs;
		}
	public static void doUpdate(String query) throws SQLException{
		ResultSet rs;
		Statement statement = control.bvs.getStatement();
		statement.executeUpdate( query );
		control.bvs.releaseStatement(statement);
		}
}
