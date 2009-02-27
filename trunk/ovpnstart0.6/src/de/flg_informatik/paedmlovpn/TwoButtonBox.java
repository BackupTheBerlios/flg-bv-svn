package de.flg_informatik.paedmlovpn;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
enum TbbRueckgabe {
	eins,zwei,cancel,undefined
}
public class TwoButtonBox extends Dialog implements ActionListener{

	String left;
	String right;
	/* static final int first=1;
	static final int second=2;
	static final int cancel=-1;
	static final int undefined=0;
	*/
	TbbRueckgabe back=TbbRueckgabe.eins;

	public TwoButtonBox(Frame owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}
	

	public TwoButtonBox(Dialog owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}

	public TwoButtonBox(Frame owner, boolean modal) {
		super(owner, modal);
		// TODO Auto-generated constructor stub
	}

	public TwoButtonBox(Frame owner, String title) {
		super(owner, title);
		// TODO Auto-generated constructor stub
	}

	public TwoButtonBox(Dialog owner, String title) {
		super(owner, title);
		// TODO Auto-generated constructor stub
	}


	public TwoButtonBox(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		// TODO Auto-generated constructor stub
	}

	public TwoButtonBox(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
		// TODO Auto-generated constructor stub
	}



	public TwoButtonBox(Frame owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		// TODO Auto-generated constructor stub
	}

	public TwoButtonBox(Dialog owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		// TODO Auto-generated constructor stub
	}

	
	private void decorate(){
		Panel pan=new Panel();
		pan.add(new TextArea("In Ihrem Benutzerverzeichnis existiert keine "+ System.getProperty("line.separator")+" Einstellungsdatei, was wollen Sie tun?"));
		Button but;
		pan.add(but=new Button(left));
		but.addActionListener(this);
		pan.add(but=new Button(right));
		but.addActionListener(this);
		this.add(pan);
	}
	public static TbbRueckgabe getModal(Frame owner, String left, String right){
		TwoButtonBox tbb= new TwoButtonBox(owner, left+" or "+right, false);
		tbb.left=left;
		tbb.right=right;
		tbb.decorate();
		tbb.pack();
		tbb.setVisible(true);
		while (tbb.back==TbbRueckgabe.undefined)
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
	public static TbbRueckgabe getModal(Dialog owner, String left, String right){
		TwoButtonBox tbb= new TwoButtonBox(owner, left+" or "+right, false);
		tbb.left=left;
		tbb.right=right;
		tbb.decorate();
		tbb.pack();
		
		while (tbb.back==TbbRueckgabe.undefined)
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

	public void test(TbbRueckgabe tbb){
		switch (tbb){
		case eins:{
			
		
			}
		}
	}
	
	
	



	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand().compareTo(left.toString()));
		if((0==e.getActionCommand().compareTo(left.toString()))){
			back=TbbRueckgabe.eins;
			
		}
		if(e.getActionCommand()==right){
			back=TbbRueckgabe.zwei;
			
		}
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args){
		Frame frame = new Frame();
			System.out.println(TwoButtonBox.getModal(frame,"ja","nein"));
	}
	

}
