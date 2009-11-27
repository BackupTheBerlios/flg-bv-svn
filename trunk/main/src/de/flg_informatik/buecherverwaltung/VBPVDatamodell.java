package de.flg_informatik.buecherverwaltung;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
	
public class VBPVDatamodell extends javax.swing.table.AbstractTableModel implements BVConstants{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String tablename = "Booktypes";
	
	String[] headers={
			"Fach",
			"Titel",
			"ISBN",
	}	;/*"soll",
			"ist",
			"indiv",
			"Lager"
			};
			*/
	
	
	Vector<Vector<Object>> tablecells=new Vector<Vector<Object>>(); 
	int numofcolumns;
	@SuppressWarnings("unused")
	private OClass bvclass;
	public VBPVDatamodell(OClass bvclass) {
		this.numofcolumns=headers.length;
		
		if (bvclass!=null){ //not 1st stage of initialization, no class, no table, no function
			this.fillTable();
			this.bvclass=bvclass;
		}
		
	}
	
	public void remake(OClass bvclass){
		this.bvclass=bvclass;
		if (bvclass!=null){ //not 1st stage of initialization, no class, no table, no function
			fillTable();
		}
		
	}
	
	
	

	






	private synchronized boolean fillTable(){
		Vector<Object> tablerow;
		boolean result = true;
		Statement statement = Control.getControl().bvs.getStatement();
		try{
			new Deb(debug,"filling: "+getColumnCount()+", "+getRowCount());
			tablecells.clear();
			ResultSet rs=USQLQuery.doQuery("SELECT * FROM "+tablename, statement);
			rs.beforeFirst();
			while(rs.next()){	
				tablerow=new Vector<Object>(numofcolumns);
				for (int i=1; i<=numofcolumns;i++ ){
					tablerow.add(rs.getObject(i));
					}
				tablecells.add(tablerow);
			}
			new Deb(debug,"filled: "+getColumnCount()+", "+getRowCount());
		}catch(SQLException sqle){
			sqle.printStackTrace();
			result = false;
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		return result;

	}
	
	
	
	
	public int getBookCount(String ean){
		return (USQLQuery.doCount("SELECT COUNT(ISBN) FROM Books WHERE ISBN="+ean.toString()));
	}
	
	public int getFreeBookCount(String ean){
		return (USQLQuery.doCount("SELECT COUNT(ISBN) FROM Books WHERE ((ISBN="+ean.toString()+ ") AND (Location = 1))"  ));
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
		return "AAA";
		//return tablecells.get(rowIndex).get(columnIndex);
	}
	public String getColumnName(int column){
		return headers[column];
	}
	
	
}
