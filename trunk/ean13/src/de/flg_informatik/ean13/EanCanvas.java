package de.flg_informatik.ean13;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;


public class EanCanvas extends Canvas implements Ean13, de.flg_informatik.Etikett.PrintableEtikett{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Dimension line = new Dimension(4,128); //testing only
	
	private int codewidth;
	private final char trenner = '|';
	private final char ende = '@';
	private Dimension code = new Dimension(0,0);
	private Dimension start = new Dimension(0,0);
	private Dimension label = new Dimension(0,0);
	private Dimension offset = new Dimension(0,0);
	private String text;
	private double textscale=1;
	private int line1 = 0;
	@SuppressWarnings("unused")
	private int line6 = 0;
	private int line7 = 0;
	private int line8 = 0;
	private Ean ean;
	private Dimension startl = new Dimension(0,0);
	
	private EanCanvas(){
		this.setBackground(Color.WHITE);
	}
	public EanCanvas(Ean ean){
		this();
		this.ean=ean;
	}
	public EanCanvas(Ean ean, Dimension box){
		this(ean);
		setDimensions(box);
	}
	
	public EanCanvas(Ean ean, Dimension pos, Dimension box){
		this(ean,box);
		offset=pos;
	}
		
	public EanCanvas(Ean ean, String text){
		this(ean);
		this.text=text;
	}
	public EanCanvas(Ean ean, Dimension box, String text){
		this(ean,box);
		this.text=text;
	}
	public EanCanvas(Ean ean, Dimension pos, Dimension box, String text){
		this(ean,pos,box);
		this.text=text;
	}
	
	public EanCanvas(Ean ean, String text, double textscale){
		this(ean,text);
		this.textscale=textscale;
	}
	public EanCanvas(Ean ean, Dimension box, String text, double textscale){
		this(ean,box,text);
		this.textscale=textscale;
	}
	public EanCanvas(Ean ean, Dimension pos, Dimension box, String text, double textscale){
		this(ean,pos,box,text);
		this.textscale=textscale;
	}
	
	/**
	 * @param args
	 */
	public Dimension getMinimumSize(){
		return new Dimension(label);
	}
	public Dimension getMaximumSize(){
		return new Dimension(label);
	}
	public Dimension getPreferredSize(){
		return new Dimension(label);
	}
	public String toString(){
		return ean.toString();
	}
	public Ean getEan(){
		return ean;
	}
	public int setLabelSize(Dimension label){
		return setDimensions(label);
	}
	public int setEan(Ean ean){
		this.ean=ean;
		return setDimensions(label);
	}
	@SuppressWarnings("serial")
	public static void main(String[] args) { //testing only
		EanCanvas eac = new EanCanvas(new Ean("400246403540"),new Dimension(150,200));
		Frame fra = new de.flg_informatik.utils.FLGFrame(){{
			setLayout(new GridLayout(1,2));
		}};
		fra.setTitle("EAN:"+eac.toString());
		fra.add(eac);
		EanCanvas eac2 = new EanCanvas(new Ean("400246403000"),new Dimension(150,200),"text");
		fra.add(eac2);
		fra.setVisible(true);
		fra.pack();
		
	}
	public int printAt(Graphics g, Dimension pos, Dimension box){
		offset=pos.getSize();
		int ret=setDimensions(box);
		print(g);
		return ret;
	}
	public void paint(Graphics g){
		
	}
	
	public void print(Graphics g){
		startl.setSize(start);
		g.setClip(offset.width, offset.height, label.width, label.height);
		g.setFont(new Font("SansSerif", Font.BOLD, line7));
		g.setColor(Color.BLACK);
		g.drawChars(ean.getEanChars(), 0, 1, startl.width-line6, startl.height+line.height+line6);
		//startl.width+=2*line1;
		paintZiffer(g,'@',0,startl);
		for (int i=1; i<digits; i++){
			g.setColor(Color.BLACK);
			if (i==7) paintZiffer(g,'|',0,startl);
			g.drawChars(ean.getEanChars(), i, 1, startl.width+line1, startl.height+line.height+line6);
			paintZiffer(g,ean.getEanChar(i),alphabeth13[ean.getEanChar(0)-'0'][i-1],startl);
		}
		g.setColor(Color.BLACK);
		paintZiffer(g,'@',0,startl);
		if (text!=null){
			g.setFont(new Font("Serif", Font.PLAIN, getTextHeight()));
			int len=Math.min(g.getFontMetrics().stringWidth(text),label.width);
			g.drawString(text, offset.width+Math.max(0, (label.width-len)/2), start.height+line.height+line6+getTextHeight());
		}
		
	}
	private void paintZiffer(Graphics g,char zeichen, int variante, Dimension start){
		
		switch (zeichen){
			case trenner:
				start.width+=line1;
			case ende:
				g.fillRect(start.width, start.height, line1, line.height+5*line1);
				start.width+=line1;
				start.width+=line1;
				g.fillRect(start.width, start.height, line1, line.height+5*line1);
				start.width+=line1;
				start.width+=line1;
				if (zeichen==ende) break;
				start.width+=line1;
				break;
			default:
				switch (variante){
				case 1:
					g.setColor(Color.WHITE);
				case 3:
					for (int i=0; i<4; i++){
						g.fillRect(start.width, start.height, line1*alphabeth[zeichen-'0'][i], line.height);
						start.width+=line1*alphabeth[zeichen-'0'][i];
						if (g.getColor()== Color.WHITE) g.setColor(Color.BLACK); else g.setColor(Color.WHITE);
					}
					break;
				case 2:
					g.setColor(Color.WHITE);
					for (int i=3; i>=0; i--){
						g.fillRect(start.width, start.height, line1*alphabeth[zeichen-'0'][i], line.height);
						start.width+=line1*alphabeth[zeichen-'0'][i];
						if (g.getColor()== Color.WHITE) g.setColor(Color.BLACK); else g.setColor(Color.WHITE);
					}
					break;
				}
		}
		
	}
	private int getTextHeight(){
		return (int)(line8*textscale);
	}
	private int setDimensions(Dimension label){
		this.label=label;
		codewidth=(3+digits)*7;
		line.width = label.width / codewidth;
		line1=line.width;
		line6=6*line1;
		line7=7*line1;
		line8=8*line1;
		line.height = label.height-line7;
		if (text!=null){
			line.height-=getTextHeight();
		}
		code.setSize(codewidth*line1,label.height);
		start.setSize(line7 +(label.width-code.width)/2+offset.width
				,offset.height);
		return line.width;
	}
		
	
}
