package com.eber.simplejson.observable;

import javafx.beans.Observable;

public interface ObservableJSONCollection<K, V> {
	
	public V get(K key);
	
	public void set(K key, V value);
	
	public Observable getCollection();
	
}
