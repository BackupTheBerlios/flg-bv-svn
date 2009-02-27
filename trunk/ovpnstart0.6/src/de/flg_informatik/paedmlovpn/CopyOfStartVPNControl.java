package de.flg_informatik.paedmlovpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.flg_informatik.utils.Pause;

public class CopyOfStartVPNControl implements ActionListener, Runnable {
	StartVPN svpn=null;
	int cstatus;
	int count;
	static final int cinit=0;
	static final int ctnconnok=10;
	static final int cmnt=20;
	static final int cmntok=30;
	static final int cok=35;
	static final int cumnt=40;
	static final int cumntok=50;
	static final int ctnfin=60;
	static final int ctnfinok=70;
	static final int cerror=80;
	boolean beenden;

	StartTelnet stnvpn = null;
	MountVPNDir mvpn = null;
	public CopyOfStartVPNControl(){
		this.svpn=new StartVPN(this);
		cstatus=cinit;
		(new Thread(this)).start();
	}
	
	public static void main(String[] args){
		new CopyOfStartVPNControl();
	}
	
	/*
	public void start(){
		//telnet starten
		cstatus=cinit;
		
		
		
			//warten auf Telnet und OVPN-Verbindung
			count=0;
			while(( count < 20)&!stnvpn.isFinished()&!stnvpn.isConnected()){
				new Pause(500);
				count++;
			}
			if (stnvpn.isFinished()){
				svpn.disconnected();
				svpn.tncfinished();
			}else{
				if (stnvpn.isConnected()){
					cstatus=ctnconnok;
					cstatus=cmnt;
					svpn.mountvpn();
					mvpn=MountVPNDir.doMountVPNDir(svpn,MountVPNDir.actionmount);
					while(mvpn.isReady()){
						new Pause(50);
					}
					svpn.vpnmounted();
					cstatus=cmntok;
				}
			}
		}
			
		
	}
	*/
	public void run(){
		while (cstatus < ctnfinok){
			if (cstatus < cok){
				switch (cstatus){
					case cinit:
						if (beenden){
							cstatus=ctnfinok;
							break;
						}
						svpn.tnconn();
						stnvpn = StartTelnet.getOVPNTelnet(this,StartTelnet.tnactionconnect,7505);
						if (stnvpn == null){
							// Verbindung missglückt
							cstatus=cerror;
							svpn.tncfinished();
						}else{
							while(!stnvpn.isConnected()){
								new Pause(50);
							}
							cstatus = ctnconnok;
						}
					break;
					case ctnconnok:
						if (beenden){
							cstatus=ctnfin;
							break;
						}
						cstatus=cmnt;
						mvpn=MountVPNDir.doMountVPNDir(this,MountVPNDir.actionmount);
						
						
						
					break;
				}
			}
		}
			
		
	}
	
	public void actionPerformed(ActionEvent e) {
				
		if (e.getSource()== svpn.beenden){
				beenden=true;
			}
			
			
			
		
						
		if ((e.getSource().getClass()== PwdDialog.TextEingabe.class)||(e.getSource().getClass()== PwdDialog.OkButton.class)){
			if (cstatus==cmnt){
				mvpn.pwdialog.sendPwdMTD();
				mvpn.status=MountVPNDir.statcheckpw;
			}else{
				stnvpn.pwdialog.sendPwdTNC();
			}
		}
		
		if ((e.getSource().getClass()== PwdDialog.CancelButton.class)){
			if (cstatus==cmnt){
				mvpn.setPw(null);
				mvpn.pwdialog.dispose();
				svpn.beenden.fire();
				//mvpn.status=MountVPNDir.statfinalize;
			}else{
				stnvpn.pwdialog.dispose();
				svpn.beenden.fire();
			}
			
				
		}
	
		if (e.getSource()== svpn.setup){
			svpn.setup();
		}
	}
}
