package com.eber.simplejson.delimiter;

import java.util.regex.Pattern;

import com.eber.simplejson.exception.ParsingException;

public final class StringValue extends Delimiter {

	private static final Pattern LITERAL_PATTERN = Pattern.compile("(\\\"|\\\\)");
	
	private String value;
	
	public StringValue() {
		super('"', '"', false, true, true);
	}
	
	public StringValue(String value) throws ParsingException {
		super(value, '"', '"', false, true, true);
	}

	@Override
	protected void addDelimiter(Delimiter delimiter) {}

	@Override
	public void processValue(String value) {
		this.value = value;
	}
	
	public String get() {
		return value;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	@Override
	public boolean acceptsEmpty() {
		return true;
	}
	
	public static final String quoteValue(String value) {
		synchronized(LITERAL_PATTERN) {			
			return LITERAL_PATTERN.matcher(value).replaceAll("\\\\$1");
		}
	}

}
