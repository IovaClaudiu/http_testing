package com.examples.testing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class URLReader {
	public static void main(String... args) throws Exception {
		URL oracle = new URL("https://www.oracle.com/");
		BufferedReader reader = new BufferedReader(new InputStreamReader(oracle.openStream()));

		String message = null;
		while ((message = reader.readLine()) != null) {
			System.out.println(message);
		}
		reader.close();
	}
}
