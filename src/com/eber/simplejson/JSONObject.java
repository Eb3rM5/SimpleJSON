package com.eber.simplejson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.eber.simplejson.delimiter.Delimiter;
import com.eber.simplejson.delimiter.StringValue;
import com.eber.simplejson.exception.ParsingException;

public class JSONObject extends JSONCollection<String, Map<String, Object>> {
	
	private String key = null;
	
	public JSONObject() {
		super('{', '}');
	}
	
	public <K, V> JSONObject(Map<K, V> map) {
		this();
		
		Iterator<Entry<K, V>> iterator = map.entrySet().iterator();
		Entry<K, V> entry;
		
		while(iterator.hasNext()) {
			entry = iterator.next();
			put(entry.getKey().toString(), entry.getValue());
		}
		
	}
	
	protected JSONObject(String str) throws ParsingException {
		super(str, '{', '}');
	}
	
	public JSONObject(InputStream in) throws ParsingException, IOException  {
		super(in, '{', '}');
	}
	
	public JSONObject(BufferedReader reader) throws ParsingException, IOException {
		super(reader, '{', '}');
	}

	@Override
	public synchronized String asString() {
		String str = "{";
		Map<String, Object> collection = getCollection();
		
		if (!collection.isEmpty()) {
			
			boolean first = true;
			
			for (String key : collection.keySet()) {
				if (!first) str += ",";
				else first = false;
				
				str += "\"" + StringValue.quoteValue(key) + "\":";
				Object obj = collection.get(key);
				
				if (obj == null) {
					str += "null";
					continue;
				}
				
				if (obj instanceof JSONObject || obj instanceof JSONArray || obj instanceof Number || obj instanceof Boolean) str += obj.toString();
				else str += "\"" + StringValue.quoteValue(obj.toString()) + "\"";
			}
			
		}
		str += "}";
		
		return str;
	}

	public Set<String> keySet(){
		return getCollection().keySet();
	}
	
	@Override
	public Iterator<Object> iterator() {
		return getCollection().values().iterator();
	}
	
	public Iterator<Entry<String, Object>> entryIterator(){
		return getCollection().entrySet().iterator();
	}
	
	public Iterator<String> keys(){
		return keySet().iterator();
	}

	@Override
	protected synchronized void addDelimiter(Delimiter delimiter) {
		
		if (key == null) {
			if(delimiter instanceof StringValue) key = ((StringValue)delimiter).get();
		} else {
			getCollection().put(key, delimiter instanceof StringValue ? ((StringValue)delimiter).get() : delimiter);
			key = null;
		}
		
	}

	@Override
	protected synchronized void processValue(String value) {
		
		if (key != null) {
			try {
				Object obj = toObject(value);
				getCollection().put(key, obj);
				key = null;
			} catch (ParsingException e) {
				e.printStackTrace();
				key = null;
				return;
			}
		}
		
	}

	@Override
	protected Map<String, Object> createCollection() {
		return new LinkedHashMap<>();
	}

	@Override
	public int size() {
		return getCollection().size();
	}
	
	@Override
	public synchronized void clear() {
		getCollection().clear();
	}
	
	public synchronized boolean contains(String key) {
		return getCollection().containsKey(key);
	}
	
	@Override
	public synchronized Object remove(String key) {
		return getCollection().remove(key);
	}
	
	@Override
	public synchronized Object get(String key) {
		return contains(key) ? getCollection().get(key) : null;
	}
	
	public synchronized Object put(String key, Object value) {
		if (value instanceof List) return getCollection().put(key, new JSONArray((List<?>)value));
		else if (value instanceof Map) return getCollection().put(key, new JSONObject((Map<?, ?>)value));
		else if (value instanceof Number) return getCollection().put(key, (Number)value);
		
		return getCollection().put(key, value);
	}
	
	public synchronized Object put(String key, String value) {
		return getCollection().put(key, value);
	}
	
	public synchronized Object put(String key, JSONArray value) {
		return getCollection().put(key, value);
	}
	
	public synchronized Object put(String key, JSONObject value) {
		return getCollection().put(key, value);
	}
	
	public synchronized Object put(String key, int value) {
		return getCollection().put(key, value);
	}
	
	public synchronized Object put(String key, long value) {
		return getCollection().put(key, value);
	}
	
	public synchronized Object put(String key, float value) {
		return getCollection().put(key, value);
	}
	
	public synchronized Object put(String key, double value) {
		return getCollection().put(key, value);
	}
	
	public synchronized Object put(String key, short value) {
		return getCollection().put(key, value);
	}

	@Override
	public synchronized boolean isEmpty() {
		return getCollection().isEmpty();
	}

}
