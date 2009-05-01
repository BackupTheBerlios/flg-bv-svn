package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

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
		thegui=this;
		this.setQuitsystem(false);
		this.setClosewindow(false);
		this.control=control;
		this.setLayout(new BorderLayout());
		//this.bvbv=new BVBooksDatamodell(control, control.connection);
		this.add(makeButtonfield(),BorderLayout.SOUTH);
		this.add(makeCardField(),BorderLayout.CENTER);;
		this.setExtendedState(MAXIMIZED_BOTH);
		this.pack();
		this.setVisible(true);
		selectView(BVUsecases.Buchtypen); // default usecase
		this.validate();
	}
	
	public static boolean isSelectedView(BVView view){
		debug("isSelectedView() from: "+view.getName());
		debug("Selected is: "+ thegui.centerpane.getSelectedComponent().getName());
		debug(view.getName().equals(thegui.centerpane.getSelectedComponent().getName()));
		return (view.getName().equals(thegui.centerpane.getSelectedComponent().getName()));
	}
	
	
	private JTabbedPane makeCardField(){
		
		centerpane=new JTabbedPane();
		for (BVUsecases usecase:BVUsecases.values()){
			centerpane.addTab(usecase.view.getName(),usecase.view);
		}
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
		thegui.centerpane.doLayout();
		thegui.centerpane.validate();
	}
	public static void main(String[] args) {
		BVControl bvc=new BVControl();
		
	}
	static private void debug(Object obj){
		System.out.println(BVGUI.class+": "+ obj);
	}

}
