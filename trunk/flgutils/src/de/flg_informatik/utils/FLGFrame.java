/*
 * FLGFrame.java V1.2
 * Changes 2008/03/18
 * Created on 3. November 2006, 13:31
 * migrated to jdk1.6 17. September 2007
 * 
 */

package de.flg_informatik.utils;


/**
 * FLGFrame extends java.awt.Frame
 *
 * V2.0 (tested 20081025) redesign
 *
 * Stellt alle Konstruktoren für Frame zur Verfügung,
 * erweitert durch einen "WindowClosingAdapter",
 *
 * Wahlweise mit zusätzlichem boolean Argument quitsystem (default: true)
 * das beim Schließen des Fensters die Anwendung beendet.
 *
 * Zusätzliche Methoden
 * void setQuitSystem(boolean), boolean isQuitSystem(),
 * void setCloseWindow(boolean), boolean isCloseWindow(),
 * callback Methode boolean toClose()
 *
 * QuitSystem ermöglicht das Ändern/Lesen der Property quitsystem s.u. 
 * zur Laufzeit 
 * CloseWindow ermöglciht die Einstellung der Property closewindow,
 * die für quitsystem==false reguliert ob das Fenster geschlossen wird oder 
 * beim Klicken auf schließen nur die callback Methode toClose() aufgerufen wird.
 *  @author  notkers
 */
public class FLGFrame extends java.awt.Frame {
	static final long serialVersionUID=20080319;
    java.awt.GraphicsConfiguration gc;
    private boolean quitsystem;
    private boolean closewindow=true;
    /** Creates a new instance of FLGFrame */
 public FLGFrame() {
        this(true);
 }
      
    
 public FLGFrame(String title) {
        this(title, true);
 }
    
 public FLGFrame(String title, boolean quitsystem) {
        this(quitsystem);
        this.setTitle(title);
 }

 public FLGFrame(java.awt.GraphicsConfiguration gc) {
        this(gc,true);
 }
      
    
 public FLGFrame(String title, java.awt.GraphicsConfiguration gc) {
        this(gc,true);
        this.setTitle(title);
 
 }
    
 public FLGFrame(String title, java.awt.GraphicsConfiguration gc, boolean quitsystem) {
        this(gc,quitsystem);
        this.setTitle(title);
 }

 public FLGFrame(boolean quitsystem) {
         super();
         this.setVisible(true);
         this.quitsystem=quitsystem;
         myWindowListener();
 }
 
 public FLGFrame(java.awt.GraphicsConfiguration gc,boolean quitsystem) {
         super(gc);
         this.setVisible(true);
         this.quitsystem=quitsystem;
         myWindowListener();
 }
 
 /** Method called as a hook when WindowListener.windowClosing( ... ) is called
  * Do overwrite to customize closing beheavior
  */ 
 protected void toClose(){
	
 }
 
 
	protected boolean isQuitsystem() {
		return quitsystem;
	}
	
	
	protected void setQuitsystem(boolean quitsystem) {
		this.quitsystem = quitsystem;
	}
	
	
	protected boolean isClosewindow() {
		return closewindow;
	}
	
	
	protected void setClosewindow(boolean closewindow) {
		this.closewindow = closewindow;
	}


private void myWindowListener(){
	 	final FLGFrame me=this;
        me.addWindowListener(new java.awt.event.WindowAdapter(){
                public void windowClosing(java.awt.event.WindowEvent e){
                	me.toClose();
                	if (me.closewindow){
                		e.getWindow().setVisible(false);
                		e.getWindow().dispose();
                	}
                    if (me.quitsystem) {
                    	System.exit(0);
                    }
                }
         });
         
         
    }
 
 
 // Testprogramm
 /** public static void main(String[] args){
     FLGFrame[] flgframe = new FLGFrame[15];
     flgframe[0]=new FLGFrame();
     flgframe[0].setTitle("FLGFrame()");
 
     flgframe[1]=new FLGFrame(true);
     flgframe[1].setTitle("FLGFrame(true)");
     
     flgframe[2]=new FLGFrame("FLGFrame(Titel)");
     
     flgframe[3]=new FLGFrame("FLGFrame(Titel,true)",true);
     
     flgframe[4]=new FLGFrame(false);
     flgframe[4].setTitle("FLGFrame(false)");
     
     flgframe[5]=new FLGFrame("FLGFrame(Titel,false)",false);
     
     java.awt.GraphicsConfiguration gc=flgframe[0].getGraphicsConfiguration();
 
     flgframe[6]=new FLGFrame(gc);
     flgframe[6].setTitle("FLGFrame(gc)");
    
     flgframe[7]=new FLGFrame(gc,true);
     flgframe[7].setTitle("gc,FLGFrame(true)");
     
     flgframe[8]=new FLGFrame("FLGFrame(Titel,gc)",gc);
     
     flgframe[9]=new FLGFrame("FLGFrame(Titel,true)",true);
     
     flgframe[10]=new FLGFrame(gc,false);
     flgframe[10].setTitle("FLGFrame(gc,false)");
     
     flgframe[11]=new FLGFrame("FLGFrame(Titel, gc, false)",gc,false);
     for(int i=0;(i<=11);i++){
         flgframe[i].setBounds(20*i,20*i,20*i+800,20*i+100);
         flgframe[i].setVisible(true);
     }
 
 }
 */
}