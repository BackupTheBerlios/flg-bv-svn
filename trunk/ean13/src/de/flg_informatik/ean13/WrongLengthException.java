package de.flg_informatik.ean13;

public class WrongLengthException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8693914898469675915L;

	public WrongLengthException() {
		super();
	}

	public WrongLengthException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public WrongLengthException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public WrongLengthException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
