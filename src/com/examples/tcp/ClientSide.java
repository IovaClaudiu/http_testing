package com.examples.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSide {

	public static void main(String... args) {

		try (Socket socket = new Socket("localhost", 7); //
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); //
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //
				BufferedReader readerIO = new BufferedReader(new InputStreamReader(System.in));//
		) {
			String input = null;
			while ((input = readerIO.readLine()) != null) {
				writer.println(input);
				System.out.println("echo: " + reader.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
