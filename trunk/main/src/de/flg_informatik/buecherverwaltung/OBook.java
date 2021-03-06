package de.flg_informatik.buecherverwaltung;

import java.awt.Dimension;
import java.awt.Graphics;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.print.PrintException;

import de.flg_informatik.ean13.Ean;
import de.flg_informatik.ean13.EanCanvas;

public class OBook implements de.flg_informatik.Etikett.PrintableEtikett, BVConstants{
	public BigInteger ID;
	public String Purchased;
 	public int Scoring_of_condition;
 	public BigInteger ISBN;
	
 	public OBook(BigInteger id, String purchased, int scoring_of_condition, BigInteger isbn) {
		
		ID = id;
		Purchased = purchased;
		Scoring_of_condition = scoring_of_condition;
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
				ISBN = new BigInteger(rs.getString("ISBN"));
			}else{
				new Err();
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
 					ISBN = new BigInteger(rs.getString("ISBN"));
 				}else{
 					new Warn("Diese Buch-ID ist zwar g�ltig \n aber das Buch ist nicht in der Datenbank! \n" +
 							"  Bitte zugeh�rigen Buchtyp w�hlen!" );
 					new Notimpl();
 				}
 				debug(rs.getWarnings());
 				
 			}catch(SQLException sqle){
 				sqle.printStackTrace();
 			}finally{
 				Control.getControl().bvs.releaseStatement(statement);
 			}
 			debug("built book"+ ID);
 				
 			
 		
 	}
 	public static synchronized boolean isOBook(BigInteger id) {
 		Statement statement = Control.getControl().bvs.getStatement();
		
		try{
			
			ResultSet rs=USQLQuery.doQuery("SELECT * FROM Books WHERE ID="+id,statement);
			if (rs.first()){
				return true;
			}else{
				return false;
			}
			
		}catch(SQLException sqle){
			new Err(sqle);
		}catch(NullPointerException e){
			new Err("Die Datenbank antwortet nicht!");
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		debug("checked book"+ id);
		return false;	
		
	}
 	public static boolean isOBook(Ean ean) {
 		return isOBook(makeBookID(ean));
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
 				return Ean.getEan(rs.getString(1));
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
			return Ean.getEan(USQLQuery.doQuery("SELECT ISBN FROM Books WHERE ID="+ID,statement).getString(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		
 	}
 	
 	public static synchronized Ean makeBookEan(BigInteger num){
 		
 		
 		return Ean.getEan(num.add(Book12));
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
 		if (debug>0) System.out.println(OBook.class+": "+ obj);
	}

	public int printAt(Graphics g, Dimension position, Dimension boxgroesse) {
		try {
			return (new EanCanvas(getEan(),OBTBookType.getTitle(Ean.getEan(this.ISBN)),0.6).printAt(g, position, boxgroesse));
		} catch (PrintException e) {
			new Err(e);
			return 0;
		}
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
				new Warn("Buch "+Id +" hat einen Zustand schlechter als 1 \n L�sche es nicht!");
				new Notimpl();
			}
		}else{
			new Warn("Buch "+Id +" ist ausgeliehen an: "+OClass.getBVClass(book.getLeaser()).Name+"! \n L�sche es nicht!");
			
		}
		return false;
		
	}

	
}
