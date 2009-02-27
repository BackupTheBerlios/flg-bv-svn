package de.flg_informatik.paedmlovpn;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;

import de.flg_informatik.utils.FLGFrame;

public class Testframe extends FLGFrame {
 String string1=new String("os");
	private FlowLayout mclo = new FlowLayout();
	Panel os= new Panel();
	Label hal=new Label("Hallo");
	/**
	 * @param args
	 */
	public Testframe(){
		//os.add(hal);
		CardLayout mclo = new CardLayout(10,10);
		this.setLayout(mclo);
		
		this.setBounds(100,200,300,400);
		os.setLayout(new FlowLayout());
		os.add(hal);
		os.setBackground(Color.RED);
		os.setMinimumSize(new Dimension(200,200));
		os.setVisible(true);
		this.add(os, string1);
		//mclo.addLayoutComponent(os, string1);
		//mclo.layoutContainer(this);
		mclo.show(this, "os");
		//this.add(os);
		System.out.print(mclo);
		this.setVisible(true);
		this.validateTree();

		
	}
	public static void main(String[] args) {
		new Testframe();
		// TODO Auto-generated method stub
		
	}

}
