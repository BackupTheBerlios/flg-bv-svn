package de.flg_informatik.utils.shell;

import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.util.regex.Pattern;


public class CallBackInputStreamReader extends InputStreamReader implements Runnable{
	
	
	Pattern[] patterns = {};
	static String lbr=System.getProperty("line.separator");
	static Pattern linesep=Pattern.compile(".*"+lbr);
	InputStreamReader inputstream;
	Thread thread;
	StringBuffer buf = new StringBuffer();
	CallBackInputStreamReaderListener caller;
	volatile boolean canchange=true;
	int patanz=0;
	
	boolean stop=false;

	public CallBackInputStreamReader(CallBackInputStreamReaderListener caller, InputStream inputstream){
		this(caller, inputstream, null);
	}
	public CallBackInputStreamReader(CallBackInputStreamReaderListener caller, InputStream inputstream, String[] patternstrings){
		super(new BufferedInputStream(inputstream));
		this.caller=caller;
		if (patternstrings != null){
			this.setInputPattern(patternstrings);
		}
		
		
		/* For Debugging
			for (int j=0;j<patanz;j++){
				System.out.println("<"+patterns[j]+">");
			}
		
		//*/
			
		
		this.thread = new Thread(this);
		this.thread.start();
		
		
	}
	
	public void inputPatternAppend(String[] pattern){
		patternAppend(pattern,false,false);
	}
	
	public int setInputPattern(String[] pattern){
		patternAppend(pattern,true,false);
		return (patanz-1);
	}
	
	public int setInputPattern(String pattern){
		patternAppend(new String[]{pattern},true,false);
		return (patanz-1);
	}	
	
	public int inputPatternAppend(String pattern){
		patternAppend(new String[]{pattern},false,false);
		return (patanz-1);
	}
	
	
	
	private void patternSynchronize(){
		canchange = true;
		/*try{
			Thread.sleep(10);
		}catch(InterruptedException e){
			
		}
		//*/
		patternAppend(null, false, true);
		
	}
		
	public synchronized int patternAppend(String[] pattern, boolean delete, boolean sync){
		int i=0;
		if (!delete && sync){
			// Synchronisations-Aufruf aus CallBackShellReader.Thread
			canchange=false; // Änderungen verriegeln
			return -1;
		}
		while(!canchange){ // Warten auf Synchronisation
			}
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
		return patanz;
	}
		
		
		
		
	
	
	public void run(){
			int chr;
			int i;
			canchange=false;
			// wait for caller to come up
			while(!caller.isReady()){
				
			}
			
		while(!stop){
			
			 try{
				
				 if (this.ready()){
					chr=this.read();
									
					
				if (chr > -1){
					buf.append((char)chr);
					// System.out.print((char)(chr));
					i=0;
					while((i<patanz)&!stop){
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
				}
				}
			}catch(Exception e){
				System.out.println(e);
			}
			
			
			
		
			
		patternSynchronize();
		
		}
		try{
			
			
			this.close();
		}catch(Exception e){
			System.out.println(e);
		}
		
		
		
		
		
	}
	public void stop(){
		stop = true;
	
	}
	synchronized void inputMatch(String string, int which){
		caller.inputMatch(string, which);
	}
	synchronized void inputReadln(String string){
		caller.inputReadln(string);
	}

}



