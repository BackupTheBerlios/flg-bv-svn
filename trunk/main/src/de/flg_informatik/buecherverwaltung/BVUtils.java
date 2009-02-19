package de.flg_informatik.buecherverwaltung;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BVUtils implements Runnable{
	enum todo{
		query,
		update,
		remove
		
		
	}
	
	static Connection connection;
	static BVControl control;
	
	private todo what; 
	private String command;
	ResultSet result=null; 
	static void setParams(BVControl control, Connection connection){
		BVUtils.control=control;
		BVUtils.connection=connection;
	}
	private BVUtils(BVUtils.todo what, String command){
		this.what=what;
		this.command=command;
		new Thread(this).start();
		this.waitForSemaphore();
	}
	
	public BVUtils(BVControl control, Connection connection){
		this.control=control;
		this.connection=connection;
	}
	
	
	public void run() {
		Statement statement = control.bvs.getStatement();
		try{
			switch (this.what){
			case query:
				result = statement.executeQuery( command );
				control.bvs.releaseStatement(statement);
				break;
			case update:
				statement = control.bvs.getStatement();
				statement.executeUpdate(command);
				control.bvs.releaseStatement(statement);
				break;
			}
			
		}catch(SQLException sqle){
			
		}
		releaseSemaphore();
	
	}
	
	private Object semaphore=new Object(); // Semaphore
	private void waitForSemaphore(){
		synchronized (semaphore){
			try{
				semaphore.wait();
			}catch(InterruptedException ie){
				ie.printStackTrace();
			}
		}
	}
	private void releaseSemaphore(){
		synchronized (semaphore){
			semaphore.notify();
		}
	}
	public static	ResultSet doQuery(String query) throws SQLException{
	/*
	 * TODO: rewrite multithreaded; OK, to be tested
	 * 
	 * 	 */
	/*		ResultSet rs;
			Statement statement = control.bvs.getStatement();
			rs = statement.executeQuery( query );
			control.bvs.releaseStatement(statement);
			return rs;*/
		return new BVUtils(todo.query,query).result;
		}
	public static void doUpdate(String update) throws SQLException{
		ResultSet rs;
		Statement statement = control.bvs.getStatement();
		statement.executeUpdate( update );
		control.bvs.releaseStatement(statement);
		}
}
/*package de.flg_informatik.buecherverwaltung;

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
*/