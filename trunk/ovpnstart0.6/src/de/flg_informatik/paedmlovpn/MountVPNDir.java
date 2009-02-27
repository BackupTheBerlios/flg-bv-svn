package de.flg_informatik.paedmlovpn;

import de.flg_informatik.utils.shell.CallBackShellReaderListener;
import de.flg_informatik.utils.shell.OSShell;
import de.flg_informatik.utils.shell.Osses;
public class MountVPNDir implements CallBackShellReaderListener{
	static final int statinit = 0;
	static final int incaskmnt = 1;
	static final int incsucmnt = 2;
	static final int incerrmnt = 3;
	static final int incnotmnt = 4;
	static final int statprepw = 10;
	static final int statgetpw = 20;
	static final int statcheckpw = 30;
	static final int statfalsepw = 40;
	static final int statrightpw = 50;
	static final int statcheckmnt = 60;
	static final int statnotmnt = statcheckmnt+incnotmnt;
	static final int statsucmnt = statcheckmnt+ incsucmnt;
	static final int staterrmnt = statcheckmnt+ incerrmnt;
	static final int stattunerr = 70;
	static final int statfinalize = 90;
	static final int statfinish = 100;
	static final int ustatfinish = 100;
	static final int ustatinit = 1000;
	static final int ustatumnt = 1030;
	static final int ustatcheckmnt =1060;
	static final int ustatfinalize = 1090;
	static final int actionmount = 1;
	static final int actionumount = 2;
	static final int actioncountmount = 3;
	
	
    	
	//OSShell osshell;
	StartVPNControl svpnctrl;
	StartVPN svpn;
	int status;
	int action;
	int conncount=-1;
	boolean stopme=false;
	PwdDialog pwdialog;
	String dir;
	static MountVPNDir run;
	String	passwort = null;
	String[] ip={""};
	String[] ep={""};
	static Thread mountthread=null;
	static String host;
	static String username;
	MountVPNDir(){
	}
	
		
	public static synchronized MountVPNDir doMountVPNDir(StartVPNControl svpnctrl, int action){
		host=svpnctrl.properties.getProperty("hostname");
		username=svpnctrl.properties.getProperty("username");;

		switch(OSShell.getOS()){
		case linux:{
			java.util.LinkedList<String> mountdirs = new java.util.LinkedList<String>();
			String[] split = svpnctrl.properties.getProperty("linux.mountpoints").split(",");
			for (String s:split){
				mountdirs.add(svpnctrl.properties.getProperty("linux.mountpath")+"/"+s);
			}
			run = new MountDirLin(mountdirs);
			run.action=action;
			run.svpnctrl=svpnctrl;
			run.svpn=svpnctrl.svpn;	
			mountthread = new Thread((MountDirLin)run);
			mountthread.start();
			break;
			}
		case winnt:{
			java.util.LinkedList<String[]> mountdirs = new java.util.LinkedList<String[]>();
			String[] split0 = svpnctrl.properties.getProperty("winnt.mountshares").split(",");
			String[] split1 = svpnctrl.properties.getProperty("winnt.volumes").split(",");
			String[] mtdir = new String[2];
			for (int i=0;i<Math.min(split0.length,split1.length);i++){
				mtdir[0]=split0[i];
				mtdir[1]=split1[i];
				mountdirs.add(mtdir);
			}
			run = new MountDirWin(mountdirs);
			run.action=action;
			run.svpnctrl=svpnctrl;
			run.svpn=svpnctrl.svpn;	
			mountthread = new Thread((MountDirWin)run);
			mountthread.start();
			break;
			}
		}
	return run;
	}
	
	void setPw(String pw){
		this.passwort = pw;
	}
	
	public void inputMatch(String string, int which) {
		printDebug("inputMatch:("+which+") "+ string);
		
	}

	public void errMatch(String string, int which) {
		printDebug("errorMatch:("+which+") "+ string);
		
	}

	public void inputReadln(String string){
		printDebug("inputReadln: "+ string);
	}
	
	public void errReadln(String string) {
		printDebug("errorReadln: " + string);
		
	}
	public boolean isReady() {
	 // TODO Auto-generated method stub
	return mountthread.isAlive();
	}
	public boolean stop(){
		printDebug("Stopp");
		stopme=true;
		return true;
	}


	void printMessage(Object obj){
		if (svpn.intelnet!=null){
			svpn.intelnet.append("MountVPNDIR: "+obj + "(" + status +")"+System.getProperties().getProperty("line.separator"));
			svpn.intelnet.validate();
		}
		
		
	}
	void printDebug(Object obj){
		System.out.println(status+" Debug: " + obj + " in Objekt " + this);
	}
	void printError(Object obj){
		System.out.println(status+" Error: " + obj + " in Objekt " + this);
	}



public static void main(String[] string){
	MountVPNDir t = new MountVPNDir();
	doMountVPNDir(null, t.actionmount);
	
}




}