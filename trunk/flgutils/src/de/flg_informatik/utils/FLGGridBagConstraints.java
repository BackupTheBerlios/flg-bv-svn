/*
 * FLGGridBagConstraints.java V1
 *
 * Created on 3. November 2006, 12:09
 */

package de.flg_informatik.utils;

/**
 * Erweitert java.awt.GridBagConstraints um einen Konstruktor
 *
 * @author  notkers
 *
 */
import java.awt.*;

public class FLGGridBagConstraints extends java.awt.GridBagConstraints implements Cloneable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/** Creates a new instance of FLGGridBagConstraints */
    /* Die ererbten Kontruktoren und Methoden
     * reichen wir durch!
     */
    public FLGGridBagConstraints() {
        super();
    }
    
    public FLGGridBagConstraints(int gridx,
                          int gridy,
                          int gridwidth,
                          int gridheight,
                          double weightx,
                          double weighty,
                          int anchor,
                          int fill,
                          Insets insets,
                          int ipadx,
                          int ipady){
        super(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, ipadx, ipady);
                              
    }
    
    /** Die neuen Konstruktoren:
     */
    
    
    public FLGGridBagConstraints(int gridx, int gridy,
                          int gridwidth, int gridheight) {
        super();
        this.gridx=gridx;
        this.gridy=gridy;
        this.gridwidth=gridwidth;
        this.gridheight=gridheight;
    }
    
    
    public Object clone(){
        return (super.clone());
    }
}
