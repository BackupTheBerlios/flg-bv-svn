package de.flg_informatik.buecherverwaltung;

import de.flg_informatik.buecherverwaltung.SelectedEvent.SelectedEventType;
import de.flg_informatik.ean13.Ean;

public class TestEventTest implements SelectedEventListener{
	int num;
	static int mynum=0;

	public TestEventTest(){
		num=mynum++;
		SelectedEvent.addBVSelectedEventListener(this);
	}
	
	public void thingSelected(SelectedEvent e) {
		System.out.print(e.getSource());
		try{
			Thread.sleep(100);
		}catch(InterruptedException ie){
			
		}
		
	}
	public String toString(){
		return "TestEventTest("+num+")";
	}
	public static void main(String[] args) {
		TestEventTest et = new TestEventTest();
		for (int i=1;i<2000;i++){
			if (i%100==0){
				new TestEventTest();
			}
			SelectedEvent.makeEvent(et, SelectedEventType.BookUnknownSelected, new Ean("0"));
			
		}
	}

}
