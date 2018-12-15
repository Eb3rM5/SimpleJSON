package com.eber.simplejson.observable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.eber.simplejson.JSONObject;
import com.eber.simplejson.delimiter.Delimiter;
import com.eber.simplejson.exception.ParsingException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class ObservableJSONObject extends JSONObject implements ObservableJSONCollection<String, Object>{

	public ObservableJSONObject() {
		super();
	}
	
	public ObservableJSONObject(InputStream in) throws ParsingException, IOException {
		super(in);
	}
	
	@Override
	protected Map<String, Object> createCollection() {
		return FXCollections.observableHashMap();
	}
	
	@Override
	public ObservableMap<String, Object> getCollection() {
		return (ObservableMap<String, Object>) super.getCollection();
	}
	
	@Override
	protected Delimiter createDelimiter(char delimiter) {
		switch (delimiter) {
			default:
				return super.createDelimiter(delimiter);
			case '[':
				return new ObservableJSONArray();
			case '{':
				return new ObservableJSONObject();
		}
	}

	@Override
	public void set(String key, Object value) {
		put(key, value);
	}
	
}
