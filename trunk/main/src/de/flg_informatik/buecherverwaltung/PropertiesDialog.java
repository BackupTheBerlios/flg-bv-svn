package de.flg_informatik.buecherverwaltung;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;

import de.flg_informatik.utils.FLGProperties;

public class PropertiesDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	FLGProperties propertiespanel;
	javax.swing.JButton cmdOk;
	
	public PropertiesDialog (java.util.Properties properties, String infilename, File defaultfile, String significantkey) {
		super ((java.awt.Frame) null, "Verbindungseinstellungen", true);
		this.setLayout(new java.awt.BorderLayout ());
		propertiespanel = new FLGProperties(properties,"buchverwaltung.xml", defaultfile, significantkey);
		cmdOk = new javax.swing.JButton ("Speichern");
		cmdOk.addActionListener(this);
		
		this.add(propertiespanel, java.awt.BorderLayout.CENTER);
		this.add(cmdOk, java.awt.BorderLayout.SOUTH);
		this.pack();
	}
	
	public void actionPerformed(ActionEvent evnt) {
		if (evnt.getSource() == cmdOk) {
			propertiespanel.saveProperties();
			this.setVisible(false);
		}
	}
	
	public static void showSettingsDialog (java.util.Properties properties, String infilename, File defaultfile, String significantkey) {
		PropertiesDialog dlg = new PropertiesDialog (properties, infilename,defaultfile,significantkey);
		dlg.setVisible(true);
	}

	
}
