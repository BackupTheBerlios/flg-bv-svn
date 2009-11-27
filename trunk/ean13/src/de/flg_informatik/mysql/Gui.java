package de.flg_informatik.mysql;
import java.awt.Button;

import javax.swing.JScrollPane;

public class Gui extends de.flg_informatik.utils.FLGFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

		
		//  Fenster sichtbar machen
		this.setVisible(true);
		
		// Größe setzen
		this.setBounds(100, 100, 200, 300);
	}
		
	public void setData(MysqlTabelle mysqlTabelle)
	{
		// JTable erstellen mit den Parametern: Namen der Zeilen und der
		// Inhalt der Tabelle
		this.table = new JMysqlTable(mysqlTabelle);
		
		// JScrollPane für JTable erstellen
		this.scrollPane = new JScrollPane(this.table);
		
		// JScrollPane dem Frame hinzufügen
		this.add(this.scrollPane);
		
		pack();		
	}
}
