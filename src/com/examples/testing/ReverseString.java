package com.examples.testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ReverseString {

	public static void main(String... args) throws Exception {

		try (Socket socket = new Socket("localhost", 80);
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader readerIN = new BufferedReader(new InputStreamReader(System.in));//
		) {
			String readLine = null;
			while ((readLine = readerIN.readLine()) != null) {
				writer.println(readLine);

				boolean wait = true;
				while (wait) {
					Thread.sleep(50);
					wait = !reader.ready();
				}
				System.out.println("Reverted: " + reader.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

//		String stringToReverse = URLEncoder.encode("Testing", "UTF-8");
//
//		URL url = new URL("http://localhost:80");
//		URLConnection openConnection = url.openConnection();
//		openConnection.setDoOutput(true);
//
//		OutputStreamWriter writer = new OutputStreamWriter(openConnection.getOutputStream());
//		writer.write(stringToReverse);
//		writer.close();
//
//		BufferedReader reader = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));
//
//		String reading;
//		while ((reading = reader.readLine()) != null) {
//			System.out.println(reading);
//		}
//		reader.close();
//	}

//}
