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
	private static boolean debug=false;
	public FireButton cancelbutton = new FireButton("Abbrechen");
	public FireButton closebutton = new FireButton("Beenden");
	BVControl control;
	JTabbedPane centerpane;
	public BVGUI(BVControl control){
		new BVD(debug,"setting Variables");
		this.setVisible(false);
		thegui=this;
		this.control=control;
		new BVD(debug,"configuring FLGFrame");
		this.setQuitsystem(false);
		this.setClosewindow(false);
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
		new BVD(debug,"isSelectedView() from: "+view.getName());
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
			new BVD(debug,"centerpane: "+ usecase.view.getName());
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
