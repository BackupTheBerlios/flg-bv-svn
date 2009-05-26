package de.flg_informatik.buecherverwaltung;

public enum BVUsecases {
	
	StapelRückgabe (new BVBookBack()),
	Buchtypen (new BVBookTypeView()),
	Einstellungen (new BVPropertyView());
	;
	
	BVView view;
	BVUsecases(BVView view){
		this.view=view;
		view.setName(this.name());
		
		
	}

}
