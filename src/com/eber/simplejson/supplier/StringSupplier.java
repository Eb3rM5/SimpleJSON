package com.eber.simplejson.supplier;

import java.util.Iterator;

public final class StringSupplier extends Supplier implements Iterator<Character> {

	private int pos;
	private String str;
	
	public StringSupplier(String str) {
		pos = 0;
		this.str = str;
	}
	
	@Override
	protected Character nextChar() {
		
		if (hasNext()) {
			char c = str.charAt(pos);
			pos++;
			return c;
		}
		
		throw new NullPointerException("Out of elements");
	}

	@Override
	public boolean hasNext() {
		return pos < str.length();
	}

}
