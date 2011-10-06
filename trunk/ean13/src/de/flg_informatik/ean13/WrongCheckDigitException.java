package de.flg_informatik.ean13;

public class WrongCheckDigitException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public WrongCheckDigitException() {
		super("You gave 13 digits, but the 13th was not the right check digit!");
	}	

}
