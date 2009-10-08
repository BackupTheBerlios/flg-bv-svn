package de.flg_informatik.buecherverwaltung;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JPanel;

import de.flg_informatik.utils.FLGProperties;

public class VPVDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	FLGProperties propertiespanel;
	javax.swing.JButton cmdOk;
	javax.swing.JButton cmdSave;
	javax.swing.JButton cmdCanc;
	
/*	public VPVDialog (java.util.Properties properties, String infilename, File defaultfile, String significantkey) {
		super ((java.awt.Frame) null, "Verbindungseinstellungen", true);
		this.setLayout(new java.awt.BorderLayout ());
		propertiespanel = new FLGProperties(properties,"buchverwaltung.xml", defaultfile, significantkey);
		cmdOk = new javax.swing.JButton ("Speichern");
		cmdOk.addActionListener(this);
		cmdCanc = new javax.swing.JButton ("Abbruch Programm");
		cmdCanc.addActionListener(this);
		
		this.add(propertiespanel, java.awt.BorderLayout.CENTER);
		this.add(new JPanel(){{
				add(cmdOk);
				add(cmdCanc);
			}}, java.awt.BorderLayout.SOUTH);
		this.pack();
	}
	*/
	public VPVDialog (FLGProperties propertiespanel) {
		super ((java.awt.Frame) null, "Verbindungseinstellungen", true);
		this.setLayout(new java.awt.BorderLayout ());
		this.propertiespanel = propertiespanel;
		cmdOk = new javax.swing.JButton ("Übernehmen");
		cmdOk.addActionListener(this);
		cmdSave = new javax.swing.JButton ("Speichern und Übernehmen");
		cmdSave.addActionListener(this);
		cmdCanc = new javax.swing.JButton ("Abbruch Programm");
		cmdCanc.addActionListener(this);
		
		this.add(propertiespanel, java.awt.BorderLayout.CENTER);
		this.add(new JPanel(){{
				add(cmdOk);
				add(cmdSave);
				add(cmdCanc);
			}}, java.awt.BorderLayout.SOUTH);
		this.pack();
	}
	public void actionPerformed(ActionEvent evnt) {
		if (evnt.getSource() == cmdOk) {
			propertiespanel.setProperties();
			this.setVisible(false);
		}
		if (evnt.getSource() == cmdSave) {
			propertiespanel.setProperties();
			propertiespanel.saveProperties();
			this.setVisible(false);
		}
		if (evnt.getSource() == cmdCanc) {
			System.exit(1);
			this.setVisible(false);
		}
	}
	
/*public static void showSettingsDialog (java.util.Properties properties, String infilename, File defaultfile, String significantkey) {
		VPVDialog dlg = new VPVDialog (properties, infilename,defaultfile,significantkey);
		dlg.setVisible(true);
	}*/
	public static void showSettingsDialog (FLGProperties propertiespanel) {
		VPVDialog dlg = new VPVDialog (propertiespanel);
		dlg.setVisible(true);
	}
	
}
