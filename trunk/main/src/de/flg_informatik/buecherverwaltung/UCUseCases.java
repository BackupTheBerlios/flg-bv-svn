package de.flg_informatik.buecherverwaltung;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JPanel;


public enum UCUseCases {
	
	Rückgabe (new VBBVBookBackView()),
	Ausleihe (new VBLVBookLeaseView()),
	Buchtypen (new VBTVBookTypeView()),
	Einstellungen (new VPVPropertyView()),
	Datenimport (new VBVCSVImporterView()),
	// Vorbereitung(new VBPVBookPrepareView()),
	// VorbereitungTest(new VBLPVBookLeasePreView());
	;
	final private static boolean debug=true;
	private static Hashtable<UCCase, UCUseCases> reverse;// is initialized in static void waitForReverse()
	final private static Selected2Usecases selected2usecases = new Selected2Usecases();
	final public Vector <SelectedEvent.SelectedEventType> ConsumedEvents;//
	final public UCCase view;
	private UCUseCases(){
		view=null;
		ConsumedEvents=null;
	}
	
	private UCUseCases(UCCase view){
		waitForReverse(); // early Initializion, needed in Constructor
		this.view=view;
		view.setName(this.name());
		doAdd(view, this);
		ConsumedEvents=view.getConsumedEvents();
		new Deb(debug,ConsumedEvents );
	}
	public static void toClose(){
		for (UCUseCases ucase:UCUseCases.values()){
			ucase.view.toClose();
		}
	}
	
	public static UCUseCases getUsecase(UCCase view){
		return reverse.get(view);
	}
	public static Selected2Usecases getSelected2Usecases(){
		return selected2usecases;
	}
	static class Selected2Usecases extends Hashtable<SelectedEvent.SelectedEventType, UCUseCases> {
		 Selected2Usecases() {
			for (SelectedEvent.SelectedEventType selev:SelectedEvent.SelectedEventType.values()){
				switch (selected=selev){
				case BookLeasedSelected:
					put(Rückgabe);
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
				case BookTypOnTop:	
					put(Buchtypen);
					break;
				
				case BookFreeSelected:
				case BLISBNSelected:	
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
			 * for (SelectedEvent.SelectedEventType selev:SelectedEvent.SelectedEventType.values()){
			 *	System.out.println(selev+" "+this.get(selev));
			 *	}
			 */
		}
		SelectedEvent.SelectedEventType selected;
		final public UCUseCases put(UCUseCases usecase){
				return super.put(selected,usecase);
					
		}
		
	}
	final private static void doAdd(UCCase view, UCUseCases usecase){
		new Deb(true,reverse );
		reverse.put(view, usecase);
		new Deb(true,reverse );
		
	}
	final private void  waitForReverse(){
		if(reverse==null){
			reverse=new Hashtable<UCCase, UCUseCases>();
			new Deb(true,reverse );
		}
	}
}