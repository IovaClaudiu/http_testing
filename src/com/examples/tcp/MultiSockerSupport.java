package com.examples.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MultiSockerSupport extends Thread {

	private Socket socket;
	private static int count = 0;

	public MultiSockerSupport(Socket socket) {
		super("Multi Support");
		this.socket = socket;
	}

	@Override
	public void run() {
		try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); //
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//
		) {
			System.out.println("Server listening: " + count++);
			String inputLine = null;
			while ((inputLine = reader.readLine()) != null) {
				writer.println(inputLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
