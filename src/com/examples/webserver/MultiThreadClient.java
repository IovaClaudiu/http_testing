package com.examples.webserver;

import java.net.Socket;
import java.util.List;

public class MultiThreadClient extends Thread {

	private static int count = 0;
	private Socket socket;
	private List<String> headerFields;

	public MultiThreadClient(Socket socket, List<String> headerFields) {
		super("Client Thread: " + count++);
		this.socket = socket;
		this.headerFields = headerFields;
	}

	@Override
	public void run() {
		String[] split = headerFields.get(0).split(" ");
		System.out.println("\t Executing method type: " + split[0]);
		System.out.println("\t For the URL: " + split[1]);
		System.out.println("\t Using protocol: " + split[2]);
		ProcessRequestURI.getRespondFromURL(split[1]).processRequest(socket);
	}
}