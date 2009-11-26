package de.flg_informatik.Etikett;
import java.awt.Dimension;
import java.awt.Graphics;

public interface PrintableEtikett {
	String text=null;
	public int printAt(Graphics g, Dimension position, Dimension boxgroesse);

}
