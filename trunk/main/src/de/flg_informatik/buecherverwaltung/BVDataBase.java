package de.flg_informatik.buecherverwaltung;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

public class BVDataBase {
	public static boolean getDataBankDrivers(Properties props){
		
		try{
			Class.forName( "sun.jdbc.odbc.JdbcOdbcDriver" );
			Class.forName( "com.mysql.jdbc.Driver" );
		}catch(Exception e){
			e.printStackTrace();
			for ( Enumeration<Driver> f = DriverManager.getDrivers(); f.hasMoreElements(); ){
				  debug( f.nextElement().getClass().getName() );
			}
			return false;
		}
		return true;
	}
	public static Connection getConnection(Properties props){
		Connection connection=null;	
		try{
			connection = DriverManager.getConnection( props.getProperty(".datenbank.connection.prefix","jdbc:mysql:")+
				props.getProperty("datenbank.connection.server")+":"+
				props.getProperty(".datenbank.connection.port","3306")+"/"+	
				props.getProperty("datenbank.datenbankname"),
				props.getProperty("datenbank.username"),
				props.getProperty("*datenbank.passwort"));
		}catch(SQLException sqle){
			sqle.printStackTrace();
			return null;
		}
		return connection;
	}
	public static boolean CloseConnection(Connection connection){
		try{
			connection.close();
			// TODO to be added transaction code
			return true;
		}catch(SQLException sqle){
			
		}
		return false;
		
	}
	public static void main(String[] args) {
		
	}
	static private void debug(Object obj){
		System.out.println(BVDataBase.class+": "+ obj);
	}
}
