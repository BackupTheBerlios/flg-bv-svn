package de.flg_informatik.ean13;

import java.awt.Dimension;
import java.awt.Graphics;
import java.math.BigInteger;

public class Ean implements Ean13, de.flg_informatik.Etikett.PrintableEtikett{
	char[] ean = new char[digits];
	char[] maxean =new char[digits-1];
	EanCanvas me = null;
	BigInteger eanbigint = BigInteger.ZERO;
	public enum Result{
		ok,
		error,
		eanpriv,
		eanisbn,
		wronglength,
		checkfalse;
	}
	
	public Ean(String string){
		this(new BigInteger(string));
				
	}
	
	public Ean(BigInteger bigint){
		for (int i = digits-2; i>=0; i--){
			ean[i] = '0';
			maxean[i]='9';
		}
		debug(bigint);
		if (bigint.toString().length()!=13){
			if ((bigint.compareTo(new BigInteger(new String(maxean))) != 1) && (bigint.compareTo(BigInteger.ZERO)!=-1)){
				for (int i = digits-bigint.toString().length()-1; i < digits-1; i++){
					ean[i] = bigint.toString().charAt(i-(digits-bigint.toString().length()-1));
				}
				setCheck();
				this.eanbigint=bigint;
				debug(this);
			}
		}else{
			for (int i = digits-bigint.toString().length(); i < digits; i++){
				ean[i] = bigint.toString().charAt(i-(digits-bigint.toString().length()));
			}
			
			this.eanbigint=bigint;
			debug(this);
		}
		
	}
	
	void setCheck(){
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
		ean[digits-1]=Integer.toString(checksum).charAt(0);
		debug(ean.toString());
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
	public static Result[] checkEan(Ean ean){
		if(ean.ean.length!=13){
			return new Result[]{Result.error,Result.wronglength};
		}
		Ean newean = new Ean((new String(ean.toString()).substring(0, 12)));
		debug(newean.toString());
		debug(ean.toString());
		if(!ean.toString().equals(newean.toString())){
			debug("Check False");
			return new Result[]{Result.error,Result.checkfalse};
		}
		return new Result[]{Result.ok};
	}

	public int printAt(Graphics g, Dimension position, Dimension boxgroesse) {
		if (me == null){
			me = new EanCanvas(this);
		}
		me.printAt(g, position, boxgroesse);
		debug("printing"+this+"@:"+position+","+boxgroesse);
		return 0;
	}
	static private void debug(Object obj){
		//System.out.println(Ean.class+": "+ obj);
	}

}
