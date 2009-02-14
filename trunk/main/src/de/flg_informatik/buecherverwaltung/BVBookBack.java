package de.flg_informatik.buecherverwaltung;

import java.sql.Connection;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

public class BVBookBack extends BVView {

	/**
	 * Zun�chst nur R�ckgabe des Buches,
	 * R�ckmeldung �ber Zustand,
	 * <auf Knopfdruck Abfrage wieviele B�cher dieses Booktypes 
	 * aus dieser Location noch fehlen>
	 * Sch�lerkonto auf separatem Tab ...  
	 * 
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	public BVBookBack(BVControl bvc,Connection connection){
		this.bvc=bvc;
	}
	
	
	private BVControl bvc;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void itemSelected(ListSelectionEvent e) {
		// No List, no Selection nurh
		
	}

	public void thingSelected(BVSelectedEvent e) {
		switch (e.getId()){
			case BookLeasedSelected:
				
				break;
			default:
				bvc.thingSelected(e);
		// TODO Auto-generated method stub
		}
		
	}

}
