/**
 * 
 */
package de.flg_informatik.paedmlovpn;

class LogPane extends java.awt.TextArea{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	private final StartVPN startvpn;
	LogPane(StartVPN startvpn){
		this.startvpn = startvpn;
		this.setEditable(false);
	}
	public java.awt.Dimension getPreferredSize(){
		return (new java.awt.Dimension(500,100));
		
	}
	synchronized public void append(String string){
		super.append(string);
		this.startvpn.repaint();
		
		
	}
}

