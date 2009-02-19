package de.flg_informatik.utils;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;


public class FLGProperties extends  Panel implements TextListener,FocusListener{
	/**
	 * 
	 */
	private Properties properties2;
	private static final long serialVersionUID = 767010307826685780L;
	private JTabbedPane jtp =new JTabbedPane();
	private FLGProperties me;
	private String infilename;
	private File defaultfile;
	private String significantkey;
	private ArrayList<String> groups;
	private ArrayList<ArrayList<String>> keys;
	private ArrayList<TabPanel.LinePanel> changelist = new ArrayList<TabPanel.LinePanel>();
	private File outfile;
	private boolean changed=false;
	/**
	 * @param (Properties properties2) Properties als Objekt f�r die R�ckgabe, wenn null, dann wird das Propertyfile (infile bzw defaultfile) geladen.
	 * @param (String infilename) Filename des Propertyfiles, das im Userverzeichnis (unix mit .dot) abgelegt ist/wird
	 * @param (File defaultfile) File, das, falls kein Propertyfile gefunden wird, als Template verwendet wird (muss mitgeliefert werden)
	 * @param (String significantkey) Propertykey an dem das File erkannt werden kann, dient zur Verifizierung
	 * 
	 * ansonsten gibt es die Methoden readProperties(), saveProperties() speichert unter infile (s.u.),boolean savePropertiesAs(String filename);	 
	 * */

	public FLGProperties(java.util.Properties properties, String infilename, File defaultfile, String significantkey){
		super();
		this.infilename = infilename;
		this.defaultfile = defaultfile;
		this.significantkey = significantkey;
		if (properties==null){
			switch(de.flg_informatik.utils.shell.OSShell.getOS()){
				case linux:
					infilename=System.getProperty("user.home")+System.getProperty("file.separator")+"."+infilename;
				break;
				default:
				break;	
			// infilename=infilename;
			}
			properties=loadPropertiesFromXML(infilename,defaultfile,significantkey);
			debug(properties);
		}	
		this.me=this;
		this.setLayout(new BorderLayout(5,5));
		this.properties2 = properties;
		makeTabs(properties);
	}

	public Properties getProperties(){
		return properties2;
	}
	
	public Properties readProperties(){
		return loadPropertiesFromXML(infilename, defaultfile, significantkey);
	}
	
	public boolean saveProperties(){
		setProps();
		return savePropertiesToXML(new File(infilename), properties2, "");
	}
	
	public boolean savePropertiesAs(String newfilename){
		return savePropertiesToXML(new File(newfilename), properties2, "");
	}
	
	private void getGroups(Properties props){
		String keystring;
		String keykey;
		String groupstring;
		groups=new ArrayList<String>();
		keys=new ArrayList<ArrayList<String>>();
		groups.add("Allgemein");
		keys.add(new ArrayList<String>());
		for (Object obj: props.keySet()){
			keystring=obj.toString();
			keykey=keystring;
			if (keystring.indexOf(".")==0){
				keykey=keystring.substring(1, keystring.length());
			}
			if (keystring.indexOf("*")==0){
				keykey=keystring.substring(1, keystring.length());
			}
			if (!(keykey.indexOf(".")<0)&&(keykey.indexOf(".")<keykey.length())){
					groupstring=(keykey.substring(0, keykey.indexOf(".")));
					if (!groups.contains(groupstring)){
						groups.add(groupstring);
						keys.add(new ArrayList<String>());
						debug(keystring);
					}
					keys.get(groups.indexOf(groupstring)).add(keystring);
			}else{	
				keys.get(0).add(keystring);
			}
		}

		
	}
	private Properties loadPropertiesFromXML(String infilename, File defaultfile, String significantkey){
		File infile = new File(infilename);
		File getfile = new File(infilename);
		Properties props = new Properties();
		while(!props.containsKey(significantkey)){
			try{FileInputStream fis =new FileInputStream(getfile);
				props.loadFromXML(fis);
			}catch(java.io.FileNotFoundException e){
				javax.swing.JOptionPane.showMessageDialog(null, "Eine Einstellungsdatei wurde nicht in ihrem Benutzerverzeichnis gefunden. Es werden die Standardeinstellungen geladen.");
				/* switch(DecisionBox.getLabel(this,"Einstellungsdatei",
							new String[]{"In Ihrem Benutzerverzeichnis gibt es keine",
								"Einstellungsdatei: (\""+infilename+"\")",
								"Was wollen Sie: Die Einstellungsdatei..."},
							new String[]{"... aus default neu erstellen?","...suchen?"})){
						case 1:{ */
							getfile=defaultfile;
							debug("loading defaults from: "+getfile.getAbsolutePath());
							
						/* }
						case 2:{
							JFileChooser jfc = new javax.swing.JFileChooser();
							if(jfc.showOpenDialog(this)==javax.swing.JFileChooser.APPROVE_OPTION){
								getfile=jfc.getSelectedFile();
								infile=getfile;
							}
							break;
						}
					} */
			}catch(java.util.InvalidPropertiesFormatException e){
				debug("Format of props not valid!");
			}
			catch(java.io.IOException e){
				e.printStackTrace();
			}
			//File outfile=new File(infile.getAbsolutePath());
			//efaultfile=infile;
			
			
		}
		
		return props;
	}
	
	private boolean savePropertiesToXML(File file, Properties props, String comment){
		if (file!=null){
			try{
				java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
				props.storeToXML(fos , comment);
				fos.close();
				return true;
			}catch(Exception e){
				e.printStackTrace(); // debug("exception");
			}
			debug(file);
			
			return false;
		}	
		return false;
	}
	
	private void makeTabs(Properties props){
		
		clearTabs();
		getGroups(props);
		for (String string: groups){
			jtp.addTab(string, new TabPanel(keys.get(groups.indexOf(string)).toArray(new String[keys.get(groups.indexOf(string)).size()])));
			jtp.addFocusListener(this);
			
		}
		this.add(jtp,BorderLayout.CENTER);
		
	}
	
	private void clearTabs(){
		jtp.removeAll();
	}
	

	private void setProps(){
		for (TabPanel.LinePanel lpl : changelist){
			properties2.setProperty(lpl.lbl.getText(), lpl.tfld.getText().trim());
		}
		changed=true;
		changelist.removeAll(changelist);
		javax.swing.JOptionPane.showMessageDialog(null, "�nderungen �bernommen!");
	}

	static private void debug(Object obj){
		System.out.println(FLGProperties.class+": "+ obj);
	}
	
	class TabPanel extends Panel{
		/**
		 * Creates the list of Label-TextField pairs
		 * TextFields of dotted propkeys are set "setEditable(false)"
		 * and grayed out
		 */
		private static final long serialVersionUID = -4718591306709206094L;

		class LinePanel extends Panel{
			private static final long serialVersionUID = 4689741209659160762L;
			Label lbl;
			TextField tfld;
		}
		
		
		TabPanel(String[] propkeys){
			Panel inner=new Panel(new GridLayout(propkeys.length,0));
			this.setLayout(new FlowLayout());
			for (int i=0 ; i< propkeys.length; i++){
					//pnl.setSize(200,50);
					LinePanel pnl = new LinePanel();
					pnl.lbl = new Label(propkeys[i]);
					pnl.tfld = new TextField(properties2.getProperty(propkeys[i]),30);
					if (propkeys[i].codePointAt(0)=='.'){
						pnl.tfld.setEditable(false);
						pnl.tfld.setBackground(java.awt.Color.LIGHT_GRAY);
					}
					if (propkeys[i].codePointAt(0)=='*'){
						pnl.tfld.setEchoChar('*');
						pnl.tfld.setBackground(java.awt.Color.GRAY);
					}
					pnl.add(pnl.lbl);
					pnl.add(pnl.tfld);
					pnl.tfld.addTextListener(me);
					inner.add(pnl);
					
			}
			JScrollPane	jsp = new JScrollPane(inner,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			this.add(jsp);
			this.validateTree();
		}
	}
	
	public boolean isChanged(){
		return changed;
	}

	
	/* public void actionPerformed(ActionEvent e) {
		if (e.getSource()==save){
			this.removeMouseListener(this);
			setProps();
			savePropertiesToXML(outfile, properties2, "Property-File by FLG-Properties V0.5 of"+parent.toString(), false);
			makeTabs(properties2);
			this.addMouseListener(this);
		}
		if (e.getSource()==saveas){
			this.removeMouseListener(this);
			setProps();
			savePropertiesToXML(outfile, properties2, "Property-File by FLG-Properties V0.5 of"+parent.toString(), true);
			makeTabs(properties2);
			this.addMouseListener(this);
			
			
		}
	} */
	/**
	 * EventListeners from Interfaces
	 * a) implemented
	 */
	
	public void textValueChanged(TextEvent e) {
		debug("textChange");
		if (!changelist.contains(((TabPanel.LinePanel)((TextField)e.getSource()).getParent()))){
			changelist.add((TabPanel.LinePanel)((TextField)e.getSource()).getParent());
		}
		changed=true;
	}
	
	public void focusLost(FocusEvent e) {
		setProps();
		
	}
	
	/**
	 * EventListeners from Interfaces
	 * b) Stubs
	 */
	

	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
