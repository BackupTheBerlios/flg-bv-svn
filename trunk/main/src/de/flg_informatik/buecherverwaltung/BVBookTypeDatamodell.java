package de.flg_informatik.buecherverwaltung;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import de.flg_informatik.ean13.Ean;
	
public class BVBookTypeDatamodell extends javax.swing.table.AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	enum Result{
		ok,
		isbnexists,
		isbnmalformed,
		unknown;
	}
	Connection connection;
	BVControl control;
	Vector<String> headers;
	BVBookTypView myview;
	Vector<Vector<Object>> tablecells=new Vector<Vector<Object>>(); 
	int numofcolumns;
	public BVBookTypeDatamodell(BVControl control, Connection con,BVBookTypView myview) {
		this.control=control;
		this.connection=con;
		this.myview=myview;
		this.headers=getColumnHeaders("Booktypes");
		this.numofcolumns=getNumOfDBColumns();
		this.fillTable();
		debug("fertig");
	}
	
	
	
	private int getNumOfDBColumns(){
		try{
			ResultSet res=BVUtils.doQuery("DESCRIBE Booktypes");
			res.last();
			return res.getRow();
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		return 0;
	}
	private int getNumOfDBRows(){
		try{
			ResultSet res=BVUtils.doQuery("SELECT COUNT(ISBN) FROM Booktypes");
			res.absolute(1);
			return res.getInt(1);
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		return 0;
	}
	private boolean fillTable(){
		Vector<Object> tablerow;
		boolean result = true;
		debug(getNumOfDBColumns());
		debug(getNumOfDBRows());
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
	
	
	private Vector<String> getColumnHeaders(String tablename){
		Vector<String> ret=new Vector<String>();
		try{
			ResultSet rs=BVUtils.doQuery("DESCRIBE "+tablename);
			rs.first();
			do{
				ret.add(rs.getString(1));
				//debug(rs.getString(1));
				
			}
			while(rs.next());
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		return ret;
			
	}
	public Result setNewBooktype(Vector<String> newvec){
		
		String isbn=newvec.firstElement();
		debug(isbn);
		if (Ean.checkEan(new Ean(isbn))[0]==Ean.Result.ok){
				if (!isInDataBase(isbn)){
					debug("not yet here!");
					StringBuffer update=new StringBuffer();
					for (int i=0; i<numofcolumns;i++ ){
								if (i==0) update.append(headers.get(i)+"='"+newvec.get(i).toString()+"'");
								if (i>1) update.append(" , "+headers.get(i)+"='"+newvec.get(i).toString()+"'");
								//debug(newvec.get(i).toString());
								};
					try{
						BVUtils.doUpdate("INSERT Booktypes SET "+ update.toString() +";");
						this.fillTable();
						return Result.ok;
					}catch(Exception sqle){
						sqle.printStackTrace();
					}
				}else{
					debug("ISBN already here");
					return Result.isbnexists;
				}
			
		}else{
			debug("isbnmalformed");
			return Result.isbnmalformed;
		}
		return Result.unknown;
		
	}
	public boolean isInDataBase(String isbn){
		try{
			ResultSet rs=BVUtils.doQuery("SELECT * FROM Booktypes WHERE ISBN="+isbn);
			if (rs.next()){
				debug("ISBN already in DB!");
				return true;
			}else{
				debug("ISBN not yet in DB!");
				return false;
			}
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		return false;
	}
	public Vector<String>  getBookType(Ean isbn){
		Vector<String> ret=null;
		if (isbn!=null&&isInDataBase(isbn.toString())){
			ret=new Vector<String>();
			ret.add(isbn.toString());// we got that at least;
			for (Vector<Object> vec : tablecells){ // ISBN suchen
				//debug(vec.get(0));
				//debug(isbn.toString());
				if (vec.get(0).toString().equals(isbn.toString())){
					ret=new Vector<String>();
					for (int i=0; i<vec.size();i++ ){
						if (vec.get(i)!=null){
							ret.add(vec.get(i).toString());
						}else{
							ret.add("");
						}
						};
				}
			}
		}
		return ret;
		
	}
	public  boolean setBookType(Vector<String> newvec){
		StringBuffer update=new StringBuffer();
		for (Vector<Object> vec : tablecells){
			if (vec.get(0).toString().equals(newvec.get(0).toString())){
				for (int i=0; i<numofcolumns;i++ ){
					vec.set(i, newvec.get(i).toString());
					if (i==1) update.append(headers.get(i)+"='"+newvec.get(i).toString()+"'");
					if (i>1) update.append(" , "+headers.get(i)+"='"+newvec.get(i).toString()+"'");
					
					//debug(newvec.get(i).toString());
					};
				}
			}
		try{
			BVUtils.doUpdate("UPDATE Booktypes SET "+ update.toString() + " WHERE ISBN="+newvec.get(0));
		}catch(Exception sqle){
			sqle.printStackTrace();
			return false;
		}
		fillTable();
		return true;
		
	}
	
	static private void debug(Object obj){
		System.out.println(BVBookTypeDatamodell.class+": "+ obj);
	}
	/**
	 *  Implemtation of Datamodel
	 */ 

	
	public int getColumnCount() {
		return headers.size();
	}

	public int getRowCount() {
		return tablecells.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return tablecells.get(rowIndex).get(columnIndex);
	}
	public String getColumnName(int column){
		return headers.get(column);
	}
	
	
}
