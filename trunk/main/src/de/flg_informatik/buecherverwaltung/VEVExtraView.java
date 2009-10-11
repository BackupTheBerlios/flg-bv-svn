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
	JPanel settings=new VPVPropertyView();
	
	enum extras{
		Programm_Einstellungen,
		Etikett_Einstellungen,
		Etikett_Druck;
	}
	JPChooser extrachoose=new JPChooser(this,new Vector<String>(){{
		for (extras s:extras.values()){
			add(s.toString());
		}
	}},JPChooser.Orientation.VERTICAL);
	VEVExtraView(){
		setLayout(new BorderLayout());
		add(extrachoose,BorderLayout.NORTH);
		
			
		
	}


	public void itemSelected(ListSelectionEvent e) {
		// TODO Auto-generated method stub

	}
	

	public void thingSelected(SelectedEvent e) {
		// TODO Auto-generated method stub

	}

	public void toBackground() {
		Control.getControl().app_settings_pane.focusLost(null);
	
		
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


	@Override
	public void actionPerformed(ActionEvent e) {
		
		switch (Integer.valueOf(e.getActionCommand())){
			case 0: 
				add(settings,BorderLayout.CENTER);
				revalidate();
				break;
			case 1: 
				add(new VEVJPSticker(),BorderLayout.CENTER);
				revalidate();
				break;	
			case 2: 
				add(new VEVJPStickerPrint(),BorderLayout.CENTER);
				revalidate();
				break;	
		
		}
		extrachoose.clearSelection();

	}
	
	
}
