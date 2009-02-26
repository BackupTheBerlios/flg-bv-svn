package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.flg_informatik.utils.FLGFrame;
import de.flg_informatik.utils.FireButton;

public class BVGUI extends FLGFrame {

	/**
	 * Hauptklasse, jeder usecase bekommt ein Panel im JTabbedPane
	 */
	private static final long serialVersionUID = 1L;
	BVControl control;
	//BVBooksDatamodell bvbv;
	FireButton cancel = new FireButton("Abbrechen");
	JTabbedPane centerpane;
	public BVGUI(BVControl control){
		this.control=control;
		this.setLayout(new BorderLayout());
		//this.bvbv=new BVBooksDatamodell(control, control.connection);
		this.add(makeButtonfield(),BorderLayout.SOUTH);
		this.add(makeCardField(),BorderLayout.CENTER);;
		this.setExtendedState(MAXIMIZED_BOTH);
		this.pack();
		this.setVisible(true);
		this.setInFront(1);
		this.validate();
			
		
		
	}
	
	private JTabbedPane makeCardField(){
		
		centerpane=new JTabbedPane();
		centerpane.addTab("StapelRückgabe",makeBookBackView(0)); // don't alter   
		centerpane.addTab("Buchtypen",makeBookTypView(1));
		return centerpane;
	}
	
	private BVBookTypView makeBookTypView(int index){
		BVBookTypView retpan;
		retpan=new BVBookTypView(control,index);
		return retpan;
	}
	
	private BVBookBack makeBookBackView(int index){
		BVBookBack retpan;
		retpan=new BVBookBack(control,index);
		return retpan;
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
				this.add((FireButton)(BVCEventObjects.cancel.ev));
				BVCEventObjects.cancel.register();
				this.add((FireButton)(BVCEventObjects.stop.ev));
				BVCEventObjects.stop.register();
				}});
		}});
		return retpan;
	}
	public void toClose(){
		((FireButton)(BVCEventObjects.stop.ev)).fire();
	}
	
	public void setInFront(int i){
		centerpane.setComponentZOrder(centerpane.getComponent(i), 1);
		
		centerpane.validate();
	}
	public static void main(String[] args) {
		BVControl bvc=new BVControl();
		
	}

}
