package de.flg_informatik.buecherverwaltung;

import java.awt.Dimension;
import java.awt.Graphics;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.flg_informatik.ean13.Ean;
import de.flg_informatik.ean13.EanCanvas;

public class OBook implements de.flg_informatik.Etikett.PrintableEtikett{
	public static final boolean debug=false;
	public static final BigInteger Book12=new BigInteger("200000000000");
	public BigInteger ID;
	public String Purchased;
 	public int Scoring_of_condition;
 	// public BigInteger Location;
 	public BigInteger ISBN;
	
 	public OBook(BigInteger id, String purchased, int scoring_of_condition,
			//BigInteger location,
 			BigInteger isbn) {
		
		ID = id;
		Purchased = purchased;
		Scoring_of_condition = scoring_of_condition;
		// Location = location;
		ISBN = isbn;
	}
 	
 	public OBook(BigInteger id) {
 		Statement statement = Control.getControl().bvs.getStatement();
		debug("new book"+ id);
		try{
			
			ResultSet rs=USQLQuery.doQuery("SELECT * FROM Books WHERE ID="+id,statement);
			if (rs.first()){
				ID = new BigInteger(rs.getString("ID"));
				Purchased = rs.getString("Purchased"); 
				Scoring_of_condition = rs.getInt("Scoring_of_condition");
				// Location = new BigInteger(rs.getString("Location"));
				ISBN = new BigInteger(rs.getString("ISBN"));
			}else{
				debug("no book");
			}
			debug(rs.getWarnings());
			
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		debug("built book"+ ID);
			
		
	}
 	
 	public OBook(Ean ean) {
 			Statement statement = Control.getControl().bvs.getStatement();
 			debug("new book"+ ean);
 			try{
 				ResultSet rs=USQLQuery.doQuery("SELECT * FROM Books WHERE ID="+makeBookID(ean),statement);
 				if (rs.first()){
 					ID = new BigInteger(rs.getString("ID"));
 					Purchased = rs.getString("Purchased"); 
 					Scoring_of_condition = rs.getInt("Scoring_of_condition");
 					// Location = new BigInteger(rs.getString("Location"));
 					ISBN = new BigInteger(rs.getString("ISBN"));
 				}else{
 					debug("no book");
 				}
 				debug(rs.getWarnings());
 				
 			}catch(SQLException sqle){
 				sqle.printStackTrace();
 			}finally{
 				Control.getControl().bvs.releaseStatement(statement);
 			}
 			debug("built book"+ ID);
 				
 			
 		
 	}
 	
 	private static int getLocation(Ean ean){
 		return getLocation(makeBookID(ean));	
	}
 	public boolean setBookType(Ean ean){
 		new Notimpl();
 		return true;	
	}
 	public void incCondition(){
		Scoring_of_condition++;
		
	}
 	public boolean isInStore(){
 		return (getLeaser(ID)==null);
 	}
 	
 	private static synchronized int getLocation(BigInteger ID){
 		debug("getLocation "+ID);
 		Statement statement = Control.getControl().bvs.getStatement();
 		try {
			return USQLQuery.doQuery("SELECT Location FROM Books WHERE ID="+ID,statement).getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		
 	}
 	public Ean getLeaser(){
 		return getLeaser(ID);
 	}
 	public static synchronized Ean getLeaser(BigInteger ID){
 		debug("getLeaser "+ID);
 		Statement statement = Control.getControl().bvs.getStatement();
		try {
			ResultSet rs=USQLQuery.doQuery("SELECT UserID FROM Leases WHERE LObjectID="+ID + " AND BackTime is null" ,statement);
			rs.beforeFirst();
			if (rs.next()){ // There is a Lease
 				return new Ean(rs.getString(1));
			}else{ // no open Lease
				return null;
			}
 			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
 		
		
 	}
 	
 	public static synchronized Ean getISBN(Ean ean){
 		return getISBN(makeBookID(ean));	
	}
 	
 	
 	public static synchronized Ean getISBN(BigInteger ID){
 		debug("getIsbn "+ID);
 		Statement statement = Control.getControl().bvs.getStatement();
 		try {
			return new Ean(USQLQuery.doQuery("SELECT ISBN FROM Books WHERE ID="+ID,statement).getString(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		
 	}
 	
 	public static synchronized Ean makeBookEan(BigInteger num){
 		
 		
 		return new Ean(num.add(Book12));
 	}
 	
 	public synchronized static BigInteger makeBookID(Ean ean){
 		if (EEANType.getType(ean).equals(EEANType.Book)){
 			return ((ean.getEan().divide(BigInteger.TEN)).subtract(Book12));
 		}else{
 			debug("no bookean");
 			return null;
 		}
 	}
 	
 	public Ean getEan(){
 		return makeBookEan(ID);
 	}



 	
 	
 	public static synchronized OBook[] makeNewBooks(int howmany, String ISBN){
 		// make temporay ISBN
 		final BigInteger subtract13=new BigInteger("6800000000000");
  		// subtract from Bookworld ISBN 979X 978X to private EAN 299X 298X, does'nt change control digit
 		new Deb(ISBN);
 		ISBN=new BigInteger(ISBN).subtract(subtract13).toString();
 		new Deb(ISBN);
 		OBook[] ret;
 		String emptyline="(null,null,1,1,"+ISBN+")";
 		StringBuffer strbuf=new StringBuffer();
		strbuf.append(emptyline);
 		for (int i=1;i<howmany;i++){
 			strbuf.append(","+emptyline);
 		}
 		
 		
 		if ((howmany=USQLQuery.doInsert("INSERT INTO Books VALUES "+strbuf))==0){ // something went wrong
 			return null;
 		}
 		ret=new OBook[howmany];
 		Statement statement = Control.getControl().bvs.getStatement();
 		try {
 			ResultSet result=USQLQuery.doQuery("SELECT * FROM Books WHERE ISBN="+ISBN,statement);
 			for (int i=0;i<howmany;i++){
 				result.absolute(i+1);
 				USQLQuery.doUpdate("UPDATE Books SET ISBN=" +new BigInteger(ISBN).add(subtract13).toString() + " WHERE ID="+result.getString("ID"));
 	 			ret[i]=new OBook(new BigInteger(result.getString("ID")));
 	 		}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
 		
 		
 		
 		return ret;
 		
 		
 		
 	}
  /*	public boolean doUpdate(){
 		return (USQLQuery.doUpdate("UPDATE "+"Books"+ " SET Location=" + Location  +
 				", Scoring_of_Condition="+ Scoring_of_condition + 
 				", Purchased=\""+ Purchased + "\""+ 
				", ISBN="+ ISBN + 
 				" WHERE ID = "+ID)==1);
 	}
 	*/
 	public boolean makeLease(Ean letto){
 		if (getLeaser(ID)==null){
 			// nicht verliehen
 			return(USQLQuery.doInsert("INSERT "+"Leases"+ " SET UserID=" + letto  +
 	 				", LObjectID = "+ ID +
 	 				", OutTime = Now()"
 	 				)==1);
 			
	}else{
 			
 			return false;
 		}
 		
 		
 	}
 	public boolean endLease(){
 			return(USQLQuery.doUpdate("Update "+"Leases"+ " SET BackTime = Now() "+
 					" WHERE LObjectID = "+ ID + " AND BackTime is null"
 	 			)==1);	
 	}
 	
 	public boolean equals(OBook o){
 		if (o==null) return false;
 		return this.ID.equals(o.ID);
 		
 	}
 	static private void debug(Object obj){
 		if (debug) System.out.println(OBook.class+": "+ obj);
	}

	public int printAt(Graphics g, Dimension position, Dimension boxgroesse) {
		return (new EanCanvas(getEan(),OBTBookType.getTitle(new Ean(this.ISBN)),0.6).printAt(g, position, boxgroesse));
	}

	public static boolean delete(BigInteger Id) {
		OBook book=new OBook(Id);
		if (book.isInStore()){
			// in Storage or virtual
			if (book.Scoring_of_condition==1){
				// looks like virtual Book i.e. bad etikett
				USQLQuery.doDelete("DELETE FROM Books WHERE ID="+Id);
				return (true);
			}else{
				// Book to be discarded
				new Warn("Buch "+Id +" hat einen Zustand schlechter als 1 \n Lösche es nicht!");
				new Notimpl();
			}
		}else{
			new Warn("Buch "+Id +" ist ausgeliehen an: "+OClass.getBVClass(book.getLeaser()).Name+"! \n Lösche es nicht!");
			
		}
		return false;
		
	}

	
}
