package de.flg_informatik.ean13;

import java.awt.*;
import java.math.BigInteger;

import javax.print.PrintException;
import javax.swing.JOptionPane;

import de.flg_informatik.buecherverwaltung.Deb;
/** 
 * @author nurh
 * @version 100413
 * Gives a visual impression of a EAN13 and
 * implements {@see de.flg_informatik.Etikett.PrintableEtikett}
 * (painting itself optimal in Graphics g at given pos in 
 * given max box size, but respecting readability giving bars only 
 * widths of integer pixel size.
 * 
 * @param ean no default
 * @param box defaults to minimal box (106,20) for ean13
 * @param text default null
 * @param textscale default 1.0
 */ 


public class EanCanvas extends Canvas implements Ean13, de.flg_informatik.Etikett.PrintableEtikett{
	
	
	private static final long serialVersionUID = 1L;
	
	private static final int codewidth =(digits-1)*7+9+6*2; //
	private static final char midchar = '9'+2;
	private static final char edgechar = '9'+1;
	
	
	
	private static int randvert = 0;
	private static int randhori = 0;
	private Dimension box = new Dimension(106,30);
	private Ean ean=null;
	
	private Dimension pos = new Dimension(0,0);
	private String[] text=null;
	private double textscale=1.0;
	
	
	private Dimension code = new Dimension(0,0);
	private Dimension start = new Dimension(0,0);
	private Dimension label = new Dimension(0,0);
	private Dimension line = new Dimension(0,0);

	
	private int eanheight;
	private int textheight;
	private Dimension startl = new Dimension(0,0);
	

	/** 
	 * 
	 * @param ean no default 
	 * @param box defaults to minimal box (106,20) for ean13
	 * @param text default null
	 * @param textscale default 1.0
	 */ 
	
	public EanCanvas(BigInteger bigint, Dimension box, String[] text, double textscale){
		init(new Ean(bigint), box, text, textscale);
	}
	public EanCanvas(Ean ean){
		init(ean, box, text, textscale);
	}
	public EanCanvas(BigInteger bigint){
		init(new Ean(bigint), box, text, textscale);
	}
	public EanCanvas(String string){
		init(new Ean(string), box, text, textscale);
	}
	public EanCanvas(Ean ean, Dimension box){
		init(ean, box, text, textscale);
	}
	public EanCanvas(BigInteger bigint, Dimension box){
		init(new Ean(bigint), box, text, textscale);
	}
	public EanCanvas(String string, Dimension box){
		init(new Ean(string), box, text, textscale);
	}
	public EanCanvas(Ean ean, Dimension box, String text){
		init(ean, box, new String[]{text}, textscale);
	}
	public EanCanvas(String string, Dimension box, String text){
		init(new Ean(string), box, new String[]{text}, textscale);
	}
	public EanCanvas(BigInteger bigint, Dimension box, String text){
		init(new Ean(bigint), box, new String[]{text}, textscale);
	}
	public EanCanvas(Ean ean, Dimension box, String[] text){
		init(ean, box, text, textscale);
	}
	public EanCanvas(String string, Dimension box, String[] text){
		init(new Ean(string), box, text, textscale);
	}
	public EanCanvas(BigInteger bigint, Dimension box, String[] text){
		init(new Ean(bigint), box, text, textscale);
	}
	public EanCanvas(Ean ean, Dimension box, String[] text, double textscale){
		init(ean, box, text, textscale);
	}
	public EanCanvas(String string, Dimension box, String[] text, double textscale){
		init(new Ean(string), box, text, textscale);
	}
		public EanCanvas(Ean ean, String text){
		init(ean, box, new String[]{text}, textscale);
	}
	public EanCanvas(String string, String text){
		init(new Ean(string), box, new String[]{text}, textscale);
	}
	public EanCanvas(BigInteger bigint, String text){
		init(new Ean(bigint), box, new String[]{text}, textscale);
	}
	public EanCanvas(Ean ean,  String[] text){
		init(ean, box, text, textscale);
	}
	public EanCanvas(String string, String[] text){
		init(new Ean(string), box, text, textscale);
	}
	public EanCanvas(BigInteger bigint,  String[] text){
		init(new Ean(bigint), box, text, textscale);
	}
	public EanCanvas(Ean ean, String text, double textscale){
		init(ean, box, new String[]{text}, textscale);
	}
	public EanCanvas(String string, String text, double textscale){
		init(new Ean(string), box, new String[]{text}, textscale);
	}
	public EanCanvas(BigInteger bigint, String text, double textscale){
		init(new Ean(bigint), box, new String[]{text}, textscale);
	}
	public EanCanvas(Ean ean,  String[] text, double textscale){
		init(ean, box, text, textscale);
	}
	public EanCanvas(String string,  String[] text, double textscale){
		init(new Ean(string), box, text, textscale);
	}
	public EanCanvas(BigInteger bigint, String[] text, double textscale){
		init(new Ean(bigint), box, text, textscale);
	}
	public static int getRandvert() {
		return randvert;
	}
	public static void setRandvert(int randvert) {
		EanCanvas.randvert = randvert;
	}
	public static int getRandhori() {
		return randhori;
	}
	public static void setRandhori(int randhori) {
		EanCanvas.randhori = randhori;
	}
	public Dimension getBox() {
		return box;
	}
	public void setBox(Dimension box) {
		this.box = box;
	}
	public String[] getText() {
		return text;
	}
	public void setText(String[] text) {
		this.text = text;
	}
	public double getTextscale() {
		return textscale;
	}
	public void setTextscale(double textscale) {
		this.textscale = textscale;
	}
	public int getTextheight() {
		return textheight;
	}
	public void setTextheight(int textheight) {
		this.textheight = textheight;
	}
	public Dimension getPreferredSize(){
		return new Dimension(label);
	}
	public String toString(){
		return ean.toString();
	}
	public Dimension getMinimumSize(){
		return new Dimension(label);
	}
	public Ean getEan(){
		return ean;
	}
	public int setEan(Ean ean){
		this.ean=ean;
		return setDimensions(label);
	}
	private int init(Ean ean, Dimension box, String[] text, double textscale){
		this.setBackground(Color.WHITE);
		this.ean=ean;
		this.text=text;
		this.textscale=textscale;
		return setDimensions(box);
	}

	public int printAt(Graphics g, Dimension pos, Dimension box) throws PrintException{
		this.pos=pos.getSize();
		if (setDimensions(box)<1){
			throw new PrintException("box size too small; is " + box.width +"x"+box.height +
					" should be >= "+ this.box.width+"x"+this.box.height);
		}
		this.paint(g);
		return 0;
		
	}
	public void paint(Graphics g){
		startl.setSize(start);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD,  eanheight));
		g.setColor(Color.BLACK);
		if (line.width<1){
			g.drawString("Zu klein für\n Barcode", pos.width, pos.height+eanheight);
			return ;
		}
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
			for (int i=0; i<text.length; i++)
			g.drawString(text[i], pos.width+randhori, start.height+line.height+eanheight+textheight*(i+1));
		}
		
	}
	private void paintZiffer(Graphics g,char zeichen, int variante, Dimension start){
		g.setColor(Color.BLACK);
		if (zeichen<='9'){ //print only digits, no edge or mid
			g.drawChars((new char[]{zeichen}) ,0,1, start.width+line.width, start.height+line.height+eanheight );
		}
		switch (variante){
			case 1:
				toggleColor(g);
			case 3:
				for (int i=0; i<alphabeth[zeichen-'0'].length; i++){
					g.fillRect(start.width, start.height, line.width*alphabeth[zeichen-'0'][i], line.height+(zeichen-'0')/10 * eanheight / 2 );
					start.width+=line.width*alphabeth[zeichen-'0'][i];
					toggleColor(g);
				}
				break;
			case 2:
				toggleColor(g);
				for (int i=alphabeth[zeichen-'0'].length-1; i>=0; i--){
					g.fillRect(start.width, start.height, line.width*alphabeth[zeichen-'0'][i], line.height+(zeichen-'0')/10 * eanheight / 2 );
					start.width+=line.width*alphabeth[zeichen-'0'][i];
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
		line.width = (label.width-2*randhori) / codewidth;
		eanheight=Math.max(8*line.width,10);
		textheight=(int)(textscale*eanheight);
		line.height = label.height-2*randvert-eanheight ;
		if (text!=null){
			line.height-=textheight*text.length;
		}
		code.setSize(codewidth*line.width,label.height);
		start.setSize((label.width-code.width)/2+8*line.width+pos.width
				,randvert+pos.height);
		return line.width;
	}
	@SuppressWarnings("serial")
	public static void main(String[] args) { //testing only
		EanCanvas eac = new EanCanvas("4003994155486",new Dimension(16,100));
		Frame fra = new de.flg_informatik.utils.FLGFrame(){{
			setLayout(new GridLayout(1,2));
		}};
		fra.setTitle("EAN:"+eac.toString());
		fra.add(eac);
		EanCanvas eac2 = new EanCanvas("400246403000");
		fra.add(eac2);
		fra.setVisible(true);
		fra.pack();
		
	}	
		
}
