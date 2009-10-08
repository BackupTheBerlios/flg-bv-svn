package de.flg_informatik.buecherverwaltung;
import java.util.Stack;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * @author notkers
 * 
 * Utility for Storing used "expensive" objects 
 *
 */
public class UStorage {
	Stack<Statement> statements=new Stack<Statement>();
	Connection connection;
	Control control;
	public UStorage(Control control,Connection connection){
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


