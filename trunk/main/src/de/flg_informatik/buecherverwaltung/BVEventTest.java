package de.flg_informatik.buecherverwaltung;

import de.flg_informatik.buecherverwaltung.BVSelectedEvent.SelectedEventType;
import de.flg_informatik.ean13.Ean;

public class BVEventTest implements BVSelectedEventListener{
	int num;
	static int mynum=0;

	public BVEventTest(){
		num=mynum++;
		BVSelectedEvent.addBVSelectedEventListener(this);
	}
	
	public void thingSelected(BVSelectedEvent e) {
		System.out.print(e.getSource());
		try{
			Thread.sleep(100);
		}catch(InterruptedException ie){
			
		}
		
	}
	public String toString(){
		return "BVEventTest("+num+")";
	}
	public static void main(String[] args) {
		BVEventTest et = new BVEventTest();
		for (int i=1;i<2000;i++){
			if (i%100==0){
				BVEventTest el = new BVEventTest();
			}
			BVSelectedEvent.makeEvent(et, SelectedEventType.BookUnknownSelected, new Ean("0"));
			
		}
	}

}
