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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;


public class FLGProperties extends  Panel implements MouseListener, TextListener{
	/**
	 * 
	 */
	public Properties properties;
	private static final long serialVersionUID = 767010307826685780L;
	private JTabbedPane jtp =new JTabbedPane();
	private FLGProperties me;
	private Panel buttonleiste;
	private Object parent=null;
	private ArrayList<String> groups;
	private ArrayList<ArrayList<String>> keys;
	private ArrayList<TabPanel.LinePanel> changelist = new ArrayList<TabPanel.LinePanel>();
	private File outfile;
	// private Button save=new Button("  speichern  ");
	// private Button saveas=new Button("speichern als");
	private boolean changed=false;
	/**
	 * @param (String infilename) Filename des Propertyfiles, das im Userverzeichnis (unix mit .dot) adgelegt ist/wird
	 * @param (File defaultfile) File, das, falls kein Propertyfile gefunden wird, als Template verwendet wird (muss mitgeliefert werden)
	 * @param (String significantkey) Propertykey an dem das File erkannt werden kann, dient zur Verifizierung
	 * @param (Object parent) Parentobject, dessen Properties bearbeitet werden. 
	 * 	 
	 * */

	public FLGProperties(java.util.Properties properties){
		super();
		this.properties = properties;
		// this.parent=parent;
		this.me=this;
		this.setLayout(new BorderLayout(5,5));
		JPanel buttonleiste = new JPanel();
		{
			/* buttonleiste.setLayout(new GridLayout(1,4));
			buttonleiste.add(new Panel());
			buttonleiste.add(saveas);
			buttonleiste.add(new Panel());
			buttonleiste.add(save);
			saveas.addActionListener(this);
			save.addActionListener(this); */
		}
		this.add(buttonleiste,BorderLayout.SOUTH);
		makeTabs(properties);
	}

	public Properties getProperties(){
		return properties;
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
	private void makeTabs(Properties props){
		
		clearTabs();
		getGroups(props);
		for (String string: groups){
			jtp.addTab(string, new TabPanel(keys.get(groups.indexOf(string)).toArray(new String[keys.get(groups.indexOf(string)).size()])));
			
		}
		this.add(jtp,BorderLayout.CENTER);
		this.addMouseListener(this);	
		
	}
	
	private void clearTabs(){
		jtp.removeAll();
	}
	

	private void setProps(){
		for (TabPanel.LinePanel lpl : changelist){
			properties.setProperty(lpl.lbl.getText(), lpl.tfld.getText().trim());
		}
		changed=true;
		changelist.removeAll(changelist);
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
					pnl.tfld = new TextField(properties.getProperty(propkeys[i]),30);
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
			savePropertiesToXML(outfile, properties, "Property-File by FLG-Properties V0.5 of"+parent.toString(), false);
			makeTabs(properties);
			this.addMouseListener(this);
		}
		if (e.getSource()==saveas){
			this.removeMouseListener(this);
			setProps();
			savePropertiesToXML(outfile, properties, "Property-File by FLG-Properties V0.5 of"+parent.toString(), true);
			makeTabs(properties);
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
	
	public void mouseExited(MouseEvent e) {
		/*debug("out");
		if (changelist.size()!=0&&!me.getBounds().contains(e.getX()-20, e.getY()-20)){
			this.removeMouseListener(this);
			this.toSave();
			
		}
		*/
	}
	
	/**
	 * EventListeners from Interfaces
	 * b) Stubs
	 */
	
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
