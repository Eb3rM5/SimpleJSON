package com.eber.simplejson.test;

import java.util.Iterator;
import java.util.Map.Entry;

import com.eber.simplejson.JSONArray;
import com.eber.simplejson.JSONObject;
import com.eber.simplejson.delimiter.StringValue;

public class ArrayTest {

	public static void main(String[] args) throws Exception {
		JSONArray object = new JSONArray(ArrayTest.class.getResourceAsStream("test_2.json"));
		
		print(object, 0);
		
		System.out.println("Constructed JSON string: " + object.toString());
	}
	
	public static final void print(Object obj, int nestLevel) {
		
		String trace = "";
		
		for (int i = 0; i < nestLevel; i++) trace += " ";
 		
		if (obj instanceof JSONArray) {
			printIterator(((JSONArray)obj).iterator(), nestLevel);
		} else if (obj instanceof JSONObject) {
			printIterator(((JSONObject)obj).entryIterator(), nestLevel);
			System.out.println();
		} else if (obj instanceof Entry) {
			Entry<?, ?> entry = (Entry<?, ?>)obj;
			System.out.println(trace + entry.getKey() + "=");
			print(entry.getValue(), nestLevel);
		} else {
			if (obj instanceof String) System.out.println(trace + "\"" + StringValue.quoteValue((String)obj) + "\"");
			else System.out.println(trace + obj);
		}
		
	}
	
	private static void printIterator(Iterator<? extends Object> iterator, int nestLevel) {
		iterator.forEachRemaining(o->{
			print(o, nestLevel + 1);
		});
	}
	
}
