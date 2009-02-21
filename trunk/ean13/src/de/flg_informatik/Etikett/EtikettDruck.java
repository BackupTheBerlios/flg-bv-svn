package de.flg_informatik.Etikett;
import de.flg_informatik.ean13.*;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.HashPrintRequestAttributeSet;


import java.awt.Graphics2D;

public class EtikettDruck implements Printable {
	
	
	/**
	 * Properties, sollten noch extern gespeichert werden, hier defaults
	 */
	
	private int zeilenanz=11;
	private int spaltenanz=4;
	private float lrmm = 12;
	private float rrmm = 8;
	private float ormm = 12;
	private float urmm = 8;
	private MediaSizeName defaultMediaSizeName = MediaSizeName.ISO_A4;
	private float defaultMediaSizeXmm = (float) 210;
	private float defaultMediaSizeYmm = (float) 297;
	
	/**
	 * ... können von der Kommandozeile / beim Aufruf überschrieben werden.
	 */
	
	
	@SuppressWarnings("unused")
	private int druckeraufloesung = 600;
	
	/**
	 * Ende defaults
	 */
	
	private MediaSizeName mediaSizeName=defaultMediaSizeName;
	private float mediaSizeXmm=defaultMediaSizeXmm;
	private float mediaSizeYmm=defaultMediaSizeYmm;
	private	double scale;
	private int proseite;
	private int seitenzahl;
	private double originx;
	private double originy;
	private double swidth;
	private double zheight;
	private PrintableEtikett[] etiketten;
	private HashPrintRequestAttributeSet pras=new HashPrintRequestAttributeSet();
	private PrinterJob pj;
	private final double inch = 25.4;
	private final int px = 72;

/** 
 * Interface-Teil
 */	
	
	public static int etikettenDruck(PrintableEtikett[] etiketten, int spaltenprozeile, int zeilenproblatt, float lrmm, float ormm, float rrmm, float urmm, MediaSizeName msn){
		
		EtikettDruck ed=new EtikettDruck();
		ed.zeilenanz=zeilenproblatt;
		ed.spaltenanz=spaltenprozeile;
		ed.lrmm=lrmm;
		ed.rrmm=rrmm;
		ed.ormm=ormm;
		ed.urmm=urmm;
		ed.mediaSizeName=msn;
		ed.initMargins();
		ed.initPrinterAttributes();
		return ed.drucken(etiketten);
		//toBeAdded
		
	}
public static void printAtrr(){
	}
public static int etikettenDruck(PrintableEtikett[] etiketten, int spaltenprozeile, int zeilenproblatt, float lrmm, float ormm, float rrmm, float urmm){
		EtikettDruck ed=new EtikettDruck();
		ed.zeilenanz=zeilenproblatt;
		ed.spaltenanz=spaltenprozeile;
		ed.lrmm=lrmm;
		ed.rrmm=rrmm;
		ed.ormm=ormm;
		ed.urmm=urmm;
		ed.initMargins();
		ed.initPrinterAttributes();
		return ed.drucken(etiketten);
		//toBeAdded
		
	}
	public static int etikettenDruck(PrintableEtikett[] etiketten, int spaltenprozeile, int zeilenproblatt){
		EtikettDruck ed=new EtikettDruck();
		ed.zeilenanz=zeilenproblatt;
		ed.spaltenanz=spaltenprozeile;
		return ed.druckeEtiketten(etiketten);
	}
	public static int etikettenDruck(PrintableEtikett[] etiketten){
		EtikettDruck ed=new EtikettDruck();
		return ed.druckeEtiketten(etiketten);
	}
	/**
	 * 
	 * @param etiketten jedes Element des Arrays wird an einen Platz auf dem Etikettenbogen gedruckt; siehe {@link de.flg_informatik.ean13.PrintableEtikett}
	 * @return Anzahl der gedruckten Seiten;
	 */
	
	public int druckeEtiketten(PrintableEtikett[] etiketten){
		try{
			if (pj.printDialog(pras)){
				//adjustMargins();
				return drucken(etiketten);
			}
		}catch(Exception e){
			System.out.println("druxkene: "+e);
		}
		return 0;
		
	}
	
	
	
	public int druckeEtiketten(PrintableEtikett[] etiketten, int spaltenprozeile, int zeilenproblatt){
		this.zeilenanz=zeilenproblatt;
		this.spaltenanz=spaltenprozeile;
		return druckeEtiketten(etiketten);
		
	}

	public int druckeEtiketten(PrintableEtikett[] etiketten, int spaltenprozeile, int zeilenproblatt, float lrmm, float ormm, float rrmm, float urmm){
		this.zeilenanz=zeilenproblatt;
		this.spaltenanz=spaltenprozeile;
		this.lrmm=lrmm;
		this.rrmm=rrmm;
		this.ormm=ormm;
		this.urmm=urmm;
		this.initMargins();
		this.initPrinterAttributes();
		return drucken(etiketten);
		
	}
	public int druckeEtiketten(PrintableEtikett[] etiketten, int spaltenprozeile, int zeilenproblatt, float lrmm, float ormm, float rrmm, float urmm, MediaSizeName msn){
		int seiten=0;
		this.zeilenanz=zeilenproblatt;
		this.spaltenanz=spaltenprozeile;
		this.lrmm=lrmm;
		this.rrmm=rrmm;
		this.ormm=ormm;
		this.urmm=urmm;
		this.defaultMediaSizeName=msn;
		this.initMargins();
		this.initPrinterAttributes();
		return seiten;
		//toBeAdded
		
	}
	private int drucken(PrintableEtikett[] etiketten){
		this.etiketten=etiketten;
		proseite=spaltenanz*zeilenanz;
		seitenzahl=(etiketten.length-1)/proseite+1;
		
		try{	
			if (pj.getPrintService()!=null){
				pj.print(pras);
				adjustPrinterAttributes();
			}else{
				System.out.println("Kein drucker verfügbar!");
			}
			
		}catch(PrinterException e){
			return 0;
		}
		
		return seitenzahl;
		
		
		
	}
	public EtikettDruck(){
		javax.print.attribute.Attribute[] attr;
		for (int i=(attr=this.pras.toArray()).length; i > 0; i--){
			System.out.println(attr[i].getCategory()+" "+attr[i].getName());
		}

	}
	/*
	 * most work is done here
	 * on my Linux machine (debian etch, jdk 1.6.0_02) getImageableY
	 * always is the same as getImageableX -- maybe a Bug?!  
	 */
	private void initMargins(){
		
		swidth=mmToP(mediaSizeXmm-(lrmm+rrmm))/spaltenanz;
		zheight=mmToP(mediaSizeYmm-(ormm+urmm))/zeilenanz;
		originx=mmToP(lrmm);
		originy=mmToP(ormm);
		System.out.println("initMargins: @: "+lrmm+", "+ormm+", size: "+swidth*spaltenanz+", "+zheight+zeilenanz+", Paper: "+mediaSizeXmm+", "+mediaSizeYmm+", Box: "+swidth+", "+zheight);
	}
	/*private void adjustMargins(){
		PageFormat pf=pj.getPageFormat(pras);
		swidth=pxToP(pf.getImageableWidth())/spaltenanz;
		zheight=pxToP(pf.getImageableHeight())/zeilenanz;
		mediaSizeXmm=pxToMm(pf.getWidth());
		mediaSizeYmm=pxToMm(pf.getHeight());
		lrmm=pxToMm(pf.getImageableX());
		ormm=pxToMm(pf.getImageableY()); // This one does not work properly on jdk 1.6.0_02
		rrmm=mediaSizeXmm-(lrmm+pxToMm(pf.getImageableWidth()));
		urmm=mediaSizeYmm-(ormm+pxToMm(pf.getImageableHeight()));
		originx=pxToP(pf.getImageableX());
		originy=pxToP(pf.getImageableY());
		System.out.println("adjustMargins: @: "+lrmm+"="+pf.getImageableX()+", "+ormm+"="+pf.getImageableY()+", size: "+pxToMm(pf.getImageableWidth())+", "+pxToMm(pf.getImageableHeight())+", Paper: "+mediaSizeXmm+", "+mediaSizeYmm+", Box: "+swidth+", "+zheight);
		
	}
	*/
	private void initPrinterAttributes(){
		//pj.getPageFormat(pras);
		pras.add(new MediaPrintableArea(lrmm,ormm,mediaSizeXmm-(rrmm+lrmm),mediaSizeYmm-(ormm+urmm),MediaPrintableArea.MM));
		pras.add(mediaSizeName);
	}
	
	private synchronized void adjustPrinterAttributes(){
		pras.add(new MediaPrintableArea(lrmm,ormm,mediaSizeXmm-(rrmm+lrmm),mediaSizeYmm-(ormm+urmm),MediaPrintableArea.MM));
	}
	
	/*
	 * convinience for changing units;
	 */
	
	private float pxToMm(double p){
		return (float)(p*inch/px);
	}
	private float pxToP(double p){
		return Math.round(p/scale);
	}
	
	/*
	 * not needed yet
	 */
	
	private float mmToP(double mm){
		return Math.round(mm/inch*px/scale);
	}
	
	
	private float pToMm(double p){
		return (float) (p*scale*inch/px);
	}
	/*
	 * end computing methods
	 */
	

	/* is not really public, never call directly
	 * (non-Javadoc)
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */
	public int print(Graphics g, PageFormat pageformat, int pageindex)
			throws PrinterException {
		if (pageindex>=seitenzahl){
			return Printable.NO_SUCH_PAGE;
		}
		
		Graphics2D g2    = (Graphics2D)g;
	    g2.scale(scale,scale);
	    for (int zeile=0;zeile<zeilenanz;zeile++){
		    for (int spalte=0;spalte<spaltenanz;spalte++){
		    	if (pageindex*proseite+zeile*spaltenanz+spalte<etiketten.length){
		    		etiketten[pageindex*proseite+zeile*spaltenanz+spalte].printAt(g2, new Dimension((int)Math.round(swidth*spalte+originx),(int)Math.round(zheight*zeile+originy)), new Dimension ((int)Math.round(swidth),(int)Math.round(zheight)));
		    	}
		    }
	    }    
		return Printable.PAGE_EXISTS;
	}
	/*
	 * Testcode
	 */
	
	public static void main(String[] args){
		
	Ean ean=new Ean("123456123456");
	etikettenDruck(new Ean[]{ean},4,2,(float)20,(float)50,(float)8,(float)8);
		}
}
