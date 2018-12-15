package com.eber.simplejson.test;

import com.eber.simplejson.JSONObject;

public class ObjectTest {

	public static void main(String[] args) throws Exception {
		JSONObject object = new JSONObject(ObjectTest.class.getResourceAsStream("test.json"));
		ArrayTest.print(object, 0);
		
		System.out.println("Constructed JSON string: " + object.toString());
	}
	
}
