package de.flg_informatik.Etikett;
import java.awt.Graphics;
import java.awt.Dimension;

public interface PrintableEtikett {
	public int printAt(Graphics g, Dimension position, Dimension boxgroesse);

}
