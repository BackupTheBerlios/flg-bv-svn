package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;

import de.flg_informatik.buecherverwaltung.BVGUI.LogPane;
import de.flg_informatik.ean13.Ean;

public class BVBookLease {
	public final static boolean debug=false;
	public final static String btablename = "Books";
	public final static String ltablename = "Leases";
	BVBookLease(BVBook book, BVClass class1){
		if (BVUtils.doUpdate("UPDATE "+btablename+" SET Location= " + class1.KID +
				" WHERE ID = "+book.ID)==1){
			BVControl.log("Ausleihe an " +class1.Name + ", Buch: " + book.ID + " " + BVBookType.getTitle(new Ean(book.ISBN)) + " ("+ book.ISBN+")");
		}
	}
	

}
