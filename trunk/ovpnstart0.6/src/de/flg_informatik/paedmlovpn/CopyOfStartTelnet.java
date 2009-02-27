package de.flg_informatik.paedmlovpn;

import java.io.InputStream;
import java.io.PrintWriter;
import org.apache.commons.net.telnet.TelnetClient;
import de.flg_informatik.utils.shell.CallBackInputStreamReader;
import de.flg_informatik.utils.shell.CallBackInputStreamReaderListener;

public class CopyOfStartTelnet implements Runnable,CallBackInputStreamReaderListener{
	final static int stattninit=0;
	final static int stattnfound=10;
	final static int stattnconn=20;
	final static int stattnask=25;
	final static int stattnvpnprepw=30;
	final static int stattnvpngetpw=40;
	final static int stattnvpncheckpw=50;
	final static int stattnvpncheckvpn=70;
	final static int stattnvpngotpw=60;
	final static int stattnvpncheckstat=80;
	final static int stattnvpnconn=90;
	final static int stattnfinalize=900;
	final static int stattnokexit=1000;
	final static int inctnerr=1;
	final static int inctnidle=8;
	final static int tnactionconnect=0;
	final static int tnactiongetzustand=10;
	final static int tnactiondisconnect=20;
	final static int tnactionfinish=30;
	
	
	
	static CopyOfStartTelnet run;
	StartVPN svpn;
	int port;
	int action;
	int count=0;
	PwdDialog pwdialog;
	PrintWriter sendstream;
	InputStream instream;
	int timeout=50;
	String[] watchstrings = new String[] {
			".*OpenVPN Management Interface Version 1",
			">HOLD:Waiting for hold release", 
			"SUCCESS: hold release succeeded\n",
			".*Need 'Private Key' password.*",
			"ERROR: the 'password' command requires 2 parameters",
			".*'Private Key' password entered, but not yet verified.*",
			"Auth read bytes",
			">FATAL:Error: private key password verification failed\n",
			".*java.net.ConnectException: Connection refused(-2).*",
			"ERROR: The 'status' command is not supported by the current daemon mode\n"

			};
	TelnetClient tnclient;
	private boolean isready=false;
	private boolean isconnected=false;
	int bufpos = 0;
	protected int status=-2;
	Thread thisthread = null;
	CallBackInputStreamReader cbisr;

	static synchronized CopyOfStartTelnet getOVPNTelnet(StartVPN svpn, int action, int port){
		run = new CopyOfStartTelnet(svpn, port);
		if (run.connect(port)){
			
			run.cbisr=new CallBackInputStreamReader(run,run.instream,run.watchstrings);
			run.action=tnactionconnect;
			run.thisthread = new Thread(run);
			run.thisthread.start();
			return run;
		}else{
			return null;
		}
	}
	
	private CopyOfStartTelnet(StartVPN svpn, int port) {
		super();
		this.svpn=svpn;
		this.port=port;
	}
	
	

	public void run() {
		
		while(action <= tnactionfinish){
			try{
				this.wait(10);
			}catch(InterruptedException e){
				System.out.println(e);
			}
			switch (action){
				case tnactionconnect:
					status=stattninit;
					isready=true;
					isconnected=false;
					while(status<stattnokexit){
						
						
						count++;
						
						switch (status){
						case stattnfound:
							if (count>timeout){
								send("hold release");
							}
							status=stattnvpncheckvpn;
							break;
						case stattnvpnprepw:
							pwdialog = new PwdDialog(svpn,"Passphrase");
							status = stattnvpngetpw;
							break;
						case stattnvpngotpw:
							if (count > timeout){
								status=stattnvpncheckvpn;
							}
							break;
							
						case stattnvpncheckvpn:
							send("status");
							status = stattnvpncheckstat;
							break;	
							
						case stattnfinalize:
							status=stattnokexit;
							break;
						}
						
						
					}
				case tnactionconnect+inctnidle:
					isconnected=true;
				default: 	
					
			}
			
			
		}
		cbisr.stop();	
		printMessage("Beendet " + Integer.toString(status));

	}
	public void inputMatch(String string, int which) {
	
		System.out.println(status+" inputMatch:("+which+") "+ string);
		switch (which){
		case 0:
			status = stattnfound;
			count=0;
			break;
		case 1:
			send("hold release");
			status=stattnconn;
			break;
		case 2:
			action=tnactionconnect+inctnidle;
			status=stattnfinalize;
			break;
		case 3:
		// case stattnask+1:
			status = stattnvpnprepw;
			break;
		case 4:
			printMessage("Leere Passwörter sind nicht erlaubt!" + " <" +string+"> ");
			status = stattnvpnprepw;
			break;
		case 5:
			printMessage(" <" +string+"> ");
			status = stattnvpngotpw;
			count=0;
			break;
		case 6:
			printMessage(" connectet ");
			action = tnactionconnect+inctnidle;
			status=stattnfinalize;
			break;	
		case 7:
			printMessage(" <" +string+"> ");
			status = stattnfinalize;
			svpn.disconnected();
			action = tnactionconnect+tnactionfinish+inctnerr;
			break;	
			
		default:
			System.out.println("default");
			
			
		}
		
		System.out.println("inputMatch:("+which+") "+ string);
		
	}
	

	boolean connect(int port) {
		svpn.connecting();
		try{
		tnclient = new org.apache.commons.net.telnet.TelnetClient();
		tnclient.connect("localhost", port);
		}catch(Exception e){
			printMessage("Error connect to OVPN(telnet):"+e);
			printMessage(" ... openVPN Service auf Port "+ port +" nicht zu finden, läuft der Service überhaupt? ");
			svpn.disconnected();
			return false;
		}
		svpn.connwait();
		sendstream=new PrintWriter(tnclient.getOutputStream());
		instream=tnclient.getInputStream();
		svpn.connected();
		return true;
	}

	protected void beenden() {
		status=100;
		svpn.setAktion("...Frontend wird beendet!");
		svpn.tncfinished();
	}
	
	public boolean isReady(){
		return isready;
	}
	public boolean isConnected(){
		return isconnected;
	}

	protected synchronized void send(String tosend) {
		sendstream.print(tosend);
		sendstream.write('\r');
		sendstream.println();
		sendstream.flush();
	}
	
	void printMessage(Object obj){
		if (run.svpn.intelnet!=null){
			run.svpn.intelnet.append("OVPN(telnet): "+obj + "(" + status +")"+System.getProperties().getProperty("line.separator"));
		}
	}
	

	void pause(int i){
		svpn.pause(i);
	}
	
	
	void fehler(String fstr){
		System.out.println(fstr);
		beenden();
	}

	

	public void inputReadln(String string) {
		System.out.println("inputReadln ("+status+"): "+ string);
	}

}