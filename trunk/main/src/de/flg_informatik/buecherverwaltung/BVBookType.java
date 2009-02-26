package de.flg_informatik.buecherverwaltung;

import java.math.BigInteger;
import java.sql.SQLException;

import de.flg_informatik.buecherverwaltung.BVUtils.todo;
import de.flg_informatik.ean13.Ean;

public class BVBookType {
	
	public static final BigInteger ISBNNull12 = new BigInteger("978000000000");
	public static final Ean ISBNNullEan = new Ean(ISBNNull12);
	public BigInteger ISBN;
	public String Author;
 	public String Title;
 	public String Publisher;
 	
 	
	
 	public BVBookType(BigInteger isbn, String author, String title,
			String publisher) {
		super();
		ISBN = isbn;
		Author = author;
		Title = title;
		Publisher = publisher;
	}

	
 	public static String getTitle(Ean ISBN){
 		
 		try {
			return BVUtils.doQuery("SELECT Title FROM Booktypes WHERE ISBN="+ISBN).getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
 	}
 	public static boolean isISBN(Ean ean){
 		if (Ean.checkEan(ean)[0]==Ean.Result.ok){
 			if (ean.toString().startsWith("978")||ean.toString().startsWith("979")){
 				return true;
 			}
 		}
 		return false;
 	}
 	public static boolean isKnownISBN(Ean ISBN){
 		
 		 if (BVUtils.doCount("SELECT COUNT(ISBN) FROM Booktypes WHERE ISBN="+ISBN)==0){
 			 return false;
 		 }else{
 			 return true;
 		 }	 
 	}
 	
 	static private void debug(Object obj){
		//System.out.println(BVBookType.class+": "+ obj);
	}
 	
 	
 	

}
