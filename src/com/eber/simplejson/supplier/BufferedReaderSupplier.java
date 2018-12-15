package com.eber.simplejson.supplier;

import java.io.BufferedReader;
import java.io.IOException;

import com.eber.simplejson.exception.ParsingException;

public class BufferedReaderSupplier extends Supplier {

	private Character nextChar;
	private char[] buffer = new char[1];
	
	protected BufferedReader reader;
	
	protected BufferedReaderSupplier() {}
	
	public BufferedReaderSupplier(char startDelimiter, BufferedReader reader) throws ParsingException, IOException {
		setReader(reader, startDelimiter);
	}
	
	protected final void setReader(BufferedReader reader, char startDelimiter) throws IOException, ParsingException {
		if (reader != null) {
			this.reader = reader;
			if (reader.read() != startDelimiter) throw new ParsingException(startDelimiter);
			
			nextChar = push();
		}
	}
	
	@Override
	protected Character nextChar() {
		
		if (hasNext()) {
			
			char character = nextChar;
			
			try {
				nextChar = push();
			} catch (Exception e) {
				nextChar = null;
			}
			
			return character;
		}
		
		return null;
	}
	
	@Override
	public boolean hasNext() {
		boolean hasNext = nextChar != null;
		return hasNext;
	}
	
	private Character push() throws IOException {
		if (reader.read(buffer) != -1) {
			char c = buffer[0];
			return c;			
		}
		
		reader.close();
		return null;
		
	}
	
	

}
