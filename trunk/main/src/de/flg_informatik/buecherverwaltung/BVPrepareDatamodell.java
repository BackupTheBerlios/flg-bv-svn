package de.flg_informatik.buecherverwaltung;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import de.flg_informatik.ean13.Ean;
	
public class BVPrepareDatamodell extends javax.swing.table.AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String tablename = "Booktypes";
	
	String[] headers={
			"Fach",
			"Titel",
			"ISBN",
			"soll",
			"ist",
			"indiv",
			"Lager"
			};
	
	BVBookPrepareView myview;
	Vector<Vector<Object>> tablecells=new Vector<Vector<Object>>(); 
	int numofcolumns;
	public BVPrepareDatamodell(BVBookPrepareView myview, BVClass bvclass) {
		this.myview=myview;
		this.numofcolumns=headers.length;
		if (bvclass!=null){ //not 1st stage of initialization, no class, no table, no function
			this.fillTable();
		}
		
	}
	
	
	
	
	

	






	private synchronized boolean fillTable(){
		Vector<Object> tablerow;
		boolean result = true;
		try{
			tablecells.clear();
			ResultSet rs=BVUtils.doQuery("SELECT * FROM Booktypes");
			rs.beforeFirst();
			while(rs.next()){	
				tablerow=new Vector<Object>(numofcolumns);
				for (int i=1; i<=numofcolumns;i++ ){
					tablerow.add(rs.getObject(i));
					}
				tablecells.add(tablerow);
			}
		}catch(SQLException sqle){
			sqle.printStackTrace();
			result = false;
		}
		return result;

	}
	
	
	
	
	public int getBookCount(String ean){
		return (BVUtils.doCount("SELECT COUNT(ISBN) FROM Books WHERE ISBN="+ean.toString()));
	}
	
	public int getFreeBookCount(String ean){
		return (BVUtils.doCount("SELECT COUNT(ISBN) FROM Books WHERE ((ISBN="+ean.toString()+ ") AND (Location = 1))"  ));
	}
	
	
	
		/**
	 *  Implemtation of Datamodel
	 */ 

	
	public int getColumnCount() {
		return headers.length;
	}

	public int getRowCount() {
		return tablecells.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return tablecells.get(rowIndex).get(columnIndex);
	}
	public String getColumnName(int column){
		return headers[column];
	}
	
	
}
