package de.flg_informatik.paedmlovpn;
import java.util.concurrent.TimeoutException;

import de.flg_informatik.utils.shell.OSCommand;

public class MountDirWin extends MountVPNDir implements Runnable {
	boolean ret;
	String dir;
	String lw;
	String[][] mountdirs;
	
	MountDirWin(java.util.LinkedList<String[]> mountdirs){
		super();
		this.mountdirs = new String[2][mountdirs.size()];
		this.mountdirs=mountdirs.toArray(this.mountdirs);
	}
			
	public void run() {
		//printMessage("");
		switch(action){
		case actionmount:
			conncount=0;
			printDebug(mountdirs.length/2);
			for (int i=0; i< mountdirs.length/2; i++){
				status=statinit;
				if (mountDir(mountdirs[i])) conncount++ ;
				if (stopme){
					i=100;
				}
			}			
			break;
		case actionumount:
			conncount=mountdirs.length;
			for (int i=0; i< mountdirs.length; i++){
				status=ustatinit;
				if (umountDir(mountdirs[i])) conncount-- ;
			}			
			break;
		
		default: printError("MountDirWin: wrong usage");
		}
		printMessage("finished MountDirWin ");

	}
		
	private boolean mountDir(String[] thedir){
		
		this.dir=thedir[0];
		this.lw=thedir[1];
		
		printMessage("trying to mount " + host + this.dir + " as "+ lw);
		
		ret = false;
		
		while ((status  !=statfinish)&& !stopme){
			
			switch(status){
				
				case statinit:
					// already mounted ?
					if (askMnt(lw)) {
						status=statfinalize;
						printMessage(dir + " already mounted, skipping ..." );
						ret=true;
					}else{
						status=statprepw;
					}
					break;
				case statprepw:
					status=statgetpw;
					if (passwort==null){
						pwdialog = new PwdDialog(svpnctrl,"Passwort für "+dir);
					}else{
						pwdialog = new PwdDialog(svpnctrl,"Passwort für "+dir,passwort);
					}
					break;
				case statcheckpw:
					int res;
					try{
					switch(res=OSCommand.execute(new String[] {"c:\\windows\\system32\\net.exe use "+lw+" "+ host+dir +" /USER:"+username+" "+passwort+" /PERSIST:no"},new java.io.File("C:\\"),null,new String[]{".*Anmeldung fehlgeschlagen: unbekannter Benutzername oder falsches Kennwort*.",".*Der Netzwerkpfad wurde nicht gefunden.*"},false,15000)){
					case 0:
						status=statsucmnt;
						break;
					case 2:
						printMessage("Passwort falsch?!");
						passwort=null;
						status=statprepw;
						break;
					case 131074:
						status=statfinalize;
						printMessage("Netzwerkverbindung nicht mehr verfügbar! -- beende");
						stopme=true;
						break;
					default:
						printError( this.toString()+ "(" + status +")" + " OSCExitCode: " + res );
						status=statfinalize;
						break;
					}
					}catch(TimeoutException e){
						printDebug(e);
					}
					break;
				case statsucmnt:
					printMessage(dir + " mounted");
					status=statfinalize;
					ret =true;
					break;
				case stattunerr:
					printMessage("Fehler: VPN-Tunnel nicht nutzbar!");
					ret = false;
					status=statfinalize;
				case statfinalize:
					status=statfinish;
					break;
					
			}

		}
		return ret;	
	}
	
	private boolean umountDir(String[] thedir){
		int res;
		this.dir=thedir[0];
		this.lw=thedir[1];
		ret=false;
		printMessage("trying to unmount " + dir);
		while (status  !=ustatfinish){
	
			switch(status){
				case ustatinit:
					if (!askMnt(lw)){
						printMessage(dir + " not mounted, skipping ..." );
						status=ustatfinalize;
					}else{
						switch(res=OSCommand.execute(new String[]{"c:\\windows\\system32\\net.exe use "+lw+" /DELETE"}, new java.io.File("C:\\"))){
						case 0:
							printMessage(dir + " un-mounted ..." );
							status=ustatfinalize;
							ret=true;
							break;
						
							
						default:
							printError( this.toString() + " OSCExitCode: " + res );
							break;
					
						}
					}
					
				case ustatfinalize:
					status=ustatfinish;
					break;
					
			}
			
				
				
		}
		return ret;	
	}
	
	
	public void inputMatch(String string, int which){
	
	}
	
	public void errMatch(String string, int which) {
		

	}

	public void inputReadln(String string){
		printDebug("inputReadln: "+ string); // only for Debugging ...
	}
	
	public void errReadln(String string) {
		switch (status){
			default:
				if (string.length() > 2){ // there are unintended empty error-lines out there ...
					printError("unexpected ErrReadln: " + string);
				}
		}
		
		
	}
	
	private boolean askMnt(String lw){
		int res;
		printDebug("askMnt: c:\\windows\\system32\\net.exe use "+ lw);
		switch(res=OSCommand.execute(new String[] {"c:\\windows\\system32\\net.exe use "+ lw}, new java.io.File("C:\\"),false)){
			case 0:
			return true;
		
			case 2:
			case -1:	
			return false;
			
			default:
				printError("askMnt(): " + this.toString() + " OSExitCode: " + res );
			return false;
			}
	}
	
			
	
	public static void main(String[] args){
		String[] cmd=new String[1];
		cmd[0]="c:\\windows\\system32\\net.exe use t: \\\\10.4.0.1\\tausch /USER:hr 87NN17 /PERSIST:no";
		System.out.println(OSCommand.execute(cmd));
		                                               //{"cmd","/C", "set","PATH="+System.getenv("PATH"),"&","set","&","net","start"}
		
	}
}
