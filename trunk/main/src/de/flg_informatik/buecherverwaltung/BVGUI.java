package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.io.File;
import java.sql.Connection;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import de.flg_informatik.utils.FLGFrame;
import de.flg_informatik.utils.FLGProperties;
import de.flg_informatik.utils.FireButton;

public class BVGUI extends FLGFrame {

	BVControl control;
	BVBooksDatamodell bvbv;
	FireButton cancel = new FireButton("Abbrechen");
	public BVGUI(BVControl control){
		this.control=control;
		this.setLayout(new BorderLayout());
		this.bvbv=new BVBooksDatamodell(control, control.connection);
		this.add(makeButtonfield(),BorderLayout.SOUTH);
		this.add(makeCardField(),BorderLayout.CENTER);;
		this.setExtendedState(MAXIMIZED_BOTH);
		this.pack();
		this.setVisible(true);
		this.validateTree();
			
		
		
	}
	private JTabbedPane makeCardField(){
		JTabbedPane retpan=new JTabbedPane();
		retpan.addTab("Buchtypen",makeBuchTypView());
		return retpan;
	}
	
	private JPanel makeBuchTypView(){
		BVBookTypView retpan;
		retpan=new BVBookTypView(control,control.connection);
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
	public static void main(String[] args) {
		BVControl bvc=new BVControl();
		
	}

}
