package com.eber.simplejson.supplier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Supplier implements Iterator<Character> {

	private List<Character> markStore;
	private boolean isMarking = false;
	
	protected abstract Character nextChar();
	
	public Character next() {
		
		Character c = null;
		synchronized(this) {
			if (isMarking) markStore.add(c = nextChar());
			else if (markStore != null && !markStore.isEmpty()) return markStore.remove(0);
		}
		
		return c == null ? nextChar() : c;
	}
	
	public void mark() {
		if (markStore == null) markStore = new ArrayList<>();
		isMarking = true;
	}
	
	public void markOff() {
		if (isMarking) isMarking = false;
	}
	
	public void reset() {
		isMarking = false;
		markStore.clear();
	}

}
