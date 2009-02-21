package de.flg_informatik.buecherverwaltung;

import java.math.BigInteger;
import java.sql.SQLException;

import de.flg_informatik.buecherverwaltung.BVUtils.todo;
import de.flg_informatik.ean13.Ean;

public class BVBookType {
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

	
 	public static String getTitle(BigInteger ISBN){
 		
 		try {
			return BVUtils.doQuery("SELECT Title FROM Booktypes WHERE ISBN="+ISBN).getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
 	}
 	
 	
 	

}
