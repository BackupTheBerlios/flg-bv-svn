package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.buecherverwaltung.SelectedEvent.SelectedEventType;
import de.flg_informatik.utils.FLGFrame;

public class VBVCSVImporterView extends JPanel implements UCCase {
	//TODO
	
	private String tablename=null;
	private Container container=null;
	private static String stablename=null;
	private static Container scontainer=null;
	private static final boolean debug=true;
	private static final String unknownfield="@unknown";
	private static final String nullfield="@setnull";
	private static final String ignorefield="@ignore";
	private static final String defaultfield="@default";
	private static Pattern delimited=Pattern.compile("(\\A\".*\"\\z)");
	private boolean isdelimited;
	private Vector <String> sqlfields;
	private Vector <FileColumn> filefields;
	private Vector <JCheckBox> emptynull = new Vector<JCheckBox>();
	private Vector <FieldSelector> filecombo= new Vector <FieldSelector>();
	private Vector <JTextField> defaultval= new Vector <JTextField>();
	private File infile=null;
	private static FileColumn unknowncolumn=new VBVCSVImporterView().new FileColumn(unknownfield,-1);
	private static FileColumn nullcolumn=new VBVCSVImporterView().new FileColumn(nullfield,-2);
	private static FileColumn ignorecolumn=new VBVCSVImporterView().new FileColumn(ignorefield,-3);
	private static FileColumn defaultcolumn=new VBVCSVImporterView().new FileColumn(defaultfield,-4);
	private static final String fieldseparator =",";
	private JButton OK = new JButton("Anwenden");
	private JButton load = new JButton("File Importieren");
	
	private JPChooser bvc;
	private char[] trenner;
	private FileReader inreader = null;
	private NorthPanel np=null;
	private WestPanel wp=null;
	private SouthPanel sp=null;
	private JScrollPane cp=null;
	
	public VBVCSVImporterView(String tablename, Container container){
		setLayout(new BorderLayout());
		this.tablename=tablename;
		init(container);
	}
	public VBVCSVImporterView(){
		setLayout(new BorderLayout());
		
	}
	public boolean init(Container container){
		
		if ( container == null){
			throw new java.lang.InternalError("Initialisierungsfehler in: "+this.getClass());
		}
		this.container=container;
		removeAll();
		add(sp=new SouthPanel(),BorderLayout.SOUTH);
		add(np=new NorthPanel(),BorderLayout.NORTH);
		add(wp=new WestPanel(),BorderLayout.WEST);
		add(cp=new JScrollPane(),BorderLayout.CENTER);// Dummy
		
		//OK.addActionListener(sp);
		setVisible(true);
		return true;
	}
	public static void setTablename(String tablename){
		stablename=tablename;
	}
	public static void setContainer(Container container){
		scontainer=container;
	}
	private boolean openFile(){
		if  ((infile=VBVCSVIVFileDialog.getFileOpen((Frame)container))==null){
			new Mess("Please choose a file!");
				// nothing chosen
			return false;
		}
		if (!infile.canRead()){ // something went wrong, maybe no rights?
			new Mess("Cannot open "+infile.getAbsolutePath()+"for reading!");
			return false;
		}else{
			// O.K.
			return true;
			
		}
	}
	
	private class MySQLcolumns extends JPanel{
		private FieldSelector fs=null;
		private JCheckBox en=null;
		private JTextField tf=null;
		Vector<Boolean> notNullColumns = new Vector<Boolean>(); 
		Vector<String> defaultColumns = new Vector<String>(); 
		public MySQLcolumns() {
			sqlfields=USQLQuery.getColumnHeaders(tablename);
			notNullColumns=USQLQuery.getNotNullColumns(tablename);
			defaultColumns=USQLQuery.getDefaults(tablename);
			setLayout(new GridLayout(sqlfields.size(),4));
			filecombo.removeAllElements();
			for (int i= 0; i<sqlfields.size();i++){
				fs=new FieldSelector(sqlfields.get(i));
				filecombo.add(fs);
				add(fs); 
				add(new JLabel(" => "+sqlfields.get(i)));
				new Deb(debug,sqlfields.get(i));
				en=new JCheckBox("wenn leer, dann MYSQL-NULL",!notNullColumns.get(i));
				emptynull.add(en);
				add(new JPanel(){{add(en,JPanel.LEFT_ALIGNMENT);}});
				
					if (notNullColumns.get(i)&(defaultColumns.get(i)==null)){
						add(new JLabel("kein (NULL & NOT NULL)"){{
							setToolTipText("Für neue Datensätze: " +
									"zuordnen oder default eintragen," +
									" für updates auch ignorieren möglich!");}});
					}else{
						if (defaultColumns.get(i)==null){
							add(new JLabel("SQLDefault: SQL-NULL"));
						}else{
							add(new JLabel("SQLDefault: "+defaultColumns.get(i)));
						}
					}
					add(new JPanel(){{
						add(new JLabel("@default= "));
							tf=new JTextField(10);
							defaultval.add(tf);
							add(tf);
					}});
			
				
			}
			setVisible(true);
		}
	
		
		
	}
	private class FieldSelector extends javax.swing.JComboBox{
		FieldSelector(String msqlc){
			super(filefields);
			setSelectedItem(getGuess(msqlc));
			
						
		}
		
	}
	private class WestPanel extends JPanel {
		String[] was = new String[]{"Schüler", "Klassen"};
		public WestPanel() {
			add(bvc=new JPChooser(VBVCSVImporterView.this.sp,was,JPChooser.Orientation.VERTICAL));
			setVisible(true);
		}
		
	}
	private class NorthPanel extends JPanel {
		JTextField fsep= new JTextField(fieldseparator,0){{setBorder(null);setBackground(Color.WHITE);this.setFont(new java.awt.Font(this.getFont().toString(),java.awt.Font.BOLD,(int)(this.getFont().getSize()*1.5)));}};
		ButtonGroup bg =new ButtonGroup();
		JRadioButton CO = new JRadioButton("\",\"");
		JRadioButton SC = new JRadioButton("\";\"");
		JRadioButton FS = new JRadioButton("=>");
		JCheckBox CR = new JCheckBox("CR=\"\\r\"");
		JCheckBox LF = new JCheckBox("LF=\"\\n\"");
		JCheckBox QM = new JCheckBox();
		
		NorthPanel(){
			super(new GridLayout(1,4));
			bg.add(CO);
			bg.add(SC);
			bg.add(FS);
			add(new JPanel(){{add(new JLabel("Feldtrenner:"));add(CO);add(SC);add(FS);add(fsep);}});
			add(new JPanel(){{add(new JLabel("Zeilentrenner:")); add(CR);add(LF);}});
			add(new JPanel(){{add(new JLabel("\" \" als opt. Textauszeichner:")); add(QM);;}});
			CO.setSelected(true);
			if(System.getProperty("line.separator").equals("\n")){
				CR.setSelected(false);
				LF.setSelected(true);
			}
			if(System.getProperty("line.separator").equals("\r\n")){
				CR.setSelected(true);
				LF.setSelected(true);
			}
			if(System.getProperty("line.separator").equals("\r")){
				CR.setSelected(true);
				LF.setSelected(false);
			}
			QM.setSelected(true);
			trenner=getChars();
			isdelimited=QM.isSelected();
			
			add(new JPanel(){{add(OK);}});
			//
		}
		private char[] getChars(){
			char[] ret = new char[3];
			if (CO.isSelected()){
				ret[0]=',';
			}
			if (SC.isSelected()){
				ret[0]=';';
			}
			if (FS.isSelected()){
				ret[0]=fsep.getText().charAt(0);
			}
			if (CR.isSelected()){
				ret[1]='\r';
				if (LF.isSelected()){
					ret[2]='\n';
				}else{
					ret[2]=0;
				}
			}else{
				if (LF.isSelected()){
					ret[1]='\n';
				}else{
					LF.doClick();
					new Warn("Kein Zeilentrenner gewählt\n es wird LF (\\n) verwendet!" );
					
				}
				ret[2]=0;
			}
			isdelimited=QM.isSelected();
			return ret;
		
		}

		
			
		
		
	}
	private class CenterPanel extends JScrollPane{
		public CenterPanel() {
			
			
			setViewportView(new JPanel(new GridLayout(1,0)){{add(new MySQLcolumns());}});
	}
		public Dimension getPreferredSize() {
			return new Dimension(500,500);
		}
		
	
	}
	/* 
	 * Mainly acting as Controller
	 */ 
	
	private class SouthPanel extends JPanel implements ActionListener{
		SouthPanel(){
		
		}

		public void actionPerformed(ActionEvent e) {
			new Deb(debug,e);
			if (e.getSource().equals(OK)){
				OK.setEnabled(false);
				trenner=np.getChars();
				isdelimited=np.QM.isSelected();
				if (tablename==null){
					new Warn("Bitte wählen Sie einen Anwendungsfall!");
					OK.setEnabled(true);
					return;
				}
				if (!openFile()){
					new Warn("Konnte das File nicht öffnen!");
					OK.setEnabled(true);
					return;
				}
				filefields=readFileColumns();
				getParent().remove(cp);
				cp=null;
				getParent().add(cp=new CenterPanel(),BorderLayout.CENTER);
				cp.invalidate();
				this.removeAll();
				this.add(load);
				load.addActionListener(this);
				getParent().validate();
				OK.setEnabled(true);
			}
			if (e.getSource().equals(load)){
				load.removeActionListener(this);
				this.remove(load);
				Control.getControl().mainGUI.setCursor(
		                Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				dataImport();
				getParent().remove(cp);
				cp=null;
				getParent().add(cp=new JScrollPane(),BorderLayout.CENTER);// Dummy
				Control.getControl().mainGUI.setCursor(
		                Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				getParent().validate();
			}
			if (e.getActionCommand().equals("0")){
				tablename="Customers";
				OK.doClick();
				
			}
			if (e.getActionCommand().equals("1")){
				tablename="Classes";
				OK.doClick();
				
			}
		}

	
	}
	private class FileColumn{
		String name; int index; 
		FileColumn(String name, int index){
			this.name=name; this.index=index;
		}
		public String toString(){
			return this.name;
		}
	}
	private void dataImport() {
		String line[];
		FileColumn fc;
		Vector <String> sets=new Vector <String>();
		

		while ((line=getLine())!=null){
			sets.removeAllElements();
			for (int i=0; i<sqlfields.size();i++){
				fc=(FileColumn)(filecombo.get(i).getSelectedItem());
				new Deb(debug,filecombo);
				if(fc.index>=0){
					if (line[fc.index].equals("")&emptynull.get(i).isSelected()){
						sets.add(sqlfields.get(i).toString()+" = NULL");
					}else{
						sets.add(sqlfields.get(i).toString()+" = '" + line[fc.index]+"'");
					}
					
				}
				if(fc==unknowncolumn){
					new Warn("Bitte allen Feldern einen Wert <> \""+unknownfield+ "\" zuordnen!\n"
							+ "\"" + ignorefield + "\" ändert nichts (UPDATE) / setzt SQL-default (INSERT)\n"
							+ "\"" + nullfield + "\" weist den Wert SQL-NULL zu \n"
							+ "\"" + defaultfield + "\" weist den Wert zu zu \n"
							+ "... dies kann je nach Tabellen-Definition zu einem SQL-Error führen!" );
					return; 
				}
				if (fc==nullcolumn){
					sets.add(sqlfields.get(i).toString()+" = NULL");
				}
				if (fc==defaultcolumn){
					sets.add(sqlfields.get(i).toString()+" = '" + defaultval.get(i).getText()+"'");
				}
			}
			sqlWrite(sets);
		}
		


	}
	private synchronized boolean sqlWrite(Vector<String> sets){
			StringBuffer strb=new StringBuffer();
			if (USQLQuery.doCount("SELECT ALL COUNT(*) FROM "+ tablename +" WHERE "+sets.firstElement())>0){
				strb.append("UPDATE "+ tablename +" SET ");
				for (int i= 1; i< sets.size()-1;i++){
					strb.append(sets.get(i)+", ");
				}
				strb.append(sets.lastElement()+ " WHERE "+sets.firstElement());
				USQLQuery.doUpdate(strb.toString());
			}else{
				strb.append("INSERT  INTO "+ tablename +" SET ");
				for (int i= 0; i< sets.size()-1;i++){
					strb.append(sets.get(i)+", ");
				}
				strb.append(sets.lastElement());
				USQLQuery.doInsert(strb.toString());
			}
		return true;
	}
	
	
		
	
	private FileColumn getGuess(String msqlc) { //TODO better heuristic wanted
		new Deb(debug,filefields);
		for (FileColumn str:filefields){
			if (!(str.index<0)){
				//new Deb(debug,str);
				String matchstring1=new String("(\\A"+str.name+"\\z)");
				Pattern p1=Pattern.compile(matchstring1);
				String matchstring2=new String("(\\A"+str.name.subSequence(0, 1)+".*"+str.name.subSequence(str.name.length()-1, str.name.length())+"\\z)");
				Pattern p2=Pattern.compile(matchstring2);
				String matchstring3=new String("("+str.name.subSequence(0, 2)+")");
				Pattern p3=Pattern.compile(matchstring1);
				if (p1.matcher((CharSequence)(msqlc)).matches()){
					new Deb(debug,"M1");
					return(str);
				}
				if (p2.matcher((CharSequence)(msqlc)).matches()){
					new Deb(debug,"M2");
					return(str);
					
				}
				if (p3.matcher((CharSequence)(msqlc)).matches()){
					new Deb(debug,"M3");
					return(str);
					
				}
			}
		
		}
		return(unknowncolumn);
		
		
	}
	
	private Vector<FileColumn> readFileColumns(){
		Vector<FileColumn> ret = new Vector<FileColumn>();
		StringBuffer strb = new StringBuffer();
		ret.removeAllElements();
		int i;
			try {	
				if (inreader!=null){
					inreader.close();
				}
				inreader = new FileReader(infile);
				while (((i = inreader.read())!= -1) && (i != trenner[1])){
					strb.appendCodePoint(i);						
				}					
			} catch (FileNotFoundException fnfe) {
				// TODO: handle exception
			} catch (IOException ioe){
				
			}
			String[] string=(new String(strb)).split(Character.toString((trenner[0])));
			for (i=0;i<string.length;i++){
				ret.add(new FileColumn(doStrip(string[i]),i));
			}
			ret.add(unknowncolumn);
			ret.add(ignorecolumn);
			ret.add(nullcolumn);
			ret.add(defaultcolumn);
		return ret;
		
	}
	
	
	
	
	

	public void itemSelected(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void toBackground() {
		if (inreader!=null){
			try{
				inreader.close();
			}catch(IOException ioe){
				
			}
			inreader=null;
		}
		removeAll();
		add(np=new NorthPanel(),BorderLayout.NORTH);
		add(cp=new JScrollPane(),BorderLayout.CENTER);// Dummy
		add(wp=new WestPanel(),BorderLayout.WEST);
		add(sp=new SouthPanel(),BorderLayout.SOUTH);
		
		OK.addActionListener(sp);
		new Deb(true,"ret" );
		
	}
	public void thingSelected(SelectedEvent e) {
		// TODO Auto-generated method stub
		
	}
	private synchronized String[] getLine() {
		String[] ret= new String[filefields.size()-4];
		String[] get= new String[filefields.size()-4];
		StringBuffer strb=new StringBuffer();
		int i;
		try {	
			while (((i = inreader.read())!= -1) && (i != trenner[1])){
				
				if (i!= trenner[2]){// ignore rest of /newline!
					strb.appendCodePoint(i);
				}	
			}	
			if (i==-1){ return null; }
		} catch (FileNotFoundException fnfe) {
			// TODO: handle exception
		} catch (IOException ioe){
			ioe.printStackTrace();
		
			
		}
		
		get=strb.toString().split(Character.toString(trenner[0]));
		for (int j=0; j< ret.length; j++){
			ret[j]=doStrip(get[j]);
		}
	return ret;
	}
	private synchronized String doStrip(String string){
		String ret=new String();
		if (isdelimited & delimited.matcher(string).matches()){
			ret = new String(string.substring(1,string.length()-1));
		}else{
			ret = string;
		}
		return ret;
	}
	
	public void toClose() {
		
		
	}
	
	public Vector<SelectedEventType> getConsumedEvents() {
		// TODO Auto-generated method stub
		return null;
	}
	public void toFront() {
		// TODO Auto-generated method stub
		
	}
	

}
