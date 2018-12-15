package com.eber.simplejson.observable;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.eber.simplejson.JSONArray;
import com.eber.simplejson.delimiter.Delimiter;
import com.eber.simplejson.exception.ParsingException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ObservableJSONArray extends JSONArray {

	public ObservableJSONArray() {
		super();
	}
	
	public ObservableJSONArray(InputStream in) throws ParsingException, IOException {
		super(in);
	}
	
	@Override
	protected List<Object> createCollection() {
		return FXCollections.observableArrayList();
	}
	
	@Override
	public ObservableList<Object> getCollection() {
		return (ObservableList<Object>) super.getCollection();
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
	
}
