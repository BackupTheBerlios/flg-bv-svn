package de.flg_informatik.buecherverwaltung;

import de.flg_informatik.ean13.Ean;

public class OBLBookLease {
	public final static boolean debug=false;
	public final static String btablename = "Books";
	public final static String ltablename = "Leases";
	OBLBookLease(OBook book, OClass class1){
		new Deb(8,"");
		if (USQLQuery.doUpdate("UPDATE "+btablename+" SET Location= " + class1.KID +
				"Scoring_of_Condition="+book.Scoring_of_condition+
				" WHERE ID = "+book.ID)==1){
			Control.log("Ausleihe an " +class1.Name + ", Buch: " + book.ID + " " + OBTBookType.getTitle(Ean.getEan(book.ISBN)) + " ("+ book.ISBN+")");
		}
	}
	

}
