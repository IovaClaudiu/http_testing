package com.examples.testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class StringReverseSocket {
	public static void main(String... args) {
		System.out.println("Server started...");

		try (ServerSocket server = new ServerSocket(80); //
				Socket socket = server.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);//
		) {
			System.out.println("Connected from: " + socket.toString());
			String messageToReverse;
			while ((messageToReverse = reader.readLine()) != null) {
				System.out.println("Message to reverse: " + messageToReverse);
				writer.println(new StringBuilder(messageToReverse).reverse().toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
