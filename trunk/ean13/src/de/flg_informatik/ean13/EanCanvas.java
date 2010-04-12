package de.flg_informatik.ean13;

import java.awt.*;
import java.math.BigInteger;

import de.flg_informatik.buecherverwaltung.Deb;


public class EanCanvas extends Canvas implements Ean13, de.flg_informatik.Etikett.PrintableEtikett {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int randvert = 10;
	private final int randhori = 4;
	private Dimension line = new Dimension(4,128); //testing only
	private final int codewidth =(2+digits)*7+2*randhori;
	private final char midchar = '9'+2;
	private final char edgechar = '9'+1;
	private Dimension here = new Dimension(0,0);
	private Dimension code = new Dimension(0,0);
	private Dimension start = new Dimension(0,0);
	private Dimension label = new Dimension(0,0);
	private Dimension offset = new Dimension(0,0);
	private String text;
	private int line1 = 0;
	private int eanheight;
	private double textscale=1.0;
	private int textheight;
	private Ean ean;
	private Dimension startl = new Dimension(0,0);
	
	private EanCanvas(){
		this.setBackground(Color.WHITE);
		//label.width=codewidth*line.width;
		//setDimensions();
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
	public EanCanvas(Ean ean, String text){
		this();
		this.text=text;
		this.ean=ean;
	}
	public EanCanvas(Ean ean, Dimension box, String text){
		this();
		this.text=text;
		setDimensions(box);
		this.ean=ean;
	}
	public EanCanvas(String string, Dimension box, String text){
		this();
		this.text=text;
		setDimensions(box);
		this.ean=new Ean(string);
	}
	public EanCanvas(Ean ean, Dimension pos, Dimension box, String text){
		this();
		this.text=text;
		this.here=pos;
		setDimensions(box);
		this.ean=ean;
	}
	public EanCanvas(String string, Dimension pos, Dimension box, String text){
		this();
		this.text=text;
		this.here=pos;
		setDimensions(box);
		this.ean=new Ean(string);
	}
	public EanCanvas(BigInteger bigint, String text){
		this();
		this.text=text;
		this.ean=new Ean(bigint);
	}
	public EanCanvas(BigInteger bigint, Dimension box, String text){
		this();
		this.text=text;
		setDimensions(box);
		this.ean=new Ean(bigint);
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
		EanCanvas eac = new EanCanvas("4003994155486",new Dimension(600,600), "Scheiﬂ Text!");
		Frame fra = new de.flg_informatik.utils.FLGFrame(){{
			setLayout(new GridLayout(1,2));
		}};
		fra.setTitle("EAN:"+eac.toString());
		fra.add(eac);
		EanCanvas eac2 = new EanCanvas("400246403000",new Dimension(150,200));
		fra.add(eac2);
		fra.setVisible(true);
		fra.pack();
		
	}
	public int printAt(Graphics g, Dimension pos, Dimension box){
		offset=pos.getSize();
		int ret=setDimensions(box);
		this.paint(g);
		offset.setSize(here);
		return ret;
	}
	public void paint(Graphics g){
		startl.setSize(start);
		g.setFont(new Font(Font.SERIF, Font.BOLD,  eanheight));
		g.setColor(Color.BLACK);
		g.drawChars(ean.getEanChars(), 0, 1, startl.width-eanheight*3/4, startl.height+line.height+eanheight);
		paintZiffer(g,edgechar,3,startl);
		for (int i=1; i<digits; i++){
			if (i==7) paintZiffer(g,midchar,1,startl); // just to be added in between #6 and #7 
			paintZiffer(g,ean.getEanChar(i),alphabeth13[ean.getEanChar(0)-'0'][i-1],startl); // plot a barcode
		}
		paintZiffer(g,edgechar,3,startl);
		if (text!=null){
			g.setFont(new Font("Serif", Font.PLAIN, textheight));
			g.setColor(Color.BLACK);
			g.drawString(text, 1, start.height+line.height+eanheight+textheight);
		}
		
	}
	private void paintZiffer(Graphics g,char zeichen, int variante, Dimension start){
		g.setColor(Color.BLACK);
		if (zeichen<='9'){ //print only digits, no edge or mid
			g.drawChars((new char[]{zeichen}) ,0,1, start.width+line1, start.height+line.height+eanheight );
		}
		switch (variante){
			case 1:
				toggleColor(g);
			case 3:
				for (int i=0; i<alphabeth[zeichen-'0'].length; i++){
					g.fillRect(start.width, start.height, line1*alphabeth[zeichen-'0'][i], line.height+(zeichen-'0')/10 * eanheight / 2 );
					start.width+=line1*alphabeth[zeichen-'0'][i];
					toggleColor(g);
				}
				break;
			case 2:
				toggleColor(g);
				for (int i=alphabeth[zeichen-'0'].length-1; i>=0; i--){
					g.fillRect(start.width, start.height, line1*alphabeth[zeichen-'0'][i], line.height+(zeichen-'0')/10 * eanheight / 2 );
					start.width+=line1*alphabeth[zeichen-'0'][i];
					toggleColor(g);
				}
				break;
			}
			
	}
	private void toggleColor(Graphics g){
		if (g.getColor()== Color.WHITE){
			g.setColor(Color.BLACK);
		}else{
			g.setColor(Color.WHITE);
		}
	}
	
	private int setDimensions(Dimension label){
		this.label=label;
		line.width = label.width / codewidth;
		line1=line.width;
		eanheight=10*line1;
		textheight=(int)(textscale*eanheight);
		line.height = label.height-2*randvert-eanheight ;
		if (text!=null){
			line.height-=textheight;
		}
		
		code.setSize(codewidth*line1,label.height);
		start.setSize(9*line1+(label.width-code.width)/2+offset.width
				,randvert+offset.height);
		return line.width;
	}
	
		
}
