package de.flg_informatik.mysql;



public class Main {
	public static void main(String[] args)
	{
		Gui gui = Gui.getInstance();
		
		// lade die Daten einer Tabelle
//		MysqlTabelle tabelle = new MysqlTabelle("namenliste");
		MysqlTabelle tabelle = new MysqlTabelle("stunden");
		
		// übergebe der Gui die Daten
		gui.setData(tabelle);
	}
}
