package de.flg_informatik.paedmlovpn;


import java.awt.Panel;
import java.awt.Label;
import java.awt.Color;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.Properties;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.flg_informatik.utils.FLGProperties;
import de.flg_informatik.utils.FireButton;
import de.flg_informatik.utils.FLGFrame;
import de.flg_informatik.utils.Pause;
public class StartVPN extends FLGFrame {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int contel=4;
	static final int condir=16;
	
	
	int timeout = 20;
	int constat = 0;
	
	
	String title="openVPN Dienst-Steuerung über telnet";
	
	Panel setupcard;
	Label aktion = new Label("Versuche den openVPN Dienst zu erreichen...");
	Panel maincard = new Panel();
	Panel panel = new Panel();
	LogPane intelnet = new LogPane(this);
	Panel butpanel0 = new Panel();
	Panel butpanel1 = new Panel();
	Panel butpanel2 = new Panel();
	Panel butpanel3 = new Panel();
	Panel subpanel0 = new Panel();
	Panel subpanel1= new Panel();
	FireButton beenden = new FireButton("Abbrechen");
	FireButton setup = new FireButton("Einstellungen");
	CardLayout mainlayout = new CardLayout();
	StartVPNControl svpnctrl = null;
	FLGProperties flgprops;
	
	
	
	public StartVPN(StartVPNControl svpnctrl){
		
		this.svpnctrl=svpnctrl;
		setTitle(title);
		setVisible(true);
		this.setLayout(mainlayout);
		this.setBounds((int)(this.getGraphicsConfiguration().getBounds().width*.3),(int)(this.getGraphicsConfiguration().getBounds().height*.3),(int)(this.getGraphicsConfiguration().getBounds().width*.4),(int)(this.getGraphicsConfiguration().getBounds().height*.4));
		maincard.setLayout(new GridLayout(2,0));
			subpanel0.setLayout(new GridLayout(0,4));
			subpanel0.add(butpanel0);
			subpanel0.add(butpanel1);
			subpanel0.add(butpanel2);
			subpanel0.add(butpanel3);
			butpanel2.add(beenden);
			butpanel3.add(setup);
			subpanel0.doLayout();
			panel.setLayout(new GridLayout(2,0));
			panel.add(aktion);
			panel.add(subpanel0);
			beenden.addActionListener(svpnctrl);
			setup.addActionListener(svpnctrl);
			intelnet.append("InLogfile \n");
			intelnet.setBackground(Color.WHITE);
			maincard.add(panel,0);
			maincard.add(intelnet,1);
			flgprops = new FLGProperties(null,"ovpnproperties.xml",new File("ovpnproperties.default.xml"),"flgovpnV0");
		this.add(maincard,"maincard");
		pack();
	}
	
	void setup(){
		this.add(flgprops,"setupcard");
		
		mainlayout.show(this, "setupcard");
		this.setTitle("Einstellungen von " + title);
		
	}
	void mainCard(){
		this.remove(flgprops);
		mainlayout.show(this, "maincard");
		this.setTitle(title);
	}
	
	void tnconn (){
		setAktion("Verbindung zu Dienst öffnen ...");

	}
	
	void connecting (){
		setAktion("... verbinden mit Dienst ...");

	}
			
	void connected (){
		setAktion("... verbunden mit Dienst!");
		this.validate();
	}
	
	void disconnected(){
		setAktion("Nicht mit Backend  verbunden!");
		this.validate();
	}
	
	void tncfinished(){
		setAktion("...schließe Frontend");
		new Pause(8000);
		setClosewindow(true);
		setQuitsystem(true);
		this.dispose();
		System.exit(0);
		
	}
	
	void mountvpn(){
		setAktion("Laufwerke verbinden ...");
	}
	void vpnmounted(){
		setAktion("... Laufwerke verbunden!");
		beenden.setLabel("Trennen&Exit");
	}
	void unmountvpn(){
		butpanel2.remove(beenden);
		setAktion("Laufwerke trennen ...");
		
	}
	void vpnunmounted(){
		setAktion("... Laufwerke getrennt!");
		beenden.setEnabled(false);
	}
	void addtext(String text){
		intelnet.append(text);
		this.validateTree();
	}
	protected void toClose(){
		setClosewindow(false);
		setQuitsystem(false);
		beenden.fire();
		
	}
	
	
	void setAktion(String string){
		aktion.setText(string);
		aktion.validate();
	}
	
	
	
}
