package de.flg_informatik.buecherverwaltung;

public enum BVUsecases {
	
	StapelR�ckgabe (new BVBookBack()),
	Buchtypen (new BVBookTypeView());
	;
	
	BVView view;
	BVUsecases(BVView view){
		this.view=view;
		view.setName(this.name());
		
		
	}

}
