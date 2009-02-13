package de.flg_informatik.buecherverwaltung;

import java.math.BigInteger;
import java.util.EventObject;
import java.util.Vector;

import de.flg_informatik.ean13.Ean;

public class BVSelectedEvent extends EventObject implements Runnable, BVSelectedEventListener{
	/**
	 * 
	 */
	public enum SelectedEventType{
		ISBNSelected,
		BookSelected,
		PersonSelected,
		BTinfo,
		BTnew,
		BTedit;
		
	}
	private SelectedEventType id=null;
	private Ean ean=null;
	private int wildcards=0;
	private boolean copyfinished=false;
	private static Vector<BVSelectedEventListener> listenerlist = new Vector<BVSelectedEventListener>();
	
	
	public BVSelectedEvent(Object source, SelectedEventType id, Ean ean, int wildcards){
		super(source);
		this.id=id;
		this.ean=ean;
		this.wildcards=wildcards;
	}
	
	public static void makeEvent(Object source, SelectedEventType id){
		BVSelectedEvent.makeEvent(source, id, null, 0);
	}
	public static void makeEvent(Object source, SelectedEventType id, Ean ean){
		BVSelectedEvent.makeEvent(source, id, ean, 0);
	}
	public static void makeEvent(Object source, SelectedEventType id, Ean ean, int wildcards){
		BVSelectedEvent me=new BVSelectedEvent (source, id, ean, wildcards);
		Thread thread=new Thread(me);
		thread.start();
		manipulateList(me,true);
	}
	
	public static void addBVSelectedEventListener(BVSelectedEventListener listener){
		manipulateList(listener,true);
	}
	public static void removeBVSelectedEventListener(BVSelectedEventListener listener){
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
		//debug("copy");
		Vector<BVSelectedEventListener> dispatchlist = new Vector<BVSelectedEventListener>();
		for (BVSelectedEventListener listener:listenerlist ){
			dispatchlist.add(listener);
		}
		//debug("copyfinished");
		copyfinished=true;
		debug("Event to Dispatch:"+this.source+", "+this.id+", "+this.ean+", "+this.wildcards);
		for (BVSelectedEventListener listener:dispatchlist ){
			debug(listener);
			listener.thingSelected(new BVSelectedEvent (source, id, ean, wildcards));
		}
	
	}
	private static synchronized void manipulateList(BVSelectedEventListener listener, boolean add){
		if (listener.getClass()==BVSelectedEvent.class){
			debug("locked");
			try{
				while(!((BVSelectedEvent)listener).copyfinished){
					Thread.sleep(1);
				}
			}catch(InterruptedException ie){
			}
			debug("unlocked");
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
	private static void debug(Object o) {
		System.out.println(o);
		
	}

	public void thingSelected(BVSelectedEvent e) {
		// stub für um mit BVSelectedEvent(this) manipulateList() zu locken;
		
	}

}
