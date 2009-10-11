package de.flg_informatik.buecherverwaltung;

import de.flg_informatik.ean13.Ean;

public enum EEANType {
	
	ISBN,
	Book,
	BVClass,
	Customer;

	public static EEANType getType(Ean ean){
		if (Ean.checkEan(ean)[0]==Ean.Result.ok){
 			if (ean.toString().startsWith("978")||ean.toString().startsWith("979")){
 				return ISBN;
 			}
 			if (ean.toString().startsWith("20")){
 				return Book;
 			}
 			if (ean.toString().startsWith("21")){
 				return BVClass;
 			}
 			if (ean.toString().startsWith("22")){
 				return Customer;
 			}
 		new Err("Es ist eine nicht zuordenbare, aber gültige Ean eingelesen worden!");
		}else{
			new Err("Ean ungültig!"+ Ean.checkEan(ean)[1]);
		}
 		return null;
	}

}
