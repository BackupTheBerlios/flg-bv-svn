package de.flg_informatik.utils.shell;

import java.io.InputStreamReader;
import java.io.BufferedInputStream;
import java.util.regex.Pattern;

public class CallBackShellReader implements Runnable{
	Pattern[] patterns = {};
	Pattern[] errpatterns = {};
	Pattern linesep=Pattern.compile(".*"+System.getProperty("line.separator"));
	InputStreamReader inputstream;
	InputStreamReader errstream;
	Thread thread;
	StringBuffer buf = new StringBuffer();
	StringBuffer errbuf = new StringBuffer();
	CallBackShellReaderListener caller;
	boolean finished=false;
	volatile boolean canchange=true;
	int patanz=0;
	int erranz=0;
	boolean stop=false;
	public CallBackShellReader(String[] patternstrings, String[] errstrings, CallBackShellReaderListener caller){
		this(caller, (java.lang.Process)null, patternstrings, errstrings);
	}
	public CallBackShellReader(String[] patternstrings, CallBackShellReaderListener caller){
		this(caller, (java.lang.Process)null, patternstrings, null);
	}
	public CallBackShellReader(CallBackShellReaderListener caller){
		this(caller, (java.lang.Process)null, null, null);
	}
	public CallBackShellReader(CallBackShellReaderListener caller, java.lang.Process pcs){
		this(caller, pcs, null, null);
	}
	public CallBackShellReader(CallBackShellReaderListener caller, java.lang.Process pcs, String[] patternstrings){
		this(caller, pcs, patternstrings, null);
	}
	public CallBackShellReader(CallBackShellReaderListener caller, java.lang.Process pcs, String[] patternstrings, String[] errstrings){
		this.caller=caller;
		if (pcs != null){
			this.inputstream= new InputStreamReader(new BufferedInputStream(pcs.getInputStream()));
			this.errstream=new InputStreamReader(new BufferedInputStream(pcs.getErrorStream()));
		}
		if (patternstrings != null){
			this.setInputPattern(patternstrings);
		}
		if (errstrings != null){
			this.setErrorPattern(errstrings);
		}
		/*int i=0;
		if (patternstrings == null){
			patanz = 0;
		}else{
			patanz = patternstrings.length;
		}
		if (errstrings == null){
			erranz = 0;
		}else{
			erranz = errstrings.length;
		}
		// System.out.println(patanz+" "+erranz);
		patterns = new Pattern[patanz];
		while(i < patanz){
			patterns[i]=Pattern.compile(patternstrings[i]);
			i++;
		}
		i=0;
		errpatterns = new Pattern[erranz];
		while(i < erranz){
			errpatterns[i]=Pattern.compile(errstrings[i]);
			i++;
		}
		*/
		
		/* For Debugging
			for (int j=0;j<patanz;j++){
				System.out.println("<"+patterns[j]+">");
			}
			
			for (int j=0;j<erranz;j++){
				System.out.println("<"+errpatterns[j]+">");
			}
		//*/
		if (pcs!=null){
			this.thread = new Thread(this);
			this.thread.start();
		}
		
		
		
		
	}
	public void errPatternAppend(String[] pattern){
		patternAppend(pattern,false,true);
	}
	public void inputPatternAppend(String[] pattern){
		patternAppend(pattern,true,false);
	}
	public int setErrorPattern(String[] pattern){
		patternAppend(pattern,false,true,true);
		return (erranz-1);
	}
	public int setInputPattern(String[] pattern){
		patternAppend(pattern,true,false,true);
		return (patanz-1);
	}
	public int setErrorPattern(String pattern){
		patternAppend(new String[]{pattern},false,true,true);
		return (erranz-1);
	}
	public int setInputPattern(String pattern){
		patternAppend(new String[]{pattern},true,false,true);
		return (patanz-1);
	}	
	public int errPatternAppend(String pattern){
		patternAppend(new String[]{pattern},false,true);
		return (erranz-1);
	}
	public int inputPatternAppend(String pattern){
		patternAppend(new String[]{pattern},true,false);
		return (patanz-1);
	}
	
	public int patternAppend(String[] pattern, boolean isinput, boolean iserror){
		return patternAppend(pattern, isinput, iserror, false);
	}
	
	private void patternSynchronize(){
		canchange = true;
		/*try{
			Thread.sleep(10);
		}catch(InterruptedException e){
			
		}
		//*/
		patternAppend(null, false, false, false);
		
	}
		
	public synchronized int patternAppend(String[] pattern, boolean isinput, boolean iserror, boolean delete){
		int i=0;
		if (!isinput && !iserror && !delete){
			// Synchronisations-Aufruf aus CallBackShellReader.Thread
			canchange=false; // Änderungen verriegeln
			return -1;
		}
		while(!canchange){ // Warten auf Synchronisation
			}
		if (isinput){
			if (delete){
				patanz=0;
			}
			Pattern[] neu = new Pattern[patanz+pattern.length] ;
			i=0;
			while(i< patanz){
				neu[i]=patterns[i];
				i++;
			}
			while(i<patanz+pattern.length){
				neu[i]=Pattern.compile(pattern[i-patanz]);
				i++;
			}
			patanz=patanz+pattern.length;
			patterns=neu;
			/* For Debugging
			for (int j=0;j<patanz;j++){
				System.out.println("<"+patterns[j]+">");
			}
			//*/
			
			// return Anweisung s.u. damit Aufruf mit ..., true, true, ... klappt;
		}
		if (iserror){
			if (delete){
				erranz=0;
			}
			Pattern[] neu = new Pattern[erranz+pattern.length];
			i=0;
			while(i< erranz){
				neu[i]=errpatterns[i];
				i++;
			}
			while(i<erranz+pattern.length){
				neu[i]=Pattern.compile(pattern[i-erranz]);
				i++;
			}
			erranz=erranz+pattern.length;
			errpatterns=neu;
			return erranz;
			/* For Debugging
			for (int j=0;j<erranz;j++){
				System.out.println("<"+errpatterns[j]+">");
			}
			*/
		}else{
			return patanz;
		}
		
		
		
		
	}
	
	public void run(){
			int chr;
			int i;
			canchange=false;
			try{
			  while(!stop){
				 if (inputstream.ready()){
					chr=inputstream.read();
					if (chr > -1){
						buf.append((char)chr);
						// System.out.print((char)(chr));
						i=0;
						while(i<patanz){
							if (patterns[i].matcher(buf).matches()){
								inputMatch(new String(buf),i);
								buf = new StringBuffer();
								i=patanz-1;
							}
							i++;
						}
						if (linesep.matcher(buf).matches()){
							inputReadln(new String(buf));
							buf = new StringBuffer();
						}
					}else{System.out.println("!");}
					
				}
			if (errstream.ready()){
				chr=errstream.read();
			if (chr > -1){
				errbuf.append((char)chr);
				// System.out.print((char)chr);
				i=0;
				while(i<erranz){
					if (errpatterns[i].matcher(errbuf).matches()){
						errMatch(new String(errbuf),i);
						errbuf = new StringBuffer();
						i=erranz-1;
					}	
					i++;
				}
				if (linesep.matcher(errbuf).matches()){	
						errReadln(new String(errbuf));
						errbuf = new StringBuffer();
				}
				
				
			}
			}
		patternSynchronize();
		}
	}catch(Exception e){
			System.out.println(e);
	}finally{
		try{
			inputstream.close();
			errstream.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}
		
		
		
		
	}
	synchronized void inputMatch(String string, int which){
		caller.inputMatch(string, which);
	}
	synchronized void inputReadln(String string){
		caller.inputReadln(string);
	}
	synchronized void errMatch(String string, int which){
		caller.errMatch(string, which);
	}
	synchronized void errReadln(String string){
		caller.errReadln(string);
	}
	
	public int start(java.lang.Process pcs){
		this.inputstream= new InputStreamReader(new BufferedInputStream(pcs.getInputStream()));
		this.errstream=new InputStreamReader(new BufferedInputStream(pcs.getErrorStream()));
		(this.thread = new Thread(this)).start();
		try{
			int ret=pcs.waitFor();
			finished=true;
			return ret;
		}catch(InterruptedException e){
			caller.errReadln("InterruptedException: "+e+" in "+this);
			return -3;
		}
	}
	
	public boolean isFinished(){
		return finished;
	}
	
	public void stop(){
		stop = true;
		
	}

}
