package de.flg_informatik.buecherverwaltung;


public class Deb extends Throwable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String who="";
	private static boolean wasasterix=true; 
	private static final int debug=1; // should be >= 1
	public Deb(Object o){
		this(debug,o);
	}

	/*public Deb(boolean isdebug, Object o){
		if (isdebug){
			printOut(debug, o);
		}
	}
	*/
	public Deb(int debuglevel, Object o){
		printOut(debuglevel, o);
		
	}
	private void printOut(int debuglevel, Object o){
		if (debuglevel>0){
			if (debuglevel>1 & !wasasterix){
				System.out.println("                ***************");
			}
			if (o!=null){
				who=o.toString()+" @ ";
			}else{
				who="null-Object @ ";
			}
			// System.out.print(getStackTrace()[0].getFileName()+"("+getStackTrace()[0].getLineNumber()+"): "+text);
			for (int i=0; i< Math.min(this.getStackTrace().length, debuglevel);i++){
				who = who+this.getStackTrace()[i].toString()+"\n";
			}
			System.out.print(who);
			if (debuglevel>1){
				System.out.println("                ***************");
				wasasterix=true;
			}else{
				wasasterix=false;
			}
		}	

	}
}		

	


