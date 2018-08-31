package com.examples.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerWeb {

	public static void main(String... args) {
		try (ServerSocket server = new ServerSocket(80)) {
			System.out.println("Server started...");
			while (true) {
				Socket accept = server.accept();
				if (accept != null) {
					System.out.println("Client Connected: " + accept.toString());
					new MultiThreadClient(accept, colletingHeaderInfo(accept)).run();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static List<String> colletingHeaderInfo(Socket socket) {
		List<String> header = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String tmpString;
			while ((tmpString = reader.readLine()) != null) {
				header.add(tmpString);
				if (tmpString.equals("")) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return header;
	}
}
