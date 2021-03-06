package de.flg_informatik.buecherverwaltung;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.flg_informatik.Etikett.EtikettDruck;
import de.flg_informatik.ean13.Ean;

public class TestData {

	/**
	 * @param args
	 */
	public static void printBooks() {
		Ean ret[]=new Ean[280];
		Statement statement = Control.getControl().bvs.getStatement();
		try {
 			ResultSet result=USQLQuery.doQuery("SELECT * FROM Books WHERE Location <> 1 LIMIT 280",statement);
 			
 			for (int i=0;i<280;i++){
 				result.absolute(i+1);
 	 		
 	 			ret[i]=Ean.getEan(new BigInteger(result.getString("ID")).add(OBook.Book12));
 	 		}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			Control.getControl().bvs.releaseStatement(statement);
		}
		EtikettDruck.etikettenDruck(ret);
	}
	
}
