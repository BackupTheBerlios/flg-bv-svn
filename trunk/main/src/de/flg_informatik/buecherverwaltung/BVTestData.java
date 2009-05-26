package de.flg_informatik.buecherverwaltung;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.flg_informatik.Etikett.EtikettDruck;
import de.flg_informatik.ean13.Ean;

public class BVTestData {

	/**
	 * @param args
	 */
	public static void printBooks() {
		Ean ret[]=new Ean[280];
		try {
 			ResultSet result=BVUtils.doQuery("SELECT * FROM Books WHERE Location <> 1 LIMIT 280");
 			
 			for (int i=0;i<280;i++){
 				result.absolute(i+1);
 	 		
 	 			ret[i]=new Ean(new BigInteger(result.getString("ID")).add(BVBook.Book12));
 	 		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		EtikettDruck.etikettenDruck(ret);
	}
	
}
