package com.eber.simplejson.exception;

public class ParsingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5078435640547563793L;

	public ParsingException(char start, char end) {
		super("The provided source string isn't wrapped by the delimiters " + start + " and " + end);
	}
	
	public ParsingException(char start) {
		super("The provided source input stream doesn't start with the delimiter " + start);
	}
	
	public ParsingException(String string) {
		super("Couldn't parse the string: " + string);
	}
	
	protected ParsingException() {}
	
}
