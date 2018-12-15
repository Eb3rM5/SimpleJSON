package com.eber.simplejson.supplier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.eber.simplejson.exception.ParsingException;

public class InputStreamSupplier extends BufferedReaderSupplier {
	
	public InputStreamSupplier(char startDelimiter, InputStream in) throws ParsingException, IOException {
		setReader(new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)), startDelimiter);
	}

}
