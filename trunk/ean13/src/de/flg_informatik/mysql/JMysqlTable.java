package de.flg_informatik.mysql;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;



public class JMysqlTable extends JTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7817282755662242170L;
	private MysqlTabelle mysqlTabelle;
	
	public JMysqlTable(MysqlTabelle mysqlTabelle)
	{
		super(mysqlTabelle.getData(), mysqlTabelle.getSpaltenNamen());
		
		this.mysqlTabelle = mysqlTabelle;
	}
	
	public void tableChanged(TableModelEvent e)
	{
		super.tableChanged(e);
		
		// Tabelle wurde editiert
		int zeile = e.getFirstRow();
		int spalte = e.getColumn();
		
		// Falls Spalte oder Zeile ungültig ist
		if (zeile == -1 || spalte == -1)
			return;
		
		// MySql-Tabelle aktualisieren
		this.mysqlTabelle.update(zeile, spalte);
		
		System.out.println("Datenbank aktualisiert");
	}
}
