package de.flg_informatik.paedmlovpn;



import de.flg_informatik.utils.Pause;
import de.flg_informatik.utils.shell.OSCommand;

public class MountDirLin extends MountVPNDir implements Runnable {
	boolean ret;
	String dir;
	String[] mountdirs;
	
	MountDirLin(java.util.LinkedList<String> mountdirs){
		super();
		this.mountdirs= new String[mountdirs.size()];
		mountdirs.toArray(this.mountdirs);
		
	}
			
	public void run() {
		//printMessage("");
		switch(action){
		case actionmount:
			for (int i=0; i< mountdirs.length; i++){
				conncount=0;
				status=statinit;
				if (mountDir(mountdirs[i])) conncount++ ;
				if (stopme) break;
			}			
			break;
		case actionumount:
			conncount=mountdirs.length;
			for (int i=0; i< mountdirs.length; i++){
				status=ustatinit;
				if (umountDir(mountdirs[i])) conncount-- ;
			}			
			break;
		
		default: printError("MountDirLin: wrong usage");
		}
		printMessage("finished MountDirLin ");

	}
		
	private boolean mountDir(String directory){
		
		
		this.dir=directory;
		printMessage("trying to mount " + dir);
		
		ret = false;
		
		while (status  !=statfinish){
	
			switch(status){
			
				case statinit:
					// already mounted ?
					if (askMnt(dir)) {
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
					switch(res=OSCommand.execute(new String[]{"bash","-c","PASSWD="+passwort+"; export PASSWD; mount "+dir  },
								new String[]{(".*Access denied.*\n"),".*Connection to server failed\n"} ,new String[]{".*can\'t find .* in /etc/fstab or /etc/mtab\n"},false)){
					
					case 256+1:
						printMessage("Das Passwort ist falsch!");
						passwort=null;
						status=statinit;
						break;
					case 2*256+1:
						printMessage("Fehler: VPN-Tunnel nicht nutzbar!");
						status=statfinalize;
						break;

					case 2*256*256+1:
						printError("Sie müssen "+ dir + " in /etc/fstab eintragen");
						break;
					case 0:
						status=statsucmnt;
						break;
						
					default:
						printError( this.toString() + " OSCExitCode: " + res );
						break;
						
					}
					
					 // there is no positive recognition by smbmount ...
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
			new Pause(1);
		}
		return ret;	
	}
	
	private boolean umountDir(String dir){
		int res;
		this.dir=dir;
		ret=false;
		printMessage("trying to unmount " + dir);
		while (status  !=ustatfinish){
			switch(status){
				case ustatinit:
					if (!askMnt(dir)){
						printMessage(dir + " not mounted, skipping ..." );
						ret=true;
						status=ustatfinalize;
					}else{
						printMessage(dir + " mounted, unmounting ..." );
						switch(res=OSCommand.execute(new String[]{"bash", "-c", " umount "+ dir + " ; RES=$? ; sleep 1 ; exit $RES "  },
								new String[]{(".*Access denied.*\n"),".*Connection to server failed\n"} ,new String[]{".*can\'t find .* in /etc/fstab or /etc/mtab\n"},false)){
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
			new Pause(10);
				
				
		}
		return ret;	
	}
	
	
	public void inputMatch(String string, int which){
		printDebug("inputMatch: "+string+" Nr( "+which);
	}
	
	public void errMatch(String string, int which) {
		printDebug("errMatch: "+string+" Nr( "+which);

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
	
	private boolean askMnt(String dir){
		int res;
		printDebug("askMnt");
		switch(res=OSCommand.execute(new String[]{"bash", "-c", "mount"},
				new String[] {".*"+ dir.substring(1) + ".*\n"},false)){
		case 256:
			return true;
		
		case 0:
			return false;
			
		default:
			printError("askMnt(): " + this.toString() + " OSExitCode: " + res );
			return false;
		}
	}
	
}
