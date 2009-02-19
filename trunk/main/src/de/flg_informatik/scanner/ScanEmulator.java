package de.flg_informatik.scanner;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import javax.swing.JComboBox;

import de.flg_informatik.ean13.Ean;
import de.flg_informatik.utils.FLGFrame;

public class ScanEmulator extends FLGFrame implements ActionListener{
	
	/**
	 * @param args
	 */
	ArrayList<String>IDString=new ArrayList<String>();
	ArrayList<String> ISBNString=new ArrayList<String>();
	ArrayList<String> PersString=new ArrayList<String>();
	JComboBox selector=new JComboBox();

	FileWriter ttyfile;
	ScanEmulator(String filename){
		try{
			ttyfile=new FileWriter(new File(filename),true);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		IDString=getIDString(10);
		ISBNString=getISBNString(10);
		PersString=getPersString(10);
		
		for(String id : IDString){
			selector.addItem(id);
		}
		for(String id : ISBNString){
			selector.addItem(id);
		}
		for(String id : PersString){
			selector.addItem(id);
		}
		selector.addActionListener(this);
		this.add(selector);
		this.setVisible(true);
		this.pack();
		
		
	}
	
	ArrayList<String> getIDString(int num){
		ArrayList<String> ret= new ArrayList<String>();
		
		for (int i=1; i<=num; i++){
			ret.add(new Ean(new BigInteger("200000000000").add(new BigInteger(i+""))).toString());
		}
		return ret;
		
		
	}
	
	ArrayList<String> getISBNString(int num){
		ArrayList<String>  ret= new ArrayList<String> ();
		
		for (int i=1; i<=num; i++){
			ret.add(new Ean(new BigInteger("9783141006000").add(new BigInteger(i+""))).toString());
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
		new ScanEmulator("C:\\temp\\temp.ll");
		// TODO Auto-generated method stub

	}

	public void actionPerformed(ActionEvent e) {
		try{
			ttyfile.write(((JComboBox)(e.getSource())).getSelectedItem()+"\n");
			ttyfile.flush();
			System.out.println(((JComboBox)(e.getSource())).getSelectedItem()+"\n");
			
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
	}

}
