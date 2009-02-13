package de.flg_informatik.utils.shell;

import java.util.concurrent.TimeoutException;

public class OSCommand implements CallBackShellReaderListener, Runnable {
	static CallBackShellReader cbsr=null;
	static boolean timedout;
	boolean debug;
	int matchedstring=0;
	int timeslice = 200;
	long timetowait;
	String[] inputmatches;
	String[] errormatches;
	String[] command; 
	java.io.File pfad;
	Thread thread;
	static int ret;
	static final long timeoutdefault=5000;
	
	
	
	public static  int execute(String[] command, long timeout) throws TimeoutException{
		int ret = executeit(command, (java.io.File)null, null, null, true, timeout);
		if (timedout) throwException(command,timeout);
		return ret;
	}
	public static  int execute(String[] command){
		return executeit(command, (java.io.File)null, null, null, true, timeoutdefault);
	}
	
	public static  int execute(String[] command, String[] inputmatches,long timeout) throws TimeoutException{ 
		int ret = executeit(command, (java.io.File)null, inputmatches, null, true, timeout);
		if (timedout) throwException(command,timeout);
		return ret;
	}
	public static  int execute(String[] command, String[] inputmatches){ 
		return executeit(command, (java.io.File)null, inputmatches, null, true, timeoutdefault);
	}

	public static int execute(String[] command, String[] inputmatches, String[] errormatches, long timeout) throws TimeoutException{
		int ret = executeit(command, (java.io.File)null, inputmatches, errormatches, true, timeout);
		if (timedout) throwException(command,timeout);
		return ret;
	}
	public static int execute(String[] command, String[] inputmatches, String[] errormatches){
		return executeit(command, (java.io.File)null, inputmatches, errormatches, true, timeoutdefault);
	}
	
	public static  int execute(String[] command, java.io.File pfad, long timeout) throws TimeoutException{
		int ret = executeit(command, pfad, null, null, true, timeout);
		if (timedout) throwException(command,timeout);
		return ret;
	}
	public static  int execute(String[] command, java.io.File pfad){
		return executeit(command, pfad, null, null, true, timeoutdefault);
	}
	
	public static  int execute(String[] command, java.io.File pfad, String[] inputmatches, long timeout) throws TimeoutException{
		int ret = executeit(command, pfad, inputmatches, null, true, timeout);
		if (timedout) throwException(command,timeout);
		return ret;
	}
	public static  int execute(String[] command, java.io.File pfad, String[] inputmatches){ 
		return executeit(command, pfad, inputmatches, null, true, timeoutdefault);
	}
	
	public static int execute(String[] command, java.io.File pfad, String[] inputmatches, String[] errormatches, long timeout) throws TimeoutException{
		int ret = executeit(command, pfad, inputmatches, errormatches, true, timeout);	
	if (timedout) throwException(command,timeout);
	return ret;		
	}
	public static int execute(String[] command, java.io.File pfad, String[] inputmatches, String[] errormatches){
		return executeit(command, pfad, inputmatches, errormatches, true, timeoutdefault);			
	}
	
	public static  int execute(String[] command, boolean debug, long timeout) throws TimeoutException{
		int ret = executeit(command, (java.io.File)null, null, null, debug, timeout);
		if (timedout) throwException(command,timeout);
		return ret;	
	}
	public static  int execute(String[] command, boolean debug){
		return executeit(command, (java.io.File)null, null, null, debug, timeoutdefault);
	}
	
	public static  int execute(String[] command, String[] inputmatches, boolean debug, long timeout) throws TimeoutException{ 
		int ret = executeit(command, (java.io.File)null, inputmatches, null, debug, timeout);
		if (timedout) throwException(command,timeout);
		return ret;	
	}
	public static  int execute(String[] command, String[] inputmatches, boolean debug){ 
		return executeit(command, (java.io.File)null, inputmatches, null, debug, timeoutdefault);
	}
	
	public static int execute(String[] command, String[] inputmatches, String[] errormatches, boolean debug, long timeout) throws TimeoutException{
		int ret = executeit(command, (java.io.File)null, inputmatches, errormatches, debug, timeout);
		if (timedout) throwException(command,timeout);
		return ret;	
	}
	public static int execute(String[] command, String[] inputmatches, String[] errormatches, boolean debug){
		return executeit(command, (java.io.File)null, inputmatches, errormatches, debug, timeoutdefault);
	}
	
	public static  int execute(String[] command, java.io.File pfad, boolean debug, long timeout) throws TimeoutException{
		int ret = executeit(command, pfad, null, null, debug, timeout);
		if (timedout) throwException(command,timeout);
		return ret;	
	}
	public static  int execute(String[] command, java.io.File pfad, boolean debug){
		return executeit(command, pfad, null, null, debug, timeoutdefault);
	}
	
	public static  int execute(String[] command, java.io.File pfad, String[] inputmatches, boolean debug, long timeout) throws TimeoutException{
		int ret = executeit(command, pfad, inputmatches, null, debug, timeout);
		if (timedout) throwException(command,timeout);
		return ret;	
	}
	public static  int execute(String[] command, java.io.File pfad, String[] inputmatches, boolean debug){ 
		return executeit(command, pfad, inputmatches, null, debug, timeoutdefault);
	}

	public static int execute(String[] command, java.io.File pfad, String[] inputmatches, String[] errormatches, boolean debug, long timeout) throws TimeoutException{
		int ret = executeit(command, pfad, inputmatches, errormatches, debug, timeout);
		if (timedout) throwException(command,timeout);
		return ret;
	}
	public static int execute(String[] command, java.io.File pfad, String[] inputmatches, String[] errormatches, boolean debug){
		return executeit(command, pfad, inputmatches, errormatches, debug, timeoutdefault);
	}
	
	
	public static synchronized int executeit(String[] command, java.io.File pfad, String[] inputmatches, String[] errormatches, boolean debug, long timeout){
		ret =-1;
		timedout=false;
		OSCommand osc=new OSCommand();
		osc.debug = debug;
		osc.timetowait = timeout;
		osc.timeslice=Math.min(osc.timeslice, (int)(timeout/25+1));
		osc.inputmatches=inputmatches;
		osc.errormatches=errormatches;
		osc.command=command;
		osc.pfad=pfad;
		
		(osc.thread = new Thread(osc)).start();
		
		while(osc.timetowait>0){
			new de.flg_informatik.utils.Pause(osc.timeslice);
			osc.timetowait-=osc.timeslice;
			if (cbsr.isFinished()) break;
		}
	
		cbsr.stop();
		
		if (!(osc.timetowait>0)){
			timedout=true;
			osc.thread.interrupt();
		}
		if (debug){
			System.out.println("OSC: return ="+ ret );
		}
		return ret+osc.matchedstring;
	}

	public void run(){
		try{
			
			if (command.length > 1) {
				cbsr = new CallBackShellReader(inputmatches, errormatches,this);
				ret=cbsr.start(Runtime.getRuntime().exec(command, null, pfad));
			}else{
				cbsr = new CallBackShellReader( inputmatches, errormatches, this);		
				ret=cbsr.start(Runtime.getRuntime().exec(command[0], null, pfad));
			}
		
			
		}catch(Exception e){
					System.out.println(e);
		}
		
	}

	public synchronized void errMatch(String string, int which) {
		matchedstring=(which+1)*256*256;
		if (debug){
			System.out.println(this+ ": errMatch(String string ="+ string +" , int which()="+which+ " -> return="+ matchedstring);
		}
	}

	public synchronized void errReadln(String string) {
		if (debug){
			System.out.println(this+ ": errReadln(String string = "+ string + " )");
		}
	}

	public synchronized void inputMatch(String string, int which) {
		matchedstring=(which+1)*256;
		if (debug){
			System.out.println(this+ ": inputMatch(String string ="+ string +" , int which()="+which+ " -> return="+ matchedstring);
		}
		
		
	}
	
	public synchronized void inputReadln(String string) {
		if (debug){
			System.out.println(this+ ": inputReadln(String string = " + string + " )");
		}
	}

	public boolean isReady() {
		return true;
	}
	
	private static void throwException(String[] command, long timeout) throws TimeoutException{
		StringBuffer string= new StringBuffer("OSCommand: ");
		for (int i=0; i < command.length;i++){
			string.append(command[i]);
		}
		string.append(", Timeout(ms): ");
		string.append(timeout);
		throw new TimeoutException(new String(string));
	}
	public static void main(String[] args){
		// testing
		try{
			OSCommand.execute(new String[]{"sleep 10"} , false, 5000);
		}catch(TimeoutException e){
			System.out.println(e);
		}
		//System.out.println(OSCommand.execute(new String[] {"net", "use", "H:"}, new java.io.File("C:\\"),5000));
		
	}

}
