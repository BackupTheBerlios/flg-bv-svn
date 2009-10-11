package de.flg_informatik.Etikett;
import de.flg_informatik.ean13.*; //needed for testing only
import de.flg_informatik.utils.FLGProperties;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.PrintService;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.HashPrintRequestAttributeSet;


import java.awt.Graphics2D;
import java.io.File;
import java.util.Properties;

public class EtikettDruck implements Printable {
	
	
	/**
	 * Properties, extern gespeichert s. infilename="etiketten.xml", "etiketten.default.xml", 
	 * default-defaults fest eincodiert
	 * Ränder und Page-Style veränderbar durch Druckdialog
	 * Ränder und Zeilen Spaltenzahl können beim Aufruf überschrieben werden.
	 * alle Aurufe auch mit etikettoffset = Anzahl leerer Etiketten von oben links zum
	 * bedrucken angefangener Bögen
	 */
	
	private static String infilename="etiketten.xml"; //should be overridden
	private static String defaultfilename="etiketten.default.xml"; //should be overridden
	private static String significantstring=".EtikettenV1"; //should be overridden
	private MediaSizeName mediaSizeName = MediaSizeName.ISO_A4; // this would be a tricky typecast
	
	
	private int zeilenproseite;
	private int spaltenprozeile;
	private float randlinksmm;
	private float randrechtsmm;
	private float randobenmm;
	private float randuntenmm;
	private float mediaSizeXmm;
	private float mediaSizeYmm;
	
	private int etikettoffset=0;
	private	double scale=1;
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
	public static void setPropertyFileStrings (String infilename, String defaultfilename, String significantstring){
		EtikettDruck.infilename=infilename;
		EtikettDruck.defaultfilename=defaultfilename;
		EtikettDruck.significantstring=significantstring;
	}
	public static int etikettenDruck(PrintableEtikett[] etiketten, int spaltenprozeile, int zeilenproblatt, float lrmm, float ormm, float rrmm, float urmm, MediaSizeName msn){
		return etikettenDruck(etiketten, spaltenprozeile, zeilenproblatt, lrmm, ormm, rrmm, urmm, msn, 0);
	}
	public static int etikettenDruck(PrintableEtikett[] etiketten, int spaltenprozeile, int zeilenproblatt, float lrmm, float ormm, float rrmm, float urmm, MediaSizeName msn, int etikettoffset){
		EtikettDruck ed=new EtikettDruck();
		ed.etikettoffset=etikettoffset;
		ed.zeilenproseite=zeilenproblatt;
		ed.spaltenprozeile=spaltenprozeile;
		ed.randlinksmm=lrmm;
		ed.randrechtsmm=rrmm;
		ed.randobenmm=ormm;
		ed.randuntenmm=urmm;
		ed.mediaSizeName=msn;
		ed.initMargins();
		ed.initPrinterAttributes();
		return ed.drucken(etiketten);
	}
	public static int etikettenDruck(PrintableEtikett[] etiketten, int spaltenprozeile, int zeilenproblatt, float lrmm, float ormm, float rrmm, float urmm){
		return etikettenDruck(etiketten, spaltenprozeile, zeilenproblatt, lrmm, ormm, rrmm, urmm, 0);
	}
	public static int etikettenDruck(PrintableEtikett[] etiketten, int spaltenprozeile, int zeilenproblatt, float lrmm, float ormm, float rrmm, float urmm, int etikettoffset){
		EtikettDruck ed=new EtikettDruck();
		ed.etikettoffset=etikettoffset;
		ed.zeilenproseite=zeilenproblatt;
		ed.spaltenprozeile=spaltenprozeile;
		ed.randlinksmm=lrmm;
		ed.randrechtsmm=rrmm;
		ed.randobenmm=ormm;
		ed.randuntenmm=urmm;
		ed.initMargins();
		ed.initPrinterAttributes();
		return ed.drucken(etiketten);
				
	}
	
	public static int etikettenDruck(PrintableEtikett[] etiketten, int spaltenprozeile, int zeilenproblatt){
		return etikettenDruck(etiketten, spaltenprozeile, zeilenproblatt, 0);	
	}
	
	public static int etikettenDruck(PrintableEtikett[] etiketten, int spaltenprozeile, int zeilenproblatt, int etikettoffset){
		EtikettDruck ed=new EtikettDruck();
		ed.etikettoffset=etikettoffset;
		ed.zeilenproseite=zeilenproblatt;
		ed.spaltenprozeile=spaltenprozeile;
		return ed.druckeEtiketten(etiketten);
	}
	
	public static int etikettenDruck(PrintableEtikett[] etiketten){
		return etikettenDruck(etiketten, 0);
	}
	public static int etikettenDruck(PrintableEtikett[] etiketten, int etikettoffset){
		EtikettDruck ed=new EtikettDruck();
		ed.etikettoffset=etikettoffset;
		return ed.druckeEtiketten(etiketten);
	}
	/**
	 * 
	 * @param etiketten jedes Element des Arrays wird an einen Platz auf dem Etikettenbogen gedruckt; siehe {@link de.flg_informatik.ean13.PrintableEtikett}
	 * @return Anzahl der gedruckten Seiten;
	 */
	
	public int druckeEtiketten(PrintableEtikett[] etiketten){
		initPrinterAttributes();
		try{
			if (pj.printDialog(pras)){
				adjustMargins();
				return drucken(etiketten);
			}else{
				return 0;
			}
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	
	
	public int druckeEtiketten(PrintableEtikett[] etiketten, int spaltenprozeile, int zeilenproblatt){
		this.zeilenproseite=zeilenproblatt;
		this.spaltenprozeile=spaltenprozeile;
		return druckeEtiketten(etiketten);
		
	}

	public int druckeEtiketten(PrintableEtikett[] etiketten, int spaltenprozeile, int zeilenproblatt, float lrmm, float ormm, float rrmm, float urmm){
		this.zeilenproseite=zeilenproblatt;
		this.spaltenprozeile=spaltenprozeile;
		this.randlinksmm=lrmm;
		this.randrechtsmm=rrmm;
		this.randobenmm=ormm;
		this.randuntenmm=urmm;
		this.initMargins();
		this.initPrinterAttributes();
		return drucken(etiketten);
		
	}
	public int druckeEtiketten(PrintableEtikett[] etiketten, int spaltenprozeile, int zeilenproblatt, float lrmm, float ormm, float rrmm, float urmm, MediaSizeName msn){
		int seiten=0;
		this.zeilenproseite=zeilenproblatt;
		this.spaltenprozeile=spaltenprozeile;
		this.randlinksmm=lrmm;
		this.randrechtsmm=rrmm;
		this.randobenmm=ormm;
		this.randuntenmm=urmm;
		this.mediaSizeName=msn;
		this.initMargins();
		this.initPrinterAttributes();
		return seiten;
		
		
	}
	
	
	private int drucken(PrintableEtikett[] etiketten){
		this.etiketten=etiketten;
		proseite=spaltenprozeile*zeilenproseite;
		etikettoffset=etikettoffset%proseite;
		seitenzahl=(etiketten.length-1+etikettoffset)/proseite+1;
		debug("drucken");
		javax.print.attribute.Attribute[] attr;
		for (int i=(attr=this.pras.toArray()).length-1; i >= 0; i--){
			debug(attr[i].getCategory()+" "+attr[i].getName());
			debug(attr[i].getCategory()+" "+attr[i]);
		}
		
		try{	
			if (pj.getPrintService()!=null){
				pj.setJobName("Etikettendruck");
				pj.setPrintable(this);
				pj.print(pras);
				adjustPrinterAttributes();
			}else{
				debug("Kein drucker verfügbar!");
			}
			
		}catch(PrinterException e){
			e.printStackTrace();
			return 0;
		}
		
		return seitenzahl;
		
		
		
	}
	private EtikettDruck(){
		
		PrintService[] list;
		Properties properties =null;

		if ((properties=new FLGProperties(properties,infilename, new File(defaultfilename), significantstring).getProperties())==null){
			properties=PropertiesDialog.showSettingsDialog(properties,"etiketten.xml", new File(defaultfilename), significantstring);
		}
		zeilenproseite=Integer.valueOf(properties.getProperty("zeilenproseite","1"));
		spaltenprozeile=Integer.valueOf(properties.getProperty("spaltenprozeile","1"));
		randlinksmm = Float.valueOf(properties.getProperty("randlinksmm","2.54"));
		randrechtsmm = Float.valueOf(properties.getProperty("randrechtsmm","2.54"));
		randobenmm = Float.valueOf(properties.getProperty("randobenmm","2.54"));
		randuntenmm = Float.valueOf(properties.getProperty("randuntenmm","2.54"));
		mediaSizeXmm = Float.valueOf(properties.getProperty("mediaSizeXmm","210"));;
		mediaSizeYmm = Float.valueOf(properties.getProperty("mediaSizeYmm","297"));;
		list=PrinterJob.lookupPrintServices();
		debug(list.length);
		for(int i=0; i<list.length;i++){
			debug(list[i]);
		}
		
		pj=PrinterJob.getPrinterJob();
		javax.print.attribute.Attribute[] attr;
		for (int i=(attr=this.pras.toArray()).length; i > 0; i--){
			debug(attr[i].getCategory()+" "+attr[i].getName());
		}

	}
	/*
	 * most work is done here
	 * on my Linux machine (debian etch, jdk 1.6.0_02) getImageableY
	 * always is the same as getImageableX -- maybe a Bug?! 
	 * not longer true on jdk 1.6.0_12
	 */
	private void initMargins(){
		
		swidth=mmToP(mediaSizeXmm-(randlinksmm+randrechtsmm))/spaltenprozeile;
		zheight=mmToP(mediaSizeYmm-(randobenmm+randuntenmm))/zeilenproseite;
		originx=mmToP(randlinksmm);
		originy=mmToP(randobenmm);
		debug(spaltenprozeile+" "+swidth);
		debug("initMargins: @: "+randlinksmm+", "+randobenmm+", size: "+swidth*spaltenprozeile+", "+zheight+zeilenproseite+", Paper: "+mediaSizeXmm+", "+mediaSizeYmm+", Box: "+swidth+", "+zheight);
	}
	private void adjustMargins(){
		PageFormat pf=pj.getPageFormat(pras);
		debug(pf.getImageableWidth());
		debug(pf.getImageableHeight());
		swidth=pxToP(pf.getImageableWidth())/spaltenprozeile;
		zheight=pxToP(pf.getImageableHeight())/zeilenproseite;
		mediaSizeXmm=pxToMm(pf.getWidth());
		mediaSizeYmm=pxToMm(pf.getHeight());
		randlinksmm=pxToMm(pf.getImageableX());
		randobenmm=pxToMm(pf.getImageableY()); // This one does not work properly on jdk 1.6.0_02, works on jdk 1.6.0_12
		randrechtsmm=mediaSizeXmm-(randlinksmm+pxToMm(pf.getImageableWidth()));
		randuntenmm=mediaSizeYmm-(randobenmm+pxToMm(pf.getImageableHeight()));
		originx=pxToP(pf.getImageableX());
		originy=pxToP(pf.getImageableY());
		debug("adjustMargins: @: "+randlinksmm+"="+pf.getImageableX()+", "+randobenmm+"="+pf.getImageableY()+", size: "+pxToMm(pf.getImageableWidth())+", "+pxToMm(pf.getImageableHeight())+", Paper: "+mediaSizeXmm+", "+mediaSizeYmm+", Box: "+swidth+"= "+ pToMm(swidth)+", "+zheight);
		
	}
	
	private void initPrinterAttributes(){
		//pj.getPageFormat(pras);
		pras.add(new MediaPrintableArea(randlinksmm,randobenmm,mediaSizeXmm-(randrechtsmm+randlinksmm),mediaSizeYmm-(randobenmm+randuntenmm),MediaPrintableArea.MM));
		pras.add(mediaSizeName);
	}
	
	private synchronized void adjustPrinterAttributes(){
		pras.add(new MediaPrintableArea(randlinksmm,randobenmm,mediaSizeXmm-(randrechtsmm+randlinksmm),mediaSizeYmm-(randobenmm+randuntenmm),MediaPrintableArea.MM));
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
		debug("print");
		
		Graphics2D g2    = (Graphics2D)g;
		// debug only g2.fillOval(100,100,100,100); 
		g2.scale(scale,scale);
	    for (int zeile=0;zeile<zeilenproseite;zeile++){
		    for (int spalte=0;spalte<spaltenprozeile;spalte++){
		    	if (!(pageindex*proseite+zeile*spaltenprozeile+spalte<etikettoffset)){
		    		if (pageindex*proseite+zeile*spaltenprozeile+spalte<etiketten.length+etikettoffset){
		    		etiketten[pageindex*proseite+zeile*spaltenprozeile+spalte-etikettoffset].printAt(g2, new Dimension((int)Math.round(swidth*spalte+originx),(int)Math.round(zheight*zeile+originy)), new Dimension ((int)Math.round(swidth),(int)Math.round(zheight)));
		    		}
		    	}else{ //offset etikett
		    	}	
		    	
		    }
	    }
	       
		return Printable.PAGE_EXISTS;
	}
	public static void debug(Object o){
		System.out.println(EtikettDruck.class+":"+o);
	}
	/**
	 * Testcode
	 * needs import of de.flg_informatik.ean13.*;
	*/
	public static void main(String[] args){
		
	Ean ean=new Ean("123456123456");
	// etikettenDruck(new Ean[]{ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean},4,14,(float)8,(float)8,(float)8,(float)8);
	etikettenDruck(new Ean[]{ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean,ean},4,14,6);
		}
	/*
	*/
}
