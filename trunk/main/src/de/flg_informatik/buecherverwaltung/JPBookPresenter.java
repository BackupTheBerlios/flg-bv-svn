package de.flg_informatik.buecherverwaltung;

import java.awt.Color;
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import de.flg_informatik.ean13.Ean;
import de.flg_informatik.ean13.WrongCheckDigitException;
import de.flg_informatik.ean13.WrongLengthException;

public class JPBookPresenter extends JPanel implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextField idf = new TextField("",14);
	private TextField titlef = new TextField("",40);
	private JButton end;
	private JButton cancel;
	private JDManualInput mi;
	OBook book;
	JPConditionSwitcher switcher;
	JPBookPresenter(OBook book,JPConditionSwitcher switcher,ActionListener listener){
		this.switcher=switcher;
		this.book=book;
		idf.setEditable(false);
		titlef.setEditable(false);
		add(idf);
		idf.addMouseListener(this);
		add(titlef);
		add(switcher);
		add(cancel=new JButton("Abbrechen"));
		cancel.setActionCommand("cancel");
		cancel.addActionListener(listener);
		add(end=new JButton("Abschließen"));
		end.setActionCommand("end");
		end.addActionListener(listener);
	}

	public void mouseClicked(MouseEvent e) {
			mi=new JDManualInput(Control.getControl().mainGUI);
			mi.setLblText("Book ID: 20...");
			mi.bclass.setEnabled(false);
			mi.bcust.setEnabled(false);
			mi.setVisible(true);
	}
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	synchronized boolean publish(OBook book){
		setVisible(false);
		if (book==null){
			idf.setText("");
			titlef.setText("");
			switcher.setCondition(0);
			setBackground(new Color(150,150,150));
			end.setEnabled(false);

		}else{
			idf.setText(OBook.makeBookEan(book.ID).toString());
			try {
				titlef.setText(OBTBookType.getTitle(new Ean(book.ISBN)));
			} catch (Exception e) {
				new Err("ISBN ungültig");
			} 
			switcher.setCondition(book.Scoring_of_condition);
			setBackground(JPConditionSwitcher.colorOfCondition(book.Scoring_of_condition));
			switcher.setVisible(true);
			end.setEnabled(true);
		}
		
		setVisible(true);
		MainGUI.val();
		return true;
		
		
	}

	
	

}
