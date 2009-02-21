package de.flg_informatik.buecherverwaltung;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BVBooksDatamodell extends javax.swing.table.AbstractTableModel{
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private Connection connection;
	private BVControl control;
	ArrayList<String> headers;
	public BVBooksDatamodell(BVControl control, Connection con) {
		this.control=control;
		this.connection=con;
		this.headers=getColumnHeaders("Books");
		
	}
	
		
	
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	
	ArrayList<String> getColumnHeaders(String tablename){
		ArrayList<String> ret=new ArrayList<String>();
		try{
			ResultSet rs=BVUtils.doQuery("DESCRIBE "+tablename);
			rs.first();
			do{
				ret.add(rs.getString(1));
				debug(rs.getString(1));
				
			}
			while(rs.next());
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		return ret;
			
	}
	
	
	static private void debug(Object obj){
		System.out.println(BVControl.class+": "+ obj);
	}
}
