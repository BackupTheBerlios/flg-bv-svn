package de.flg_informatik.buecherverwaltung;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.flg_informatik.buecherverwaltung.BVUtils.todo;
import de.flg_informatik.ean13.Ean;

public class BVBook {
	public static final BigInteger Book12=new BigInteger("200000000000");
	public BigInteger ID;
	public String Purchased;
 	public int Scoring_of_condition;
 	public BigInteger Location;
 	public BigInteger ISBN;
	
 	public BVBook(BigInteger id, String purchased, int scoring_of_condition,
			BigInteger location, BigInteger isbn) {
		
		ID = id;
		Purchased = purchased;
		Scoring_of_condition = scoring_of_condition;
		Location = location;
		ISBN = isbn;
	}
 	
 	public BVBook(BigInteger id) {
	
		debug("new book"+ id);
		try{
			ResultSet rs=BVUtils.doQuery("SELECT * FROM Books WHERE ID="+id);
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
 	
 	public BVBook(Ean ean) {
 		
 			debug("new book"+ ean);
 			try{
 				ResultSet rs=BVUtils.doQuery("SELECT * FROM Books WHERE ID="+makeBookID(ean));
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
 		new BVNI();
 		return true;	
	}
 	
 	public static synchronized int getLocation(BigInteger ID){
 		debug("getLocation "+ID);
 		try {
			return BVUtils.doQuery("SELECT Location FROM Books WHERE ID="+ID).getInt(1);
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
			return new Ean(BVUtils.doQuery("SELECT ISBN FROM Books WHERE ID="+ID).getString(1));
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
 		if (isBookEan(ean)){
 			return ((ean.getEan().divide(BigInteger.TEN)).subtract(Book12));
 		}else{
 			debug("no bookean");
 			return null;
 		}
 	}
 	
 	public synchronized static boolean isBookEan(Ean ean){
 		if(Ean.checkEan(ean)[0]==Ean.Result.ok){
 			if (ean.toString().startsWith("20")){
 				return true;
 			}
 		}
 	return false;
 		
 	}



 	
 	
 	public static synchronized Ean[] makeNewBooks(int howmany, String ISBN){
 		// make temporay ISBN
 		final BigInteger subtract13=new BigInteger("13000000000000");
 		// subtract from Bookworld ISBN 379X 378X to private EAN 249X 248X, does'nt change control digit
 		ISBN=new BigInteger(ISBN).subtract(subtract13).toString();
 		Ean[] ret;
 		String emptyline="(null,null,1,1,"+ISBN+")";
 		StringBuffer strbuf=new StringBuffer();
		strbuf.append(emptyline);
 		for (int i=1;i<howmany;i++){
 			strbuf.append(","+emptyline);
 		}
 		
 		
 		if ((howmany=BVUtils.doInsert("INSERT INTO Books VALUES "+strbuf))==0){ // something went wrong
 			return null;
 		}
 		ret=new Ean[howmany];
 		try {
 			ResultSet result=BVUtils.doQuery("SELECT * FROM Books WHERE ISBN="+ISBN);
 			for (int i=0;i<howmany;i++){
 				result.absolute(i+1);
 	 			BVUtils.doUpdate("UPDATE Books SET ISBN=" +new BigInteger(ISBN).add(subtract13).toString() + " WHERE ID="+result.getString("ID"));
 	 			ret[i]=new Ean(new BigInteger(result.getString("ID")).add(Book12));
 	 		}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
 		
 		
 		
 		return ret;
 		
 		
 		
 	}
 	static private void debug(Object obj){
		// System.out.println(BVBook.class+": "+ obj);
	}

}
