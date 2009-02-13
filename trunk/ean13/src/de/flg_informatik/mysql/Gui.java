package de.flg_informatik.mysql;
import java.awt.*;
import javax.swing.*;


import java.awt.Button;

public class Gui extends de.flg_informatik.utils.FLGFrame {
	private static Gui instance = null;
	private JMysqlTable table;
	private JScrollPane scrollPane;
	
	public static Gui getInstance() {
		if (instance == null)
			instance = new Gui();
		return instance;
	}
	
	private Gui()
	{
		Button but;
		
		//  Fenster sichtbar machen
		this.setVisible(true);
		
		// Gr��e setzen
		this.setBounds(100, 100, 200, 300);
	}
		
	public void setData(MysqlTabelle mysqlTabelle)
	{
		// JTable erstellen mit den Parametern: Namen der Zeilen und der
		// Inhalt der Tabelle
		this.table = new JMysqlTable(mysqlTabelle);
		
		// JScrollPane f�r JTable erstellen
		this.scrollPane = new JScrollPane(this.table);
		
		// JScrollPane dem Frame hinzuf�gen
		this.add(this.scrollPane);
		
		pack();		
	}
}
