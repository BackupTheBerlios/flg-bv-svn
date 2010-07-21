package de.flg_informatik.buecherverwaltung;

import java.math.BigInteger;

import de.flg_informatik.ean13.Ean;
import de.flg_informatik.utils.Version;

public interface BVConstants {
	final static int debug=0; // override per File with 
	// private static final int debug=1; //or more
	final static int debuglevel=20;	// override per File with 
	// private static final int debuglevel=1; //or more
	public final static BigInteger Book12=new BigInteger("200000000000");
	public final static BigInteger Class12=new BigInteger("210000000000");
	public final static BigInteger Customer12=new BigInteger("220000000000");
	public final static BigInteger Action12=new BigInteger("230000000000");
	public final static BigInteger subtract13=new BigInteger("6800000000000");
		// subtract from Bookworld ISBN 979X 978X to private EAN 299X 298X, does'nt change control digit
	public static final BigInteger ISBNNull12 = new BigInteger("978000000000");
	public static final Ean ISBNNullEan = new Ean(ISBNNull12);
	final static Version version=new Version(new int[]{1,2},"21-04-13 (USBScan) ");
	static final boolean docorrect=true; 

}
