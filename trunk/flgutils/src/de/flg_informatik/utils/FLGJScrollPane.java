package de.flg_informatik.utils;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class FLGJScrollPane extends JScrollPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int insetsvert=100;
	private int insetshoriz=100;
	public FLGJScrollPane(JComponent comp){
		super(comp,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.comp=comp;
	}
	private static final int adjusthoriz=4; // has to be advanced a bit
	private static final int adjustvert=4; // has to be advanced a bit
	private JComponent comp;
	void makeInsets(JComponent comp){
		if (comp.getParent()!=null & JComponent.class.isAssignableFrom(comp.getParent().getClass())){
			makeInsets((JComponent)comp.getParent());
		}
		insetsvert+=comp.getInsets().bottom+comp.getInsets().top;
		insetshoriz+=comp.getInsets().left+comp.getInsets().right;
		
	}
	public Dimension getPreferredSize() {
		java.awt.GraphicsConfiguration gc=this.getGraphicsConfiguration();
		makeInsets(this);
		return new Dimension(Math.min(comp.getPreferredSize().width+adjusthoriz,gc.getBounds().width-insetshoriz),Math.min(comp.getPreferredSize().height+adjustvert,gc.getBounds().height+15-insetsvert));
		}
	public int getInsetsvert() {
		return insetsvert;
	}
	public void setInsetsvert(int insetsvert) {
		this.insetsvert = insetsvert;
	}
	public int getInsetshoriz() {
		return insetshoriz;
	}
	public void setInsetshoriz(int insetshoriz) {
		this.insetshoriz = insetshoriz;
	}

}
