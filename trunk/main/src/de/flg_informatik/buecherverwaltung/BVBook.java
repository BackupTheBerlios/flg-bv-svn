package de.flg_informatik.buecherverwaltung;

import java.math.BigInteger;
import java.sql.SQLException;

public class BVBook {
	public BigInteger ID;
	public String Purchased;
 	public int Scoring_of_condition;
 	public BigInteger Location;
 	public BigInteger ISBN;
	
 	public BVBook(BigInteger id, String purchased, int scoring_of_condition,
			BigInteger location, BigInteger isbn) {
		super();
		ID = id;
		Purchased = purchased;
		Scoring_of_condition = scoring_of_condition;
		Location = location;
		ISBN = isbn;
	}
 	
 	public String getTitle(){
 		
 		try {
			return BVUtils.doQuery("SELECT Title FROM Booktypes WHERE ISBN="+ISBN).getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
 	}

}
