package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigInteger;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;

import de.flg_informatik.Etikett.EtikettDruck;
import de.flg_informatik.Etikett.PrintableEtikett;
import de.flg_informatik.buecherverwaltung.SelectedEvent.SelectedEventType;
import de.flg_informatik.buecherverwaltung.VBTVBookTypeView.State;
import de.flg_informatik.ean13.Ean;
import de.flg_informatik.utils.FLGProperties;
import de.flg_informatik.utils.FireButton;

public class VEVJPBookStickerDelete extends JPanel implements ActionListener{
	private JButton save = new JButton("Bücher-Etiketten löschen");
	private JPEditLine[] editfields={new JPEditLine("Start-Id",12),
			new JPEditLine("Anzahl",5),
			new JPEditLine("Letzte-Id",12)};
	VEVJPBookStickerDelete(){
		add (new JPanel(){{
			setLayout(new BorderLayout());
			add(new JPanel(){{
					setLayout(new GridLayout(5,1));
					for (JPEditLine el:editfields){
						add(el);
					}			
			}},BorderLayout.CENTER);
			add(new JPanel(){{add(save);}},BorderLayout.SOUTH);
		}});
		save.addActionListener(this);
		
	}
	
	
	//@Override
	public void actionPerformed(ActionEvent e) {
		BigInteger first=BigInteger.ZERO;
		int howmany=0;
		BigInteger last=BigInteger.ZERO;
		if (editfields[0].textfield.getText().length()>0){
			first=new BigInteger(editfields[0].textfield.getText());
		}
		if (editfields[1].textfield.getText().length()>0){
			howmany=Integer.parseInt(editfields[1].textfield.getText());
		}
		if (editfields[2].textfield.getText().length()>0){
			last=new BigInteger(editfields[2].textfield.getText());
		}
		if (first.equals(BigInteger.ZERO) & last.compareTo(BigInteger.ZERO) > 0 ){
			if (howmany==0){
				howmany=1;
			}
			first=last.subtract(new BigInteger((howmany-1)+""));
		}
		if (last.equals(BigInteger.ZERO) & first.compareTo(BigInteger.ZERO) > 0 ){
			if (howmany==0){
				howmany=1;
			}
			last=first.add(new BigInteger((howmany-1)+""));
		}
		if ((last.compareTo(first) >= 0)&howmany==0 ){
			howmany=last.subtract(first).intValue()+1;
		}
		new Deb(first+", "+last+", "+howmany);
		if (last.equals(first.add(new BigInteger((howmany-1)+"")))){
			for (int i=0; i<howmany;i++){
				if(OBook.delete(first.add(new BigInteger(i+"")))){
					Control.logln("Gelöscht: Etikett "+first.add(new BigInteger(i+"")) );
				}
			}
			
		}else{
			new Warn("Ihre Eingabe ist inkonsistent!");
		}
		
		
	}
	

}
