package com.eber.simplejson.delimiter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;

import com.eber.simplejson.JSONArray;
import com.eber.simplejson.JSONObject;
import com.eber.simplejson.exception.ParsingException;
import com.eber.simplejson.supplier.BufferedReaderSupplier;
import com.eber.simplejson.supplier.InputStreamSupplier;
import com.eber.simplejson.supplier.StringSupplier;
import com.eber.simplejson.supplier.Supplier;

public abstract class Delimiter {

	private static final char ESCAPE = '\\', COMMA = ',', COLON = ':', SPACE = ' ',
								ES = '\n', ES_2 = '\r', ES_3 = '\b', ES_4 = '\t', UNICODE = 'u';
	
	private char start, end;
	private boolean acceptsDelimiters, acceptsEscapes, acceptsSpace;
	private static NumberFormat NUMBER = NumberFormat.getInstance();
	
	protected Delimiter(BufferedReader reader, char start, char end, boolean acceptsDelimiters, boolean acceptsEscapes, boolean acceptsSpace) throws ParsingException, IOException  {
		this(start, end, acceptsDelimiters, acceptsEscapes, acceptsSpace);
		processString(new BufferedReaderSupplier(start, reader));
	}
	
	protected Delimiter(InputStream in, char start, char end, boolean acceptsDelimiters, boolean acceptsEscapes, boolean acceptsSpace) throws ParsingException, IOException  {
		this(start, end, acceptsDelimiters, acceptsEscapes, acceptsSpace);
		processString(new InputStreamSupplier(start, in));
		in.close();
	}
	
	protected Delimiter(String str, char start, char end, boolean acceptsDelimiters, boolean acceptsEscapes, boolean acceptsSpace) throws ParsingException {
		str = str.trim();
		if (str.indexOf(start) == -1 && str.indexOf(end) == -1) throw new ParsingException(start, end);
		
		this.start = start;
		this.end = end;
		this.acceptsDelimiters = acceptsDelimiters;
		this.acceptsEscapes = acceptsEscapes;
		this.acceptsSpace = acceptsSpace;
		
		processString(new StringSupplier(str));
	}
	
	protected Delimiter(char start, char end, boolean acceptsDelimiters, boolean acceptsEscapes, boolean acceptsSpace) {
		this.start = start;
		this.end = end;
		this.acceptsDelimiters = acceptsDelimiters;
		this.acceptsEscapes = acceptsEscapes;
		this.acceptsSpace = acceptsSpace;
	}
	
	public char getStart() {
		return start;
	}
	
	public char getEnd() {
		return end;
	}
	
	public boolean isStart(char c) {
		return c == start;
	}
	
	public boolean isEnd(char c) {
		return c == end;
	}
	
	protected abstract void addDelimiter(Delimiter delimiter);
	
	protected abstract void processValue(String value);
	
	public boolean acceptsEmpty() {
		return false;
	}
	
	protected void processString(Supplier supplier) {
		
		String value = "";
		boolean isEscape = false;
		
		while(supplier.hasNext()) {
			
			char c = supplier.next();
			
			if (c == ES || c == ES_2 || c == ES_3 || c == ES_4) continue;
			else if (isEscape) {
				
				if (!acceptsDelimiters && c == UNICODE) {
					
					supplier.mark();
					String code = "";
					for (int i = 0; i < 4; i++) code += supplier.next();
					
					try {
						int integer = Integer.parseInt(code, 16);
						
						System.out.println(integer);
						value += "" + (char)integer;
						supplier.reset();
					} catch (Exception e) {
						supplier.markOff();
						isEscape = false;
						continue;
					}
					
				} else value += c;
				
				isEscape = false;
				
			} else {
				if (acceptsEscapes && c == ESCAPE) {
					isEscape = true;
					continue;
				} else if (!acceptsSpace && c == SPACE) {
					continue;
				} else if (isEnd(c)) {
					if (!value.isEmpty() || acceptsEmpty()) {
						processValue(value);
						value = "";
					}
					
					break;
				} else if (acceptsDelimiters) {
					
					Delimiter d;
					if ((d = createDelimiter(c)) != null){
						value = "";
						
						d.processString(supplier);
						addDelimiter(d);
						continue;
					} else if (c == COMMA || c == COLON) {
						if (!value.isEmpty()) processValue(value);
						value = "";
						continue;
					}
					
				}
				
				value += c;
				
			}
			
		}
		
	}
	
	protected Delimiter createDelimiter(char delimiter) {
		
		switch (delimiter) {
			default:
				return null;
			case '"':
				return new StringValue();
			case '[':
				return new JSONArray();
			case '{':
				return new JSONObject();
		}
		
	}
	
	public static final Object toObject(String valueString) throws ParsingException {
		
		if (valueString.equalsIgnoreCase("null")) return null;
		else if (valueString.equalsIgnoreCase("true")) return true;
		else if (valueString.equalsIgnoreCase("false")) return false;
		else {
			try {
				valueString = valueString.replaceAll("\\.", ",");
				Number n = NUMBER.parse(valueString);
				return n;
			} catch (ParseException e) {
				throw new ParsingException(valueString);
			}
		}
		
	}
	
}
