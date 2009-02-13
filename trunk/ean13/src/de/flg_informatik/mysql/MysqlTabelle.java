package de.flg_informatik.mysql;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;



public class MysqlTabelle {
	// Name der Tabelle
	private String tabellenName;
	
	// Name aller Spalten, gespeichert als String in einem Vector
	private Vector<String> spaltenNamen = new Vector<String>();
	
	// Daten der Tabelle: Zeile, Spalte
	private Vector<Vector<String>> data = new Vector<Vector<String>>();
	
	
	public Vector<String> getSpaltenNamen() {
		return this.spaltenNamen;
	}
	public Vector<Vector<String>> getData() {
		return this.data;
	}
	
	
	public MysqlTabelle(String tabellenName)
	{
		// setze den als Parameter übergeben Namen in die lokale Variable
		this.tabellenName = tabellenName;
		
		Mysql mysql = Mysql.getInstance();
		
		String query;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		Statement st;
		int i, zeilenAnzahl;
		Vector<String> spaltenInhalt;
		
		
		// Das Query
		query = "SELECT "
			  + 	"* "
			  + "FROM "
			  +		"`" + tabellenName + "`;";
		try {
			// neues Statement erzeugen und Query ausführen
			st = mysql.getNewStatement();
			rs = st.executeQuery(query);
		}
		catch (SQLException e) {
			// Tritt ein Fehler beim Abrufen der Tabellendaten auf =>
			// Die Mysql-Tabelle existiert nicht
			System.out.println("Mysql-Tabelle \"" + tabellenName + "\" existiert nicht");
			return;
		}
		
		// Falls der Wert null ist => Fehler
		if (rs == null) {
			System.out.println("Rückgabewert nicht gültig");
			return;
		}
		
		try {
			// MetaData aus dem ResultSet auslesen. MetaData beinhaltet
			// Spaltennamen, etc.
			rsmd = rs.getMetaData();
		}
		catch (SQLException e) {
			// Falls ein Fehler aufgetreten ist
			System.out.println("MetaData konnte nicht ausgelesen werden");
			e.printStackTrace();
			return;
		}
	
		try {
			// Anzahl der Zeilen auslesen
			zeilenAnzahl = rsmd.getColumnCount();
			
			// Zeilennamen auslesen
			for (i=1; i<=zeilenAnzahl; i++) {
				// Jeden Spaltennamen auslesen und in den Vektor "spaltenNamen"
				// speichern
				this.spaltenNamen.add(rsmd.getColumnName(i));
			}
			
			// Daten auslesen
			while(rs.next()) {
				// Aktuelle Spalte in einen neuen Vektor speichern
				spaltenInhalt = new Vector<String>();
				
				for (i=1; i<=zeilenAnzahl; i++) {
					// i-1, da das Array mit dem Index 0, das ResultSet aber
					// mit Index 1 beginnt
					spaltenInhalt.add(rs.getString(i));
				}
				
				// Spalte den Daten hinzufügen
				this.data.add(spaltenInhalt);
			}
			
			// ResultSet und Statment wieder schließen
			rs.close();
			st.close();
		}
		catch (SQLException e) {
			System.out.println("Fehler während dem Auslesen der Daten");
			e.printStackTrace();
		}
	}
	
	public void update(int zeile, int spalte)
	{
		Mysql mysql = Mysql.getInstance();
		String spaltenName, neuerWert, query, id;
		Statement st;
		
		
		// Falls Zeile oder Spalte einen ungültigen Wert hat
		if (zeile > this.data.size() || zeile < 0)
			return;
		if (spalte > this.spaltenNamen.size() || spalte < 0)
			return;
		
		// Spaltennamen und den entsprechenden Wert auslesen
		spaltenName = this.spaltenNamen.get(spalte);
		neuerWert = this.data.get(zeile).get(spalte);
		id = this.data.get(zeile).get(0);
		
		System.out.println("Spalte: \"" + spaltenName + "\"\tNeuer Wert: \"" + neuerWert + "\"");

		// Query erzeugen
		query = "UPDATE "
			  +		"`" + this.tabellenName + "` "
			  +		"SET `" + spaltenName + "`='" + neuerWert + "' "
			  + "WHERE "
			  +		"`id`='" + id + "' "
			  + "LIMIT 1;";
		

		
		try {
			// neues Statement erzeugen und Query ausführen
			st = mysql.getNewStatement();
			st.execute(query);
			st.close();
		}
		catch (SQLException e) {			
			System.out.println("Fehler: " + e.toString());
		}
	}
}
