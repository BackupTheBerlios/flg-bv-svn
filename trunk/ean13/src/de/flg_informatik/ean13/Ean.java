package de.flg_informatik.ean13;

import java.awt.Dimension;
import java.awt.Graphics;
import java.math.BigInteger;
import java.util.jar.JarException;

import javax.print.PrintException;

public class Ean implements Ean13, de.flg_informatik.Etikett.PrintableEtikett{
	char[] ean = new char[digits];
	char[] maxean =new char[digits-1];
	EanCanvas me = null;
	BigInteger eanbigint = BigInteger.ZERO;
	/*public enum Result{
		ok,
		error,
		eanpriv,
		eanisbn,
		wronglength,
		checkfalse;
	}
	*/
	public static Ean getEan(String string){
		try {
			return new Ean(string);
		} catch (WrongCheckDigitException e) {
			// TODO Auto-generated catch block
			
		} catch (WrongLengthException e) {
			// TODO Auto-generated catch block
			
		}
		return null;
	}
	public static Ean getEan(BigInteger bigint){
		try {
			return new Ean(bigint);
		} catch (WrongCheckDigitException e) {
			throw new InternalError(e.getLocalizedMessage());
		} catch (WrongLengthException e) {
			throw new InternalError(e.getLocalizedMessage());
		}
		//return null;
	}
	public Ean(String string) throws WrongCheckDigitException, WrongLengthException{
		try{
			makeEan(new BigInteger(string));
		}catch(java.lang.NumberFormatException e){
			throw new InternalError("String is not convertable to a BigInteger!");			
		}catch(NullPointerException e){
			throw new InternalError("null-String is not convertable to a BigInteger!");
		}
				
	}
	
	public Ean(BigInteger bigint) throws WrongCheckDigitException, WrongLengthException{
		makeEan(bigint);
	}
	private void makeEan(BigInteger bigint) throws WrongCheckDigitException, WrongLengthException{
		for (int i = digits-2; i>=0; i--){
			ean[i] = '0';
			maxean[i]='9';
		}
		ean[digits-1] = '0';
		debug("conv "+bigint);
		if (bigint.toString().length()>digits){
			throw new WrongLengthException("Ean will be truncated, (> "+digits+" digits)!");
		}
		if (bigint.toString().length()!=digits){
			if ((bigint.compareTo(new BigInteger(new String(maxean))) != 1) && (bigint.compareTo(BigInteger.ZERO)!=-1)){
				for (int i = digits-bigint.toString().length()-1; i < digits-1; i++){
					ean[i] = bigint.toString().charAt(i-(digits-bigint.toString().length()-1));
				}
				setCheck();
				this.eanbigint=new BigInteger(new String(ean));
				debug(this);
			}
		}else{
			for (int i = digits-bigint.toString().length(); i < digits; i++){
				ean[i] = bigint.toString().charAt(i-(digits-bigint.toString().length()));
			}
			checkEan(this);
			this.eanbigint=bigint;
			
		}
		debug("end "+this);
	}
	
	private void setCheck(){
		ean[digits-1]=getCheck();
		debug(ean.toString());
	}
	
	private char getCheck(){
		int checksum=0;
		for (int i=0;i<12;i++){
			if (i%2==0){
				checksum+=(ean[i]-'0');
			}else{
				checksum+=((ean[i]-'0')*3);
			}
			checksum%=10;
		}
		checksum=(10-checksum)%10;
		return Integer.toString(checksum).charAt(0);
	}
	
	
	public char[] getEanChars(){
		return ean;
	}
	public char getEanChar(int index){
		return ean[index];
	}
	public String toString(){
		return(new String(ean));
	}
	public BigInteger getEan(){
		return eanbigint;
	}
	public static boolean checkEan(Ean ean) throws WrongCheckDigitException, WrongLengthException{
		
		if(ean.ean.length!=13){
			throw new WrongLengthException("Ean has length of "+ean.ean.length+", expected was 13.");
		}
		if((ean.toString().charAt(12)!=ean.getCheck())){
			throw new WrongCheckDigitException();
		}
		return true;
	}

	public int printAt(Graphics g, Dimension position, Dimension boxgroesse) 
		throws PrintException{
		if (me == null){
			me = new EanCanvas(this);
		}
		me.printAt(g, position, boxgroesse);
		debug("printing"+this+"@:"+position+","+boxgroesse);
		return 0;
	}
	static private void debug(Object obj){
		 System.out.println(Ean.class+": "+ obj);
	}

}
