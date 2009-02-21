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
		count
		}
	
	static Connection connection;
	static BVControl control;
	
	private todo what; 
	private String command;
	private ResultSet result=null;
	
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
			case update:
				statement = control.bvs.getStatement();
				statement.executeUpdate(command);
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
	
	public static	ResultSet doQuery(String query) throws SQLException{
	/*
	 * TODO: rewrite multithreaded - done, to be tested
	 */
		return new BVUtils(todo.query,query).result;
	}
	
	public static void doUpdate(String update) throws SQLException{
	/*
	 * TODO: rewrite multithreaded - done, to be tested
	 */
		new BVUtils(todo.update, update );
	}
	
	public static int doCount(String query){
		BVUtils bvutils=new BVUtils(todo.count,query);
		try{
			// debug(bvutils.result.toString());
			bvutils.result.absolute(1);
			return bvutils.result.getInt(1);
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		return -1;		
	}
	static private void debug(Object obj){
		System.out.println(BVUtils.class+": "+ obj);
	}
}
