package de.flg_informatik.buecherverwaltung;

import java.math.BigInteger;
import java.util.EventObject;
import java.util.Vector;

import de.flg_informatik.ean13.Ean;

public class SelectedEvent extends EventObject implements Runnable, SelectedEventListener{
	/**
	 * 
	 */
	private static boolean debug=false;
	public enum SelectedEventType{
		ISBNSelected,
		ISBNUnknownSelected,
		ISBNBuySelected,
		BookUnknownSelected,
		BookFreeSelected,
		BookLeasedSelected,
		PersonSelected,
		BTinfo,
		BTnew,
		BTedit,
		EanUnknown,
		BLClassSelected, 
		BLISBNSelected, BookTypOnTop;
	}
	private SelectedEventType id=null;
	private Ean ean=null;
	private int wildcards=0;
	private boolean copyfinished=false;
	private static Vector<SelectedEventListener> listenerlist = new Vector<SelectedEventListener>();
		
	
	public SelectedEvent(Object source, SelectedEventType id, Ean ean, int wildcards){
		super(source);
		this.id=id;
		this.ean=ean;
		this.wildcards=wildcards;
	}
	
	public static void makeEvent(Object source, SelectedEventType id){
		
		SelectedEvent.makeEvent(source, id, null, 0);
	}
	public static void makeEvent(Object source, SelectedEventType id, Ean ean){
		new Deb(20,id);
		SelectedEvent.makeEvent(source, id, ean, 0);
	}
	public static void makeEvent(Object source, SelectedEventType id, Ean ean, int wildcards){
		SelectedEvent me=new SelectedEvent (source, id, ean, wildcards);
		Thread thread=new Thread(me);
		thread.start();
		manipulateList(me,true);
	}
	
	public static void addBVSelectedEventListener(SelectedEventListener listener){
		manipulateList(listener,true);
	}
	public static void removeBVSelectedEventListener(SelectedEventListener listener){
		manipulateList(listener,false);
	}
	public SelectedEventType getId(){
		return id;
	}
	public Ean getEan(){
		return ean;
	}
	public int getWildcards(){
		return wildcards;
	}
	
	public void run(){
		new Deb(debug,"copy");
		Vector<SelectedEventListener> dispatchlist = new Vector<SelectedEventListener>();
		for (SelectedEventListener listener:listenerlist ){
			dispatchlist.add(listener);
		}
		new Deb(debug,"copyfinished");
		copyfinished=true;
		new Deb(debug,"Event to Dispatch:"+this.source+", "+this.id+", "+this.ean+", "+this.wildcards);
		for (SelectedEventListener listener:dispatchlist ){
			new Deb(debug,listener.getClass());
			listener.thingSelected(new SelectedEvent (source, id, ean, wildcards));
		}
	
	}
	private static synchronized void manipulateList(SelectedEventListener listener, boolean add){
		if (listener.getClass()==SelectedEvent.class){
			new Deb(debug,"locked");
			try{
				while(!((SelectedEvent)listener).copyfinished){
					Thread.sleep(0,0);
				}
			}catch(InterruptedException ie){
			}
			new Deb(debug,"unlocked");
		}else{
			if (add){
				if (!listenerlist.contains(listener)){
					listenerlist.add(listener);
				}	
			}else{
				listenerlist.remove(listener);
			}
		}
	}
	

	
	private static final long serialVersionUID = 1L;
	

	public void thingSelected(SelectedEvent e) {
		// stub für um mit SelectedEvent(this) manipulateList() zu locken;
		
	}

}
