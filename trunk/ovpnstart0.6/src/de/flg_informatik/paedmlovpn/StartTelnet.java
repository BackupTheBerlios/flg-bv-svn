package de.flg_informatik.paedmlovpn;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.PrintWriter;
import org.apache.commons.net.telnet.TelnetClient;

import de.flg_informatik.utils.Pause;
import de.flg_informatik.utils.shell.CallBackInputStreamReader;
import de.flg_informatik.utils.shell.CallBackInputStreamReaderListener;

public class StartTelnet implements Runnable,CallBackInputStreamReaderListener{
	final static int stattninit=0;
	final static int stattnp0=10;
	final static int stattnreleased=15;
	final static int stattnconn=20;
	final static int stattnask=25;
	final static int stattnvpnprepw=30;
	final static int stattnvpngetpw=40;
	final static int stattnvpncheckpw=50;
	final static int stattnvpncheckvpn=70;
	final static int stattnvpngotpw=60;
	final static int stattnwf6=80;
	final static int stattndisconnecting=200;
	final static int stattnwf9=300;
	final static int stattnp9=400;
	final static int stattnwf10=700;
	final static int stattnp10=800;
	final static int stattnvpnconn=90;
	final static int stattnfinalize=900;
	final static int stattnokexit=1000;
	final static int stattnerrexit=1100;
	final static int tnactionerr=60;
	final static int tnactionconnect=0;
	final static int tnactionconnected=40;
	final static int tnactiondisconnected=50;
	final static int tnactiondisconnect=20;
	final static int tnactionfinish=30;
	
	
	
	static StartTelnet run=null;
	StartVPNControl svpnctrl;
	int port;
	int action;
	int count=0;
	boolean isready=false;
	boolean isfinished=false;
	PwdDialog pwdialog;
	PrintWriter sendstream;
	InputStream instream;
	int timeout=20;
	String[] watchstrings = new String[] {
			".*OpenVPN Management Interface Version 1", //0
			">HOLD:Waiting for hold release", //1 
			"SUCCESS: hold release succeeded\n", //2
			".*Need 'Private Key' password.*", //3
			"ERROR: the 'password' command requires 2 parameters", //4
			".*'Private Key' password entered, but not yet verified.*", //5
			"Auth read bytes", //6
			">FATAL:Error: private key password verification failed\n", //7
			".*java.net.ConnectException: Connection refused(-2).*", //8
			"ERROR: The 'status' command is not supported by the current daemon mode\n", //9
			"SUCCESS: signal SIGHUP thrown\n" //10

			};
	TelnetClient tnclient;
	private boolean isconnected=false;
	int bufpos = 0;
	protected int status=-2;
	Thread thisthread = null;
	CallBackInputStreamReader cbisr;

	static synchronized StartTelnet getOVPNTelnet(StartVPNControl svpnctrl, int action, int port){
			if (run==null){
				run = new StartTelnet(svpnctrl, port);
			}
			run.status=stattninit;
			if (run.connect(port)){
			run.startThread(action);
			return run;
		}else{
			return null;
		}
	
	}
	private synchronized void startThread(int action){
		this.cbisr=new CallBackInputStreamReader(run,run.instream,run.watchstrings);
		this.action=action;
		this.thisthread = new Thread(this);
		this.thisthread.start();
	}
	
	private StartTelnet(StartVPNControl svpnctrl, int port) {
		super();
		this.svpnctrl=svpnctrl;
		this.port=port;
	}
	
	

	public void run() {
		boolean connect=false;
		
		if (action==tnactionconnect){
			connect=true;
		}
		printMessage("Verbinden mit Dienst ");
		
		
		isconnected=false;
		count=0;
		isready=true;
		while(status<stattnokexit){
			//printMessage(status);
			switch (status){
			case stattninit:
				new Pause(100);
				count++;
				if (count>timeout){
					status=stattnerrexit;
				}
				break;
			case stattnp0:
				new Pause(10);
				count++;
				if (!connect){
					status=stattndisconnecting;
				}
				if (count>timeout){
					count=0;
					status=stattnreleased;
				}
				break;
			case stattnreleased:
				new Pause(30);
				count++;
				if (count>timeout){
					count=0;
					status=stattnvpngotpw;
				}
				break;
			case stattnvpnprepw:
				count=0;
				pwdialog = new PwdDialog(svpnctrl,"Passphrase");
				status = stattnvpngetpw;
				break;
			case stattnvpngotpw:
				count++;
				new Pause(50);
				if (count>timeout){
					count=0;
					status=stattnvpncheckvpn;
				}
				break;
			case stattnvpncheckvpn:
				send("status");
				printMessage("VPN prüfen");
				status = stattnwf6;
				count=0;
				break;	
			case stattnwf6:
				new Pause(50);
				count++;
				if (count>timeout){
					count=status;
					status=stattnerrexit;
				}
				break;
			case stattndisconnecting:
				printMessage("Verbindung trennen ");
				printDebug("Disconnecting");
				send("signal SIGHUP");
				count=0;
				status=stattnwf10;
				break;
				
							
			case stattnp9:
		
				count=status;
				status=stattnerrexit;
				break;
			case stattnwf10:
				new Pause(20);
				count++;
				if (count>timeout){
					count=status;
					status=stattnfinalize;
				}
				break;
			case stattnfinalize:
				send("exit");
				status=stattnokexit;
				break;
			}
			
			
			
		}
		
		cbisr.stop(); 	
					
			
			
			
		
		

	}
	public void inputMatch(String string, int which) {
	
		printDebug(status+" inputMatch:("+which+") "+ string);
		switch (which){
		case 0:
			printMessage(string);
			count=0;
			status = stattnp0;
			printMessage("Kommt \"waiting for hold release\"? ");
			
			break;
		case 1:
			count=0;
			if (status<stattndisconnecting){
				printMessage("Ja! Sende hold release");
				send("hold release");
				status=stattnreleased;
			}else{
				status=stattnfinalize;
			}
			break;
		case 2:
			printMessage(string);
			count=0;
			status=stattnvpngotpw;
			break;
		case 3:
			printMessage(string);
			status = stattnvpnprepw;
			break;
		case 4:
			printMessage("Leere Passwörter sind nicht erlaubt!" + " <" +string+"> ");
			status = stattnvpnprepw;
			break;
		case 5:
			printMessage(string + "count="+count+" ");
			status = stattnvpngotpw;
			count=0;
			break;
		case 6:
			printMessage(string);
			action = tnactionconnected;
			status=stattnfinalize;
			isconnected=true;
			printMessage("VPN-Tunnel steht!");
			svpnctrl.svpn.connected();
			break;	
		case 7:
			printMessage(" < Falsche Passphrase! > ");
			
			status = stattnfinalize;
			svpnctrl.svpn.disconnected();
			action = tnactionerr;
			break;	
			
		case 8:
			printMessage(" <" +string+"> ");
			status = stattnfinalize;
			svpnctrl.svpn.disconnected();
			action = tnactionerr;
			break;
		case 9:
			printMessage(" <" +string+"> ");
			status = stattnp9;
			svpnctrl.svpn.disconnected();
			action = tnactiondisconnected;
			break;
		case 10:
			printMessage(" <" +string+"> ");
			status = stattnp10;
			svpnctrl.svpn.disconnected();
			action = tnactiondisconnected;
			break;
		case 11:
			printMessage(" <" +string+"> ");
			status = stattnp10;
			svpnctrl.svpn.disconnected();
			action = tnactiondisconnected;
			break;
		default:
			printMessage(this.toString()+" inputMatch unresolved Error <" +string+"> ");
			action=tnactiondisconnected;
			
		}
		
		
		
	}
	

	boolean connect(int port) {
		svpnctrl.svpn.connecting();
		try{
		tnclient = new org.apache.commons.net.telnet.TelnetClient();
		tnclient.connect("localhost", port);
		}catch(Exception e){
			printMessage("Error connect to OVPN(telnet):"+e);
			printMessage(" ... openVPN Service auf Port "+ port +" nicht zu finden, läuft der Service überhaupt? ");
			svpnctrl.svpn.disconnected();
			return false;
		}
		sendstream=new PrintWriter(tnclient.getOutputStream());
		instream=new BufferedInputStream(tnclient.getInputStream());
		svpnctrl.svpn.connected();
		return true;
	}

	protected void beenden() {
		status=100;
		svpnctrl.svpn.setAktion("...Frontend wird beendet!");
		svpnctrl.svpn.tncfinished();
	}
	
	public boolean isReady(){
		return (isready);
	}
	public boolean isFinished(){
		boolean ret=false;
		try{
			ret=(cbisr.ready());
		}catch(Throwable e){
			printDebug(e);
		}
		return (ret);
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
		//if (run.svpn.intelnet!=null){
			run.svpnctrl.svpn.intelnet.append("OVPN(telnet): "+obj + "(" + status +")"+System.getProperties().getProperty("line.separator"));
		//}
	}
	

		
	void fehler(String fstr){
		System.out.println(fstr);
		beenden();
	}

	

	public void inputReadln(String string) {
		 printDebug("inputReadln ("+status+"): "+ string);
	}
	void printDebug(Object obj){
		//System.out.println("StartTelnet Debug: "+obj);
	}
	

}