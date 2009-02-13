package de.flg_informatik.utils;

public class Pause implements Runnable{
	long mils;
	boolean stop=false;
	public Pause(long mils){
		this.mils=mils;
		new Thread(this).start();
		while(!stop){
			
		}
	}
	public void run(){
		
		try{
			Thread.sleep(mils);
			}catch(Exception e){
				System.out.println("PauseException: "+e);
			}
		stop=true;
	}
	
}
