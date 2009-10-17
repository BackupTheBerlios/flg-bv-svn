package de.flg_informatik.buecherverwaltung;

import java.awt.Dimension;
import java.awt.Graphics;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import de.flg_informatik.buecherverwaltung.USQLQuery.todo;
import de.flg_informatik.ean13.Ean;
import de.flg_informatik.ean13.EanCanvas;

public class OBook implements de.flg_informatik.Etikett.PrintableEtikett{
	public static final boolean debug=true;
	public static final BigInteger Book12=new BigInteger("200000000000");
	public BigInteger ID;
	public String Purchased;
 	public int Scoring_of_condition;
 	public BigInteger Location;
 	public BigInteger ISBN;
	
 	public OBook(BigInteger id, String purchased, int scoring_of_condition,
			BigInteger location, BigInteger isbn) {
		
		ID = id;
		Purchased = purchased;
		Scoring_of_condition = scoring_of_condition;
		Location = location;
		ISBN = isbn;
	}
 	
 	public OBook(BigInteger id) {
	
		debug("new book"+ id);
		try{
			ResultSet rs=USQLQuery.doQuery("SELECT * FROM Books WHERE ID="+id);
			if (rs.first()){
				ID = new BigInteger(rs.getString("ID"));
				Purchased = rs.getString("Purchased"); 
				Scoring_of_condition = rs.getInt("Scoring_of_condition");
				Location = new BigInteger(rs.getString("Location"));
				ISBN = new BigInteger(rs.getString("ISBN"));
			}else{
				debug("no book");
			}
			debug(rs.getWarnings());
			
		}catch(SQLException sqle){
			sqle.printStackTrace();
		}
		debug("built book"+ ID);
			
		
	}
 	
 	public OBook(Ean ean) {
 		
 			debug("new book"+ ean);
 			try{
 				ResultSet rs=USQLQuery.doQuery("SELECT * FROM Books WHERE ID="+makeBookID(ean));
 				if (rs.first()){
 					ID = new BigInteger(rs.getString("ID"));
 					Purchased = rs.getString("Purchased"); 
 					Scoring_of_condition = rs.getInt("Scoring_of_condition");
 					Location = new BigInteger(rs.getString("Location"));
 					ISBN = new BigInteger(rs.getString("ISBN"));
 				}else{
 					debug("no book");
 				}
 				debug(rs.getWarnings());
 				
 			}catch(SQLException sqle){
 				sqle.printStackTrace();
 			}
 			debug("built book"+ ID);
 				
 			
 		
 	}
 	
 	public static int getLocation(Ean ean){
 		return getLocation(makeBookID(ean));	
	}
 	public boolean setBookType(Ean ean){
 		new Notimpl();
 		return true;	
	}
 	
 	public static synchronized int getLocation(BigInteger ID){
 		debug("getLocation "+ID);
 		try {
			return USQLQuery.doQuery("SELECT Location FROM Books WHERE ID="+ID).getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
 	}
 	
 	public static synchronized Ean getISBN(Ean ean){
 		return getISBN(makeBookID(ean));	
	}
 	
 	
 	public static synchronized Ean getISBN(BigInteger ID){
 		debug("getIsbn "+ID);
 		try {
			return new Ean(USQLQuery.doQuery("SELECT ISBN FROM Books WHERE ID="+ID).getString(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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
 		final BigInteger subtract13=new BigInteger("13000000000000");
 		// subtract from Bookworld ISBN 379X 378X to private EAN 249X 248X, does'nt change control digit
 		ISBN=new BigInteger(ISBN).subtract(subtract13).toString();
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
 		try {
 			ResultSet result=USQLQuery.doQuery("SELECT * FROM Books WHERE ISBN="+ISBN);
 			for (int i=0;i<howmany;i++){
 				result.absolute(i+1);
 	 			USQLQuery.doUpdate("UPDATE Books SET ISBN=" +new BigInteger(ISBN).add(subtract13).toString() + " WHERE ID="+result.getString("ID"));
 	 			ret[i]=new OBook(new BigInteger(result.getString("ID")));
 	 		}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
 		
 		
 		
 		return ret;
 		
 		
 		
 	}
 	static private void debug(Object obj){
		// System.out.println(OBook.class+": "+ obj);
	}

	@Override
	public int printAt(Graphics g, Dimension position, Dimension boxgroesse) {
		return (new EanCanvas(getEan(),OBTBookType.getTitle(new Ean(this.ISBN))).printAt(g, position, boxgroesse));
	}

	
}
