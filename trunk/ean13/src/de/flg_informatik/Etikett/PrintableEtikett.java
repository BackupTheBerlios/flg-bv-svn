package de.flg_informatik.Etikett;
import java.awt.Graphics;
import java.awt.Dimension;

public interface PrintableEtikett {
	String text=null;
	public int printAt(Graphics g, Dimension position, Dimension boxgroesse);

}
