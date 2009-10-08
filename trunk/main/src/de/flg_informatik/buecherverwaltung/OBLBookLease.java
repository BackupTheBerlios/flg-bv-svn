package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;

import de.flg_informatik.buecherverwaltung.MainGUI.LogPane;
import de.flg_informatik.ean13.Ean;

public class OBLBookLease {
	public final static boolean debug=false;
	public final static String btablename = "Books";
	public final static String ltablename = "Leases";
	OBLBookLease(OBook book, OClass class1){
		new Deb(8,"");
		if (USQLQuery.doUpdate("UPDATE "+btablename+" SET Location= " + class1.KID +
				" WHERE ID = "+book.ID)==1){
			Control.log("Ausleihe an " +class1.Name + ", Buch: " + book.ID + " " + OBTBookType.getTitle(new Ean(book.ISBN)) + " ("+ book.ISBN+")");
		}
	}
	

}
