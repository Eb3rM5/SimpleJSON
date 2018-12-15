package com.eber.simplejson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.eber.simplejson.delimiter.Delimiter;
import com.eber.simplejson.delimiter.StringValue;
import com.eber.simplejson.exception.ParsingException;

public class JSONArray extends JSONCollection<Integer, List<Object>> implements Iterable<Object> {
	
	public JSONArray() {
		super('[', ']');
	}
	
	protected JSONArray(String str) throws ParsingException {
		super(str, '[', ']');
	}
	
	public JSONArray(InputStream in) throws ParsingException, IOException {
		super(in, '[', ']');
	}
	
	public <T> JSONArray(List<T> list) {
		this();
		
		Iterator<T> iterator = list.iterator();
		
		while(iterator.hasNext()) add(iterator.next());
	}
	
	public <T> List<T> toList(Class<T> listClass, boolean clearBefore){
		return toList(new LinkedList<>(), listClass, clearBefore, true);
	}
	
	public <T> List<T> toList(Class<T> listClass){
		return toList(listClass, false);
	}
	
	public <T> List<T> toList(List<T> list, Class<T> listClass){
		return toList(list, listClass, false, true);
	}
	
	public <T> List<T> toList(List<T> list, Class<T> listClass, boolean clearBefore, boolean allowRepeating){
		
		if (clearBefore && !list.isEmpty()) list.clear();
		if (!isEmpty()) {			
			
			for (int i = 0; i < size(); i++) {
				
				Object obj = get(i);
				
				if (!listClass.isInstance(obj)) for (Method m : getClass().getSuperclass().getMethods()) {
					
					if (m.getName().startsWith("get") && m.getParameterCount() == 1 && listClass.isAssignableFrom(m.getReturnType())) {
						try {
							obj = m.invoke(this, i);
							break;
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
							break;
						}
					}
					
					obj = null;
					
					continue;
				}
				
				if (obj != null) {
					T casted = listClass.cast(obj);
					if (allowRepeating || !list.contains(casted)) list.add(casted);
				}
				
			}
			
		}
			
		
		return list;
		
	}
	
	@Override
	protected synchronized String asString() {
		
		String str = "[";
		List<Object> collection = getCollection();
		if (!collection.isEmpty()) for (int i = 0; i < size(); i++) {
			Object obj = collection.get(i);
			if (i > 0) str += ",";
			
			if (obj == null) {
				str += "null";
				continue;
			}
			
			if (obj instanceof JSONObject || obj instanceof JSONArray || obj instanceof Number || obj instanceof Boolean) str += obj.toString();
			else str += "\"" + StringValue.quoteValue(obj.toString()) + "\"";
			
		}
		
		str += "]";
		
		return str;
	}

	@Override
	public Object remove(Integer key) {
		return getCollection().remove(key);
	}
	
	@Override
	public Object get(Integer index) {
		List<Object> collection = getCollection();
		return index >= 0 && collection.size() > index ? collection.get(index) : null;
	}
	
	public synchronized boolean add(Object obj) {
		if (obj instanceof List) return getCollection().add(new JSONArray((List<?>)obj));
		else if (obj instanceof Map) return getCollection().add(new JSONObject((Map<?, ?>)obj));
		else if (obj instanceof Number) return getCollection().add((Number)obj);
		
		return getCollection().add(obj);
	}
	
	public synchronized boolean add(String value) {
		return getCollection().add(value);
	}
	
	public synchronized boolean add(JSONArray value) {
		return getCollection().add(value);
	}
	
	public synchronized boolean add(JSONObject value) {
		return getCollection().add(value);
	}
	
	public synchronized boolean add(int value) {
		return getCollection().add(value);
	}
	
	public synchronized boolean add(long value) {
		return getCollection().add(value);
	}
	
	public synchronized boolean add(float value) {
		return getCollection().add(value);
	}
	
	public synchronized boolean add(double value) {
		return getCollection().add(value);
	}
	
	public synchronized boolean add(short value) {
		return getCollection().add(value);
	}
	
	public synchronized boolean contains(Object obj) {
		return getCollection().contains(obj);
	}
	
	public synchronized boolean remove(Object obj) {
		return getCollection().remove(obj);
	}
	
	@Override
	public int size() {
		return getCollection().size();
	}
	
	@Override
	public synchronized void clear() {
		getCollection().clear();
	}

	@Override
	public Iterator<Object> iterator() {
		return getCollection().iterator();
	}

	@Override
	protected synchronized void addDelimiter(Delimiter delimiter) {
		getCollection().add(delimiter instanceof StringValue ? ((StringValue)delimiter).get() : delimiter);
	}

	@Override
	protected synchronized void processValue(String value) {
		try {
			Object obj = toObject(value);
			if (obj != null) getCollection().add(obj);
		} catch (ParsingException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	protected List<Object> createCollection() {
		return new ArrayList<>();
	}

	@Override
	public boolean isEmpty() {
		return getCollection().isEmpty();
	}

}
