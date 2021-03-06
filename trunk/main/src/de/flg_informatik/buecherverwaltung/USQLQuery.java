package de.flg_informatik.buecherverwaltung;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class USQLQuery implements Runnable, BVConstants{

	enum todo{
		query,
		update,
		remove,
		count,
		exists,
		insert
		}
	
	static Control control;
	private Statement statement;
	private todo what; 
	private String command;
	ResultSet result=null;
	
	private int count=0;
	
	/*static void setParams(Control control){
		//, Connection connection){
		USQLQuery.control=control;
		// USQLQuery.connection=connection;
	}
	*/
	private USQLQuery(USQLQuery.todo what, String command, Statement statement){
		this.what=what;
		this.command=command;
		this.statement=statement;
		//new Thread(this).start();
		//this.waitForSemaphore();
		run();
	}
	/*public USQLQuery(){
		 
	}
	
	public USQLQuery(Control control ){//, // Connection connection){
		USQLQuery.control=control;
	}
	*/
	
	public synchronized void run() {
		// Statement statement = Control.getControl().bvs.getStatement();
		try{
			switch (this.what){
			case query:
				result = statement.executeQuery( command );
				break;
			case insert:	
			case update:
				count=statement.executeUpdate(command);
				break;
			case remove:
				statement.executeUpdate(command);
				break;
			case count:
				result=statement.executeQuery(command);
				break;
			case exists:
				result=statement.executeQuery(command);
				break;
			}
			
		}catch(SQLException sqle){
			new Err(sqle.getMessage());
			
		}
		
	}
	
	
	
	public static synchronized	ResultSet doQuery(String query, Statement statement) throws SQLException{
	/*
	 * TODO: rewrite multithreaded - done, to be tested
	 */
		ResultSet ret=new USQLQuery(todo.query,query,statement).result;
		ret.first();
		return ret;
	}
	
	public static synchronized int doUpdate(String update) {
		  Statement statement = Control.getControl().bvs.getStatement();
		  int i=(new USQLQuery(todo.update,update, statement)).count;
		  Control.getControl().bvs.releaseStatement(statement);
				return i;
	}
		
	
	public static synchronized int doCount(String query){
		Statement statement = Control.getControl().bvs.getStatement();
		USQLQuery bvutils=new USQLQuery(todo.count,query,statement);
		try{
			bvutils.result.absolute(1);
			return bvutils.result.getInt(1);
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		return -1;		
	}
	public static synchronized boolean doesExist(String query){
		Statement statement = Control.getControl().bvs.getStatement();
		USQLQuery bvutils=new USQLQuery(todo.exists,query,statement);
		try{
			if (bvutils.result.next()){
				return true;
			}else{
				return false;
			}
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		return false;		
	}
	
	public static synchronized int doInsert(String query){
		Statement statement = Control.getControl().bvs.getStatement();
		int i=(new USQLQuery(todo.insert, query,statement )).count;
		Control.getControl().bvs.releaseStatement(statement);
		return i;
		
	}
	public static synchronized int doDelete(String query){
		Statement statement = Control.getControl().bvs.getStatement();
		int i=(new USQLQuery(todo.remove, query, statement )).count;
		Control.getControl().bvs.releaseStatement(statement);
		return i;
		
		
	}
	public static Vector<String> getColumnHeaders(String tablename){
		Vector<String> ret=new Vector<String>();
		Statement statement = Control.getControl().bvs.getStatement();
		try{
			ResultSet rs=USQLQuery.doQuery("DESCRIBE "+tablename,statement);
			rs.first();
			do{
				ret.add(rs.getString(1));
				 new Deb(debug,rs.getString(1));
				
			}
			while(rs.next());
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		return ret;
			
	}
	public static int getNumOfDBColumns(String tablename){
		Statement statement = Control.getControl().bvs.getStatement();
		try{
			ResultSet res=USQLQuery.doQuery("DESCRIBE " + tablename,statement);
			res.last();
			return res.getRow();
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		return 0;
	}
	public static Vector<Boolean>  getNotNullColumns(String tablename){
		Vector<Boolean> ret=new Vector<Boolean>(); 
		Statement statement = Control.getControl().bvs.getStatement();
		try{
			ResultSet res=USQLQuery.doQuery("DESCRIBE " + tablename,statement); //Cols 1-6
			res.beforeFirst();
			while (res.next()){
				ret.add(new Boolean(!res.getBoolean(3)));
				
				new Deb(debug,res.getString(4));
				new Deb(debug,res.getString(5));
				new Deb(debug,res.getString(6));
			}
			
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		return ret;
	}
	public static Vector<String>  getDefaults(String tablename){
		Vector<String> ret=new Vector<String>();
		Statement statement = Control.getControl().bvs.getStatement();
		try{
			ResultSet res=USQLQuery.doQuery("DESCRIBE " + tablename,statement); //Cols 1-6
			res.beforeFirst();
			while (res.next()){
				ret.add(res.getString(5));
			}
			
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		return ret;
	}
	public static int getNumOfDBRows(String tablename){

		return USQLQuery.doCount("SELECT COUNT * FROM tablename");
	}
	public static Vector<String> getEnumTokens(String tablename, String col_set){
		Vector<String> set = new Vector<String>();
		Statement statement = Control.getControl().bvs.getStatement();
		ResultSet rs;
		String[] res;
		try{
			rs=USQLQuery.doQuery("SHOW COLUMNS FROM "+ tablename +" LIKE '" + col_set +"'",statement);
			if (rs.first()){
				new Deb(debug,rs.getString("Type"));
					res=rs.getString("Type").split(new String("(enum\\(')|(',')|('\\))"));
				
				for (int i=0; i< res.length;i++){ // this is strange! it starts with a ""
					if (!res[i].equals("")){
						set.add(res[i]);
						new Deb(debug,res[i]);
					}
				}
			}
		}catch(SQLException sqe){
			sqe.printStackTrace();
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		
		return (set);
	}
	public static Vector<String> getSetTokens(String tablename, String col_set){
		Vector<String> set = new Vector<String>();
		Statement statement = Control.getControl().bvs.getStatement();
		ResultSet rs;
		String[] res;
		try{
			rs=USQLQuery.doQuery("SHOW COLUMNS FROM "+ tablename +" LIKE '" + col_set +"'",statement);
			if (rs.first()){
				new Deb(debug,rs.getString("Type"));
					res=rs.getString("Type").split(new String("(set\\(')|(',')|('\\))"));
				
				for (int i=0; i< res.length;i++){ // this is strange! it starts with a ""
					if (!res[i].equals("")){
						set.add(res[i]);
						new Deb(debug,res[i]);
					}
				}
			}
		}catch(SQLException sqe){
			sqe.printStackTrace();
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		
		return (set);
	}
	public static Vector<String> getAll(String tablename, String col_set){
		Vector<String> set = new Vector<String>();
		Statement statement = Control.getControl().bvs.getStatement();
		ResultSet rs;
		try{
			rs=USQLQuery.doQuery("Select " + col_set + " FROM " + tablename // + " GroupBy " + col_set
					,statement);
			rs.beforeFirst();
			while(rs.next()){ // this is strange! it starts with a ""
				set.add(rs.getString(1));
			}
		}catch(SQLException sqe){
			sqe.printStackTrace();
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		
		return (set);
	}
}
