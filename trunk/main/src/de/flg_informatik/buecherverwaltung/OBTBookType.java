package de.flg_informatik.buecherverwaltung;

import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Statement;

import de.flg_informatik.ean13.Ean;

public class OBTBookType implements BVConstants {
	
	public BigInteger ISBN;
	public String Author;
 	public String Title;
 	public String Publisher;
 	
 	
	
 	public OBTBookType(BigInteger isbn, String author, String title,
			String publisher) {
		super();
		ISBN = isbn;
		
		Author = author;
		Title = title;
		Publisher = publisher;
	}

	
 	public static String getTitle(Ean ISBN){
 		Statement statement = Control.getControl().bvs.getStatement();
 		try {
 			
			return USQLQuery.doQuery("SELECT Title FROM Booktypes WHERE ISBN="+ISBN,statement).getString(1);
		} catch (SQLException e) {
			//new Err("Kein Titel fur ISBN "+ISBN+" gefunden!");
			return null;
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		
 	}
 	/*public static boolean isISBN(Ean ean){
 		
 	}*/
 	public static boolean isKnownISBN(Ean ISBN){
 		
 		 if (USQLQuery.doCount("SELECT COUNT(ISBN) FROM Booktypes WHERE ISBN="+ISBN)==0){
 			 return false;
 		 }else{
 			 return true;
 		 }	 
 	}
 	
 	
 	
 	
 	

}
