package com.eber.simplejson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import com.eber.simplejson.delimiter.Delimiter;
import com.eber.simplejson.exception.ParsingException;

public abstract class JSONCollection<K, T> extends Delimiter {
	
	protected T collection;
	
	public JSONCollection(char start, char end) {
		super(start, end, true, true, false);
	}
	
	public JSONCollection(String str, char start, char end) throws ParsingException {
		super(str.substring(1, str.length() - 1), start, end, true, true, false);
	}
	
	public JSONCollection(InputStream in, char start, char end) throws ParsingException, IOException {
		super(in, start, end, true, true, false);
	}
	
	public JSONCollection(BufferedReader reader, char start, char end) throws ParsingException, IOException {
		super(reader, start, end, true, true, false);
	}
	
	public abstract Iterator<? extends Object> iterator();
	
	protected abstract T createCollection();
	
	public abstract int size();
	
	public abstract Object get(K key);
	
	public abstract Object remove(K key);
	
	public abstract boolean isEmpty();
	
	public boolean isNull(K key) {
		return get(key) == null;
	}
	
	public abstract void clear();
	
	public String getString(K key) {
		Object obj = get(key);
		return cast(obj, String.class);
	}
	
	public JSONArray getArray(K key) {
		Object obj = get(key);
		return cast(obj, JSONArray.class);
	}
	
	public JSONObject getObject(K key) {
		Object obj = get(key);
		return cast(obj, JSONObject.class);
	}
	
	public Number getNumber(K key) {
		Object obj = get(key);
		return cast(obj, Number.class);
	}
	
	public Integer getInt(K key) {
		Number n = getNumber(key);
		
		if (n != null) return n.intValue();
		else return null;
	}
	
	public Long getLong(K key) {
		Number n = getNumber(key);
		
		if (n != null) return n.longValue();
		else return null;
	}
	
	public Float getFloat(K key) {
		Number n = getNumber(key);
		
		if (n != null) return n.floatValue();
		else return null;
	}
	
	public Double getDouble(K key) {
		Number n = getNumber(key);
		
		if (n != null) return n.doubleValue();
		else return null;
	}
	
	public Short getShort(K key) {
		Number n = getNumber(key);
		
		if (n != null) return n.shortValue();
		else return null;
	}
	
	public Boolean getBoolean(K key) {
		Object obj = get(key);
		return cast(obj, Boolean.class);
	}
	
	public <V> boolean isInstance(K key, Class<V> vClass) {
		if (vClass != null && key != null && !isNull(key)) return vClass.isInstance(get(key));
		return false;
	}
	
	public T getCollection() {
		return collection == null ? collection = createCollection() : collection;
	}
	
	protected abstract String asString();
	
	@Override
	public String toString() {
		return asString();
	}
	
	private static <T> T cast(Object obj, Class<T> pClass) {
		
		if (obj != null) {
			if (pClass.isInstance(obj)) return pClass.cast(obj);
			else throw new ClassCastException(obj.getClass() + " can't be casted to " + pClass);
		}
		
		return null;
	}
	
}
