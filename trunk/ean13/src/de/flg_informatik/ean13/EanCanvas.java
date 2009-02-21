package de.flg_informatik.ean13;

import java.awt.*;
import java.math.BigInteger;


public class EanCanvas extends Canvas implements Ean13, de.flg_informatik.Etikett.PrintableEtikett {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int randvert = 10;
	private final int randhori = 4;
	
	private Dimension line = new Dimension(4,128); //testing only
	
	private final int codewidth =(2+digits)*7+2*randhori;
	private final char trenner = '|';
	private final char ende = '@';
	private Dimension here = new Dimension(0,0);
	private Dimension code = new Dimension(0,0);
	private Dimension start = new Dimension(0,0);
	private Dimension label = new Dimension(0,0);
	private Dimension offset = new Dimension(0,0);
	
	private int line1 = 0;
	@SuppressWarnings("unused")
	private int line6 = 0;
	private int line7 = 0;
	private int line8 = 0;
	private Ean ean;
	private Dimension startl = new Dimension(0,0);
	
	public EanCanvas(){
		this.setBackground(Color.WHITE);
		label.width=codewidth*line.width;
		setDimensions();
	}
	public EanCanvas(Ean ean){
		this();
		this.ean=ean;
	}
	public EanCanvas(Ean ean, Dimension box){
		this();
		setDimensions(box);
		this.ean=ean;
	}
	public EanCanvas(String string, Dimension box){
		this();
		setDimensions(box);
		this.ean=new Ean(string);
	}
	public EanCanvas(Ean ean, Dimension pos, Dimension box){
		this();
		this.here=pos;
		setDimensions(box);
		this.ean=ean;
	}
	public EanCanvas(String string, Dimension pos, Dimension box){
		this();
		this.here=pos;
		setDimensions(box);
		this.ean=new Ean(string);
	}
	public EanCanvas(BigInteger bigint){
		this();
		this.ean=new Ean(bigint);
	}
	public EanCanvas(BigInteger bigint, Dimension box){
		this();
		setDimensions(box);
		this.ean=new Ean(bigint);
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
	public static void main(String[] args) { //testing only
		EanCanvas eac = new EanCanvas("400246403540",new Dimension(150,200));
		Frame fra = new de.flg_informatik.utils.FLGFrame();
		fra.setTitle("EAN:"+eac.toString());
		fra.add(eac);
		fra.setVisible(true);
		fra.pack();
		
	}
	public int printAt(Graphics g, Dimension pos, Dimension box){
		offset=pos.getSize();
		int ret=setDimensions(box);
		paint(g);
		offset.setSize(here);
		return ret;
	}
	public void paint(Graphics g){
		startl.setSize(start);
		g.setFont(new Font("SansSerif", Font.BOLD, line8));
		g.setColor(Color.BLACK);
		g.drawChars(ean.getEanChars(), 0, 1, startl.width-line8, startl.height+line.height+line7);
		startl.width+=2*line1;
		paintZiffer(g,'@',0,startl);
		for (int i=1; i<digits; i++){
			g.setColor(Color.BLACK);
			if (i==7) paintZiffer(g,'|',0,startl);
			g.drawChars(ean.getEanChars(), i, 1, startl.width+line1, startl.height+line.height+line7);
			paintZiffer(g,ean.getEanChar(i),alphabeth13[ean.getEanChar(0)-'0'][i-1],startl);
		}
		g.setColor(Color.BLACK);
		paintZiffer(g,'@',0,startl);
		
		
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
	private void setLine(int x, int y){
		line.setSize(x,y);
		setDimensions();
	}
	private void setLine(Dimension xline){
		line.setSize(xline);
		setDimensions();
	}
	private int setDimensions(Dimension label){
		line.width = label.width / codewidth;
		line.height = label.height-2*randvert*line1-line7;
		this.label=label;
		setDimensions();
		return line.width;
	}
	private void setDimensions(){
		line1=line.width;
		line6=6*line1;
		line7=7*line1;
		line8=8*line1;
		label.height=line.height+2*randvert*line1+line7;
		code.setSize(codewidth*line1,label.height);
		start.setSize(9*line1+(label.width-code.width)/2+offset.width,randvert*line1+offset.height);
	}
	
}
