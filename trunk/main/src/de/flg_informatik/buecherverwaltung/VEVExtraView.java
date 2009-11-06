package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.buecherverwaltung.SelectedEvent.SelectedEventType;
import de.flg_informatik.utils.FLGProperties;
import de.flg_informatik.utils.FireButton;

public class VEVExtraView extends JPanel implements UCCase, ActionListener{
	JButton save = new JButton("Extras speichern");
	JPanel settings;//=new VPVPropertyView();
	
	enum extras{
		Programm_Einstellungen,
		Etikett_Einstellungen,
		Etikett_Druck,
		Buch_loeschen;
	}
	JPChooser extrachoose=new JPChooser(this,new Vector<String>(){{
		for (extras s:extras.values()){
			add(s.toString());
		}
	}},JPChooser.Orientation.VERTICAL);
	VEVExtraView(){
		setLayout(new BorderLayout());
		add(new JPanel(){{
			add(extrachoose);
			}},BorderLayout.WEST);
		
		
			
		
	}


	public void itemSelected(ListSelectionEvent e) {
		// TODO Auto-generated method stub

	}
	

	public void thingSelected(SelectedEvent e) {
		// TODO Auto-generated method stub

	}

	public void toBackground() {
		Control.getControl().app_settings_pane.focusLost(null);
		extrachoose.clearSelection();
	
		
	}

	public void toClose() {
		// TODO Auto-generated method stub
		
	}


	public Vector<SelectedEventType> getConsumedEvents() {
		
		return null;
	}


	public void toFront() {
		// TODO Auto-generated method stub
		
	}


	//@Override
	public void actionPerformed(ActionEvent e) {
		Control.getControl().app_settings_pane.focusLost(null);
		removeAll();
		add(new JPanel(){{
			add(extrachoose);
			}},BorderLayout.WEST);
		switch (Integer.valueOf(e.getActionCommand())){
			case 0: 
				add(new VPVPropertyView(),BorderLayout.CENTER);
				break;
			case 1: 
				add(new VEVJPSticker(),BorderLayout.CENTER);
				break;	
			case 2: 
				add(new VEVJPStickerPrint(),BorderLayout.CENTER);
				break;	
			case 3: 
				add(new VEVJPBookStickerDelete(),BorderLayout.CENTER);
				break;	
		
		}
		validate();
		//extrachoose.clearSelection();

	}
	
	
}
