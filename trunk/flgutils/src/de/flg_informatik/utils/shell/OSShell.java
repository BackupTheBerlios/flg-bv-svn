package de.flg_informatik.utils.shell;

import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

public class OSShell implements CallBackShellReaderListener{
	
	
	//public static final int linux=0;
	//public static final int winnt=1;
	
	public CallBackShellReader cbsr = null;

	private static OSShell osshell;
	private BufferedWriter out;
	private Process pcs;
	
	private OSShell(){
		try{
			switch(getOS()){
			case linux:
				pcs=Runtime.getRuntime().exec("bash  --noediting");
				break;
			case winnt:
				pcs=Runtime.getRuntime().exec(new String[]{"cmd"},new String[]{""},new java.io.File("C:\\"));
				break;	
			default:
				System.out.println("OSShell: "+ this + "Internal Error 1");
				}
			out=new BufferedWriter(new OutputStreamWriter(pcs.getOutputStream()));
			}catch(Exception e){
			
		}
	}
	
	
	
	static public synchronized OSShell getShell(CallBackShellReaderListener shellreader, String[] pat, String[] errpat){
		osshell = new OSShell();
		if (shellreader == null){
			shellreader = osshell;
		}
		osshell.cbsr = new CallBackShellReader (shellreader, osshell.pcs, pat, errpat);
		return 	osshell;
	}
	static public OSShell getShell(CallBackShellReaderListener shellreader, String[] pat){
		return 	getShell(shellreader, pat, null);
	}
	static public OSShell getShell(CallBackShellReaderListener shellreader){
		return 	getShell(shellreader, null, null);
	}
	static public OSShell getShell(){
		return 	getShell(null, null, null);
	}
	public boolean writeln(String string){
		try{
			// System.out.println(string);
			out.write(string);
			out.newLine();
			out.flush();
		}catch(Exception e){
			System.out.println(e);
		}
	return true;	
	}
	
	public boolean writePwln(String string){
		try{
			/*for (int i=0; i< string.length(); i++){
				System.out.print("*");
			}
			System.out.println(";-) was password ;-)!");
			*/
			out.write(string);
			out.newLine();
			out.flush();
		}catch(Exception e){
			System.out.println(e);
		}
	return true;	
	}
	
	public void stop(){
		cbsr.stop();
		writeln("^C");
		
	}
	
	public void exit(){
		cbsr.stop();
		writeln("exit");
		
	}
	public void sleep(long mils){
		try{
			Thread.sleep(mils);
		}catch(Exception e){
			System.out.println(e);
			stop();
			
		}
	}
	
	public static Osses getOS(){
		String myostype = new String(System.getProperty("os.name"));
		for ( Osses os: Osses.values())
			if (myostype.equals(os.ostype)){
				return os;
			}
				
		System.out.println("OS: "+ myostype +" unkown to OSShell.java, sorry!");
		System.out.println("Contact author: haeussler@flg-informatik.de");
		System.exit(1);		
		return null;
	}
	//<ullor getOSShell() without params only>
	public void errMatch(String string, int which) {
		
			System.out.println("errMatch("+which+"): " + string );
		
	}

	public void errReadln(String string) {
		System.out.print("ShellError: " + string);
	}

	public void inputMatch(String string, int which) {
		System.out.println("inputMatch("+which+"): " + string );
		
	}

	public void inputReadln(String string) {
		System.out.print("ShellOuput: " + string);
		
	}
	//</for getOSShell() without params only>
	protected void finalize(){
		stop();
	}
	
	
	public static void main(String[] str){
		OSShell s=getShell();
		s.writeln("mount ; echo Stop");
		
		
	}



	public boolean isReady() {
		// TODO Auto-generated method stub
		return true;
	}


}