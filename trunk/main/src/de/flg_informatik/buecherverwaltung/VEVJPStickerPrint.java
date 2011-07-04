package de.flg_informatik.buecherverwaltung;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;

import javax.swing.JButton;
import javax.swing.JPanel;

import de.flg_informatik.Etikett.EtikettDruck;
import de.flg_informatik.Etikett.PrintableEtikett;
import de.flg_informatik.ean13.Ean;
import de.flg_informatik.ean13.WrongCheckDigitException;
import de.flg_informatik.ean13.WrongLengthException;

@SuppressWarnings("serial")
public class VEVJPStickerPrint extends JPanel implements ActionListener{
	private JButton save = new JButton("Etiketten drucken");
	private JPEditLine[] editfields={new JPEditLine("Start-Ean/Start-Id",13),
			new JPEditLine("Anzahl",5),
			new JPEditLine("Offset",5)};
	private JPChooser type = new JPChooser(null, EEANType.values(), JPChooser.Orientation.HORZONTAL);
	
	VEVJPStickerPrint(){
		add (new JPanel(){{
			setLayout(new BorderLayout());
			add(new JPanel(){{
					setLayout(new GridLayout(5,1));
					for (JPEditLine el:editfields){
						add(el);
					}
					add (type);
				
			}},BorderLayout.CENTER);
			add(new JPanel(){{add(save);}},BorderLayout.SOUTH);
		}});
		save.addActionListener(this);
		
	}
	private PrintableEtikett[] makeEtiketts(Ean ean, int count){
		Ean[] ret = new Ean[count];
		for (int i=0; i< count; i++ ){
			try {
				ret[i]=new Ean(new BigInteger(ean.toString().substring(0,12)).add(new BigInteger(i+"")));
			} catch (WrongCheckDigitException e) {
				new InternalError(e.getLocalizedMessage());
			} catch (WrongLengthException e) {
				new InternalError(e.getLocalizedMessage());
			}
		}
		return ret;
		
	}

	
	//@Override
	public void actionPerformed(ActionEvent e) {
		Ean ean=null;
		int howmany;
		if (editfields[0].getText().length()<12){
			if (type.getSelected().equals(EEANType.ISBN.name())){
				new Warn("Dies ist zu kurz f�r eine ISBN!");
			}
			if (type.getSelected().equals(EEANType.Book.name())){
				ean=OBook.makeBookEan(new BigInteger(editfields[0].getText()));
			}
			//TODO other types
		}
		//TODO some plausibility Check and Warning
		if ((howmany=Integer.parseInt(editfields[1].getText()))>0){
			EtikettDruck.etikettenDruck(makeEtiketts(ean,howmany),Integer.parseInt(editfields[2].getText()));
			editfields[1].textfield.setText("");
		}
		
	}
	

}
