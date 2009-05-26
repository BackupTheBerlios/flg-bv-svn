package de.flg_informatik.buecherverwaltung;

import java.awt.print.Book;
import java.util.Hashtable;

public class BVSwitchUsecases extends Hashtable<BVSelectedEvent.SelectedEventType, BVUsecases> {
	public BVSwitchUsecases() {
		for (BVSelectedEvent.SelectedEventType selev:BVSelectedEvent.SelectedEventType.values()){
			switch (selev){
			case BookLeasedSelected:
				this.put(selev,BVUsecases.StapelRückgabe);
				break;
			case BookUnknownSelected:
			case ISBNBuySelected:
			case ISBNUnknownSelected:
			case ISBNSelected:
			case BTedit:
			case BTinfo:
			case BTnew:
							
				this.put(selev,BVUsecases.Buchtypen);
				break;
			
			case BookFreeSelected:
			case PersonSelected:
			case EanUnknown:	
				this.put(selev,BVUsecases.Buchtypen);
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
}
