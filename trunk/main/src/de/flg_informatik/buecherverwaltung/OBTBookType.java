package de.flg_informatik.buecherverwaltung;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.flg_informatik.ean13.Ean;

public class OBTBookType {
	
	public static final BigInteger ISBNNull12 = new BigInteger("978000000000");
	public static final Ean ISBNNullEan = new Ean(ISBNNull12);
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
 		
 		try {
 			
			return USQLQuery.doQuery("SELECT Title FROM Booktypes WHERE ISBN="+ISBN).getString(1);
		} catch (SQLException e) {
			//new Err("Kein Titel fur ISBN "+ISBN+" gefunden!");
			return null;
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
