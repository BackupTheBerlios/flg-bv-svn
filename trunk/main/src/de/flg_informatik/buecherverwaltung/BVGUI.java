package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import de.flg_informatik.utils.FLGFrame;
import de.flg_informatik.utils.FireButton;

public class BVGUI extends JFrame {

	/*
	 * Hauptklasse, jeder usecase bekommt ein Panel im JTabbedPane
	 * definiert in BVUsecases
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private static BVGUI thegui;
	private static boolean debug=true;
	// public static Hashtable<BVView, BVUsecases> usecases =new Hashtable<BVView, BVUsecases>();
	public JButton cancelbutton = new JButton("Abbrechen");
	public JButton closebutton = new JButton("Beenden");
	BVControl control;
	JTabbedPane centerpane;
	LogPane lp;
	public BVGUI(BVControl control){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new BVD(debug,"setting Variables");
		this.setVisible(false);
		thegui=this;
		this.control=control;
		new BVD(debug,"configuring FLGFrame");
		//this.setQuitsystem(false);
		//this.setClosewindow(false);
		new BVD(debug,"setting Layout");
		this.setLayout(new BorderLayout());
		new BVD(debug,"making Buttonfield");
		this.add(makeButtonfield(),BorderLayout.SOUTH);
		new BVD(debug,"making CardField");
		this.add(makeCardField(),BorderLayout.CENTER);
		new BVD(debug,"finishing");
		this.setExtendedState(MAXIMIZED_BOTH);
		this.pack();
		this.setVisible(true);
		new BVD(debug,"finished");
		
		
	}
	
	public static boolean isSelectedView(BVView view){
		if (view==null){
			return false;
		}
		new BVD(debug,"isSelectedView() from: "+((JPanel)view).getName());
		if(thegui.centerpane.getSelectedComponent()==null){
			return false;
		}else{
			return ((JPanel)view).getName().equals(thegui.centerpane.getSelectedComponent().getName());
		}
		
	}

	public static BVView getSelectedView(){
		if(thegui.centerpane.getSelectedComponent()==null){
			return null;
		}else{
			return (BVView) thegui.centerpane.getSelectedComponent();
		}
		
	}
	
	private JTabbedPane makeCardField(){
		centerpane=new JTabbedPane();
		for (BVUsecases usecase:BVUsecases.values()){
			new BVD(debug,"centerpane: "+ usecase.view.getName());
			centerpane.addTab(((JPanel)(usecase.view)).getName(),((JPanel)(usecase.view)));
			BVSelectedEvent.addBVSelectedEventListener(usecase.view);
		}
		centerpane.addChangeListener(BVControl.getControl());
		
		
		
		
		
		return centerpane;
	}
	
	
	
	
	private JPanel makeButtonfield(){
		JPanel retpan=new JPanel();
		retpan.add(lp=new LogPane());
		retpan.add(new JPanel(new GridLayout(0,1)){
			{
				add(new JLabel("Buchverwaltung Version: "+control.version));
				add(new JLabel("(C) C.HOFF&NURH (@flg-informatik.de)"));
				add(new JPanel(new FlowLayout()){{
				this.add(cancelbutton);
				cancelbutton.addActionListener(control);
				this.add(closebutton);
				closebutton.addActionListener(control);
				
			}});
		}});
		invalidate();
		return retpan;
	}
	class LogPane extends java.awt.TextArea{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
			
		
		LogPane(){
			this.setEditable(false);
		}
		public java.awt.Dimension getPreferredSize(){
			if (BVControl.getControl().gui==null){
				return(new java.awt.Dimension(600,100));
			}
			return (new java.awt.Dimension(BVControl.getControl().gui.getSize().width/3*2,BVControl.getControl().gui.getSize().height/4));
			
		}
		public java.awt.Dimension getMinimumSize(){
			
				return(new java.awt.Dimension(600,100));
			
		}
		synchronized public void append(String string){
			super.append(string);
			invalidate();
			getParent().validate();
			
			
		}
	}

	public void toClose(){
		closebutton.doClick();
	}
	
	public static void selectView(BVUsecases usecase){
		new BVD(debug,"selectView: "+ usecase);
		thegui.centerpane.setSelectedIndex(usecase.ordinal());
		thegui.centerpane.invalidate();
		thegui.validate();
	}
	public static void main(String[] args) {
		BVControl bvc=new BVControl();
		
	}
	
	public synchronized void validate(){
		super.validate();
		
	}
	public static void val(){
		thegui.validate();
		
	}
}
