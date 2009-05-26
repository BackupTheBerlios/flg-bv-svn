package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.flg_informatik.utils.FLGFrame;
import de.flg_informatik.utils.FireButton;

public class BVGUI extends FLGFrame {

	/*
	 * Hauptklasse, jeder usecase bekommt ein Panel im JTabbedPane
	 * definiert in BVUsecases
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private static BVGUI thegui;
	public FireButton cancelbutton = new FireButton("Abbrechen");
	public FireButton closebutton = new FireButton("Beenden");
	BVControl control;
	JTabbedPane centerpane;
	public BVGUI(BVControl control){
		debug("setting Variables");
		this.setVisible(false);
		thegui=this;
		this.control=control;
		debug("configuring FLGFrame");
		this.setQuitsystem(false);
		this.setClosewindow(false);
		debug("setting Layout");
		this.setLayout(new BorderLayout());
		debug("making Buttonfield");
		this.add(makeButtonfield(),BorderLayout.SOUTH);
		debug("making CardField");
		this.add(makeCardField(),BorderLayout.CENTER);
		debug("finishing");
		this.setExtendedState(MAXIMIZED_BOTH);
		this.pack();
		this.setVisible(true);
		debug("finished");
		
		
	}
	
	public static boolean isSelectedView(BVView view){
		if (view==null){
			return false;
		}
		debug("isSelectedView() from: "+view.getName());
		if(thegui.centerpane.getSelectedComponent()==null){
			return false;
		}else{
			return (view.getName().equals(thegui.centerpane.getSelectedComponent().getName()));
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
			debug("centerpane: "+ usecase.view.getName());
			centerpane.addTab(usecase.view.getName(),usecase.view);
		}
		centerpane.addChangeListener(BVControl.getControl());
		
		
		
		
		return centerpane;
	}
	
	
	
	
	private JPanel makeButtonfield(){
		JPanel retpan=new JPanel(new GridLayout(1,0));
		retpan.add(new JPanel(){
			{add(new JLabel("Buchverwaltung Version: "+control.version+", (C) C.HOFF&NURH (@flg-informatik.de)"));
			}});
			
		retpan.add(new JPanel(new FlowLayout()){{
			add(new JPanel());
			add(new JPanel());
			add(new JPanel());
			add(new JPanel(new FlowLayout()){{
				this.add(cancelbutton);
				cancelbutton.addActionListener(control);
				this.add(closebutton);
				closebutton.addActionListener(control);
			}});
		}});
		return retpan;
	}
	public void toClose(){
		closebutton.fire();
	}
	
	public static void selectView(BVUsecases usecase){
		debug("selectView: "+ usecase);
		thegui.centerpane.setSelectedIndex(usecase.ordinal());
		thegui.centerpane.invalidate();
		thegui.validate();
	}
	public static void main(String[] args) {
		BVControl bvc=new BVControl();
		
	}
	static private void debug(Object obj){
		// System.out.println(BVGUI.class+": "+ obj);
	}
	public synchronized void validate(){
		super.validate();
		
	}
	public static void val(){
		thegui.validate();
		
	}
}
