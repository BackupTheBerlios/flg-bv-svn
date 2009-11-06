/**
 * 
 */
package de.flg_informatik.buecherverwaltung;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.sun.org.apache.xerces.internal.impl.RevalidationHandler;

import de.flg_informatik.buecherverwaltung.SelectedEvent.SelectedEventType;

/**
 * @author notkers
 *
 */
public class JDManualInput extends JDialog implements ActionListener {
	

	/**
	 * 
	 */
	public JDManualInput() {
		makeDialog();
	}

	/**
	 * @param owner
	 */
	public JDManualInput(Frame owner) {
		makeDialog();
	}

	/**
	 * @param owner
	 */
	public JDManualInput(Dialog owner) {
		super(owner);
		makeDialog();
	}

	/**
	 * @param owner
	 */
	public JDManualInput(Window owner) {
		super(owner);
		makeDialog();
	}

	/**
	 * @param owner
	 * @param modal
	 */
	public JDManualInput(Frame owner, boolean modal) {
		super(owner, modal);
		makeDialog();
	}

	/**
	 * @param owner
	 * @param title
	 */
	public JDManualInput(Frame owner, String title) {
		super(owner, title);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param modal
	 */
	public JDManualInput(Dialog owner, boolean modal) {
		super(owner, modal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param title
	 */
	public JDManualInput(Dialog owner, String title) {
		super(owner, title);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param modalityType
	 */
	public JDManualInput(Window owner, ModalityType modalityType) {
		super(owner, modalityType);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param title
	 */
	public JDManualInput(Window owner, String title) {
		super(owner, title);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 */
	public JDManualInput(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 */
	public JDManualInput(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param title
	 * @param modalityType
	 */
	public JDManualInput(Window owner, String title, ModalityType modalityType) {
		super(owner, title, modalityType);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @param gc
	 */
	public JDManualInput(Frame owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @param gc
	 */
	public JDManualInput(Dialog owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param title
	 * @param modalityType
	 * @param gc
	 */
	public JDManualInput(Window owner, String title, ModalityType modalityType,
			GraphicsConfiguration gc) {
		super(owner, title, modalityType, gc);
		// TODO Auto-generated constructor stub
	}
	JButton bbook=new JButton("als Buch");
	JButton bclass=new JButton("als Klasse");
	JButton bcust=new JButton("als Leiher");
	JButton	cancel=new JButton("Abbrechen");
	private JPEditLine editline = new JPEditLine("",10);
	private void makeDialog(){
		setLayout(new GridLayout(0,1));
		setTitle("Manuelle Eingabe");
		add(new JPanel(){{
			add(editline);
			add(new JTextField("x"){{
				setEditable(false);
				setBackground(Color.white);
			}});
			
		
		}});
		add(new JLabel("Bitte ohne Pr�fziffer eingeben!"));
		add(new JPanel(){{
			add(bbook);
			add(bclass);
			add(bcust);
			add(cancel);
		}});
		addActionListener(this);
		cancel.addActionListener(this);
		setModalityType(ModalityType.APPLICATION_MODAL);
		pack();
		setLocation(MainGUI.getCentering(this));
	}
	public void addActionListener(ActionListener listener){
		bbook.addActionListener(listener);
		bclass.addActionListener(listener);
		bcust.addActionListener(listener);
		cancel.addActionListener(listener);
	}
	public void setLblText(String text){
		editline.label.setText(text);
		validate();
	}
	
	public String getText(){
		return editline.getText();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(cancel)){
			
		}
		if (e.getSource().equals(bbook)){
			Control.getControl().scanner.eanScanned(OBook.makeBookEan(new BigInteger(getText())).toString());
		}
		if (e.getSource().equals(bclass)){
			new Notimpl();
		}
		if (e.getSource().equals(bcust)){
			new Notimpl();
		}
		setVisible(false); // get off
	}

}
