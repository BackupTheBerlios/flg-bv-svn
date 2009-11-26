package de.flg_informatik.scanner;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import javax.swing.JComboBox;

import de.flg_informatik.buecherverwaltung.Deb;
import de.flg_informatik.ean13.Ean;
import de.flg_informatik.utils.FLGFrame;
import de.flg_informatik.utils.FLGProperties;

public class ScanEmulator extends FLGFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int debug=0;
	/**
	 * @param args
	 */
	ArrayList<String>IDString=new ArrayList<String>();
	ArrayList<String> ISBNString=new ArrayList<String>();
	ArrayList<String> PersString=new ArrayList<String>();
	JComboBox selector=new JComboBox();

	FileWriter ttyfile;
	ScanEmulator(String filename){
		this(new File(filename));
	}
	public ScanEmulator(File file){
		try{
			ttyfile=new FileWriter(file,false);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		IDString=getIDString(10);
		ISBNString=getISBNString(10);
		PersString=getPersString(10);
		
		for(String id : IDString){
			selector.addItem(id);
			selector.addItem(id);
		}
		for(String id : ISBNString){
			selector.addItem(id);
		}
		for(String id : PersString){
			selector.addItem(id);
		}
		selector.addActionListener(this);
		this.setTitle("NEHEIT");
		this.add(selector);
		this.setVisible(true);
		this.pack();
		
		
	}
	
	ArrayList<String> getIDString(int num){
		ArrayList<String> ret= new ArrayList<String>();
		
		for (int i=1; i<=num; i++){
			ret.add(new Ean(new BigInteger("200000000015").add(new BigInteger(i+""))).toString());
		}
		return ret;
		
		
	}
	
	ArrayList<String> getISBNString(int num){
		ArrayList<String>  ret= new ArrayList<String> ();
		
		for (int i=1; i<=num; i++){
			ret.add(new Ean(new BigInteger("978314100600").add(new BigInteger(i+""))).toString());
		}
		return ret;
		
		
	}
	ArrayList<String>  getPersString(int num){
		ArrayList<String>  ret= new ArrayList<String> ();
		
		for (int i=1; i<=num; i++){
			ret.add(new Ean(new BigInteger("210000000000").add(new BigInteger(i+""))).toString());
		}
		return ret;
		
		
	}
	
	public static void main(String[] args) {
		if(System.getProperty("os.name").equals("Linux")){
			new ScanEmulator(new FLGProperties(null,"buchverwaltung.xml", new File("buchverwaltung.default.xml"), ".BuchverwaltungV01").getProperties().getProperty("scanner.emulator.filename_linux"));

		}
		if (System.getProperty("os.name").equals("Windows XP")){
				
			new ScanEmulator(new FLGProperties(null,"buchverwaltung.xml", new File("buchverwaltung.default.xml"), ".BuchverwaltungV01").getProperties().getProperty("scanner.emulator.filename_winnt"));
		}
		
	}

	public synchronized void actionPerformed(ActionEvent e) {
		try{
			ttyfile.write(((JComboBox)(e.getSource())).getSelectedItem()+"\n");
			ttyfile.flush();
			try{
				Thread.sleep(500);
			}catch(InterruptedException ie){
				
			}
			new Deb(debug,((JComboBox)(e.getSource())).getSelectedItem()+"\n");
			
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
	}

}
