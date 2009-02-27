package de.flg_informatik.paedmlovpn;

import java.awt.Button;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CopyOfDecissionBox extends Dialog implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static int getLabel(Container owner, String title, String labels, String[] buttons){
		return getLabel(owner,title,new String[] {labels},buttons);
	}
	
	public static int getLabel(Container owner, String title, String[] labels, String[] buttons){
		CopyOfDecissionBox tbb = getTbb(owner, title);
		tbb.setLayout(new GridLayout(2,1));
		tbb.add(tbb.labels(labels));
		tbb.buttons=buttons;
		tbb.add(tbb.buttons());
		tbb.pack();
		tbb.dispose();
		tbb.pack();
		tbb.setVisible(true);
		while (tbb.back==-1)
		{
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		tbb.dispose();
		return tbb.back;		
		
	}
	
	public CopyOfDecissionBox(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
	}

	public CopyOfDecissionBox(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
	}
	
	public void actionPerformed(ActionEvent e) {
		for (int i=0;i<buttons.length;i++){
			if (e.getActionCommand()==buttons[i]){
				back=i;
				break;
			}
			
		}
		
		
	}

	private int back=-1;
	private String[] buttons;

	
	private Panel labels(String[] labels){
		Panel pan = new Panel();
		GridLayout gla = new GridLayout(labels.length,1);
		gla.setVgap(-3);
		pan.setLayout(gla);		
		for (int i=0;i<labels.length;i++){
			pan.add(new Label(labels[i]));
		}
		return pan;
		
	}
	private Panel buttons(){
		Panel pan=new Panel();
		Button but;
		for (int i=0;i<buttons.length;i++){
			pan.add(but=new Button(buttons[i]));
			but.addActionListener(this);
		}
		return pan;
	}

	private static CopyOfDecissionBox getTbb(Container owner, String title){
		CopyOfDecissionBox tbb;
		if (owner.getClass().isInstance(Frame.class)){
			tbb=new CopyOfDecissionBox((Frame) owner,title,false);
		}else{
			if (owner.getClass().isInstance(Frame.class)){
				tbb=new CopyOfDecissionBox((Dialog) owner,title,false);
			}else{
				tbb=new CopyOfDecissionBox((Frame) null,title,false);
			}
			
		}
		return tbb;
	}
	
	public static void main(String[] args){
		Frame frame = new Frame();
			System.out.println(CopyOfDecissionBox.getLabel(frame,"Einstellungsdatei",new String[]{"In Ihrem Benutzerverzeichnis gibt es keine","Einstellungsdatei. Was wollen Sie: Die Datei..."},new String[]{"...neu erstellen?","...suchen?"}));
	}
			

}
