package de.flg_informatik.mysql;
import java.sql.*;

public class Mysql {
	// singelton-instance
	private static Mysql instance = null;
	
	// Connection-Variable
	private Connection cn;
	
	// Datenbankname
	private final String dbName = "informatik";
	private final String dbBenutzer = "root";
	private final String dbPasswort = "";
	
	
	
	public static Mysql getInstance() {
		if (instance == null)
			instance = new Mysql();
		return instance;
	}
	
	private Mysql()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + this.dbName, this.dbBenutzer, this.dbPasswort );
			System.out.println("Datenbank geöffnet");
		}
		catch (SQLException e) {
			System.out.println("Datenbank konnte nicht geöffnet werden");
			e.printStackTrace();
			System.exit(0);
		}
		catch (ClassNotFoundException e) {
			System.out.println("Connector nicht gefunden");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public Statement getNewStatement() throws SQLException
	{
		Statement st;
		
		try {
			st = this.cn.createStatement();
		}
		catch (SQLException e) {
			System.out.println("Konnte Statement nicht erzeugen");
			e.printStackTrace();
			return null;
		}
		
		return st;
	}
}
