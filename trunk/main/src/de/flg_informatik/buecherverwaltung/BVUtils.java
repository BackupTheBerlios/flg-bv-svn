package de.flg_informatik.buecherverwaltung;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BVUtils implements Runnable{
	enum todo{
		query,
		update,
		remove,
		count,
		exists,
		insert
		}
	
	static Connection connection;
	static BVControl control;
	
	private todo what; 
	private String command;
	private ResultSet result=null;
	private int count=0;
	
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
		BVUtils.control=control;
		BVUtils.connection=connection;
	}
	
	public void run() {
		Statement statement = control.bvs.getStatement();
		try{
			switch (this.what){
			case query:
				result = statement.executeQuery( command );
				control.bvs.releaseStatement(statement);
				break;
			case insert:	
			case update:
				statement = control.bvs.getStatement();
				count=statement.executeUpdate(command);
				control.bvs.releaseStatement(statement);
				break;
			case remove:
				statement = control.bvs.getStatement();
				statement.executeUpdate(command);
				control.bvs.releaseStatement(statement);
				break;
			case count:
				statement = control.bvs.getStatement();
				result=statement.executeQuery(command);
				control.bvs.releaseStatement(statement);
				break;
			case exists:
				statement = control.bvs.getStatement();
				result=statement.executeQuery(command);
				control.bvs.releaseStatement(statement);
				break;
			}
			
		}catch(SQLException sqle){
			sqle.printStackTrace();
			
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
	
	public static synchronized	ResultSet doQuery(String query) throws SQLException{
	/*
	 * TODO: rewrite multithreaded - done, to be tested
	 */
		ResultSet ret=new BVUtils(todo.query,query).result;
		ret.first();
		return ret;
	}
	
	public static synchronized int doUpdate(String update) {
	
		return (new BVUtils(todo.update, update )).count;
	}
	
	public static synchronized int doCount(String query){
		BVUtils bvutils=new BVUtils(todo.count,query);
		try{
			bvutils.result.absolute(1);
			return bvutils.result.getInt(1);
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		return -1;		
	}
	public static synchronized boolean doesExist(String query){
		BVUtils bvutils=new BVUtils(todo.exists,query);
		try{
			if (bvutils.result.next()){
				return true;
			}else{
				return false;
			}
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		return false;		
	}
	
	public static synchronized int doInsert(String query){
		
		return (new BVUtils(todo.insert, query )).count;
		
	}
	
	static private void debug(Object obj){
		System.out.println(BVUtils.class+": "+ obj);
	}
}
