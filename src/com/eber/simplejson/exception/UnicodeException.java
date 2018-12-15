package com.eber.simplejson.exception;

public class UnicodeException extends ParsingException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1037801927539351325L;

	public UnicodeException(String str) {
		super("Couldn't convert the following string into an unicode char: " + str);
	}
	
}
