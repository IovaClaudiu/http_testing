package com.examples.tcp;
import java.io.IOException;
import java.net.ServerSocket;

public class ServerSide {

	public static void main(String... args) {
		try (ServerSocket server = new ServerSocket(7);) {
			while (true) {
				new MultiSockerSupport(server.accept()).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
