package de.flg_informatik.buecherverwaltung;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.flg_informatik.buecherverwaltung.BVUtils.todo;
import de.flg_informatik.ean13.Ean;

public class BVBook {
	public static final BigInteger praefix=new BigInteger("200000000000");
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
 	
 	public static String getISBN(BigInteger ID){
 		
 		try {
			return BVUtils.doQuery("SELECT ISBN FROM Books WHERE ID="+ID).getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
 	}
 	public static synchronized Ean makeBookEan(BigInteger num){
 		
 		return new Ean(num.add(praefix));
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
 		
 		
 		if ((howmany=BVUtils.doInsert("INSERT INTO Books VALUES "+strbuf))==0){ // something went wron
 			return null;
 		}
 		ret=new Ean[howmany];
 		try {
 			ResultSet result=BVUtils.doQuery("SELECT * FROM Books WHERE ISBN="+ISBN);
 			for (int i=0;i<howmany;i++){
 				result.absolute(i+1);
 	 			ret[i]=new Ean(result.getString("ID"));
 	 			BVUtils.doUpdate("UPDATE Books SET ISBN=" +new BigInteger(ISBN).add(subtract13).toString() + " WHERE ID="+result.getString("ID"));
 	 		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
 		
 		
 		
 		return ret;
 		
 		
 		
 	}

}
