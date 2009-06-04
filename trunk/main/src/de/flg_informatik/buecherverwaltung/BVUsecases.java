package de.flg_informatik.buecherverwaltung;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JPanel;

public enum BVUsecases {
	
	StapelRückgabe (new BVBookBack()),
	Buchtypen (new BVBookTypeView()),
	Datenimport (new BVCSVImporter()),
	// Einstellungen (new BVPropertyView()),
	Ausleihe (new BVLeaseView());
	;
	final private static boolean debug=true;
	private static Hashtable<BVView, BVUsecases> reverse;// is initialized in static void waitForReverse()
	final private static Selected2Usecases selected2usecases = new Selected2Usecases();
	final public Vector <BVSelectedEvent.SelectedEventType> ConsumedEvents;//
	final public BVView view;
	private BVUsecases(){
		view=null;
		ConsumedEvents=null;
	}
	
	private BVUsecases(BVView view){
		waitForReverse(); // early Initializion, needed in Constructor
		this.view=view;
		view.setName(this.name());
		doAdd(view, this);
		ConsumedEvents=view.getConsumedEvents();
		new BVD(debug,ConsumedEvents );
	}
	
	public static BVUsecases getUsecase(BVView view){
		return reverse.get(view);
	}
	public static Selected2Usecases getSelected2Usecases(){
		return selected2usecases;
	}
	static class Selected2Usecases extends Hashtable<BVSelectedEvent.SelectedEventType, BVUsecases> {
		 Selected2Usecases() {
			for (BVSelectedEvent.SelectedEventType selev:BVSelectedEvent.SelectedEventType.values()){
				switch (selected=selev){
				case BookLeasedSelected:
					put(StapelRückgabe);
					break;
				case BookUnknownSelected:
					put(Buchtypen);
					break;
				case ISBNBuySelected:
				case ISBNUnknownSelected:
				case ISBNSelected:
				case EanUnknown:
				case BTedit:
				case BTinfo:
				case BTnew:
					put(Buchtypen);
					break;
				
				case BookFreeSelected:
					put(Ausleihe);
					break;
				case PersonSelected:
				case BLClassSelected:	
					break;
				
				default:
					throw new InternalError("BVSwitchUsecases has no usecase for: "+ selev);
			
					
				}
				 
				
			}
			
			
			/* Debug only
			 * 
			 * for (BVSelectedEvent.SelectedEventType selev:BVSelectedEvent.SelectedEventType.values()){
			 *	System.out.println(selev+" "+this.get(selev));
			 *	}
			 */
		}
		BVSelectedEvent.SelectedEventType selected;
		final public BVUsecases put(BVUsecases usecase){
				return super.put(selected,usecase);
					
		}
		
	}
	final private static void doAdd(BVView view, BVUsecases usecase){
		new BVD(true,reverse );
		reverse.put(view, usecase);
		new BVD(true,reverse );
		
	}
	final private void  waitForReverse(){
		if(reverse==null){
			reverse=new Hashtable<BVView, BVUsecases>();
			new BVD(true,reverse );
		}
	}
}