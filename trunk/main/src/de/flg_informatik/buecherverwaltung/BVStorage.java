package de.flg_informatik.buecherverwaltung;
import java.util.Stack;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
public class BVStorage {
	Stack<Statement> statements=new Stack<Statement>();
	Connection connection;
	BVControl control;
	public BVStorage(BVControl control,Connection connection){
		this.connection=connection;
	}
	
	
	public synchronized Statement getStatement(){
		if (!statements.isEmpty()){
			return statements.pop();
		}else{
			try{
					return connection.createStatement();
			}catch(SQLException sqlexc){
				sqlexc.printStackTrace();
			}
		}
		return null;
	}
	public void releaseStatement(Statement statement){
		statements.push(statement);
		
	}
		
		
}


