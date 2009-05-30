package de.flg_informatik.buecherverwaltung;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class BVUtils implements Runnable{
	private static boolean debug=false;
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
		//new Thread(this).start();
		//this.waitForSemaphore();
		run();
	}
	public BVUtils(){
		 
	}
	
	public BVUtils(BVControl control, Connection connection){
		BVUtils.control=control;
		BVUtils.connection=connection;
	}
	
	public synchronized void run() {
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
	public static synchronized int doDelete(String query){
		
		return (new BVUtils(todo.remove, query )).count;
		
	}
	public static Vector<String> getColumnHeaders(String tablename){
		Vector<String> ret=new Vector<String>();
		try{
			ResultSet rs=BVUtils.doQuery("DESCRIBE "+tablename);
			rs.first();
			do{
				ret.add(rs.getString(1));
				 new BVD(debug,rs.getString(1));
				
			}
			while(rs.next());
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		return ret;
			
	}
	public static int getNumOfDBColumns(String tablename){
		try{
			ResultSet res=BVUtils.doQuery("DESCRIBE " + tablename);
			res.last();
			return res.getRow();
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		return 0;
	}
	public static int getNumOfDBRows(String tablename){

		return BVUtils.doCount("SELECT COUNT * FROM tablename");
	}
	public static Vector<String> getSetTokens(String tablename, String col_set){
		Vector<String> set = new Vector<String>();
		ResultSet rs;
		String[] res;
		try{
			rs=BVUtils.doQuery("SHOW COLUMNS FROM "+ tablename +" LIKE '" + col_set +"'");
			if (rs.first()){
				new BVD(debug,rs.getString("Type"));
					res=rs.getString("Type").split(new String("(set\\(')|(',')|('\\))"));
				
				for (int i=0; i< res.length;i++){ // this is strange! it starts with a ""
					if (!res[i].equals("")){
						set.add(res[i]);
						new BVD(debug,res[i]);
					}
				}
			}
		}catch(SQLException sqe){
			sqe.printStackTrace();
		}
		
		return (set);
	}
	

}
