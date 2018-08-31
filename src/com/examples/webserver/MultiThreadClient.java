package com.examples.webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
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
		String url = headerFields.get(0).split(" ")[1];
		manageRequest(url);
	}

	private void manageRequest(String path) {
		if (path.equals("/")) {
			writePlainHTML();
		} else if (path.equals("/file")) {
			writeHTMLFromFile();
		} else if (path.equals("/image")) {
			writeHtmlImage();
		} else {
			writeErrorCode();
		}
	}

	private void writePlainHTML() {
		try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
			writeFirstHTMLLines(writer);
			writer.println("<h1> Hello world </h1>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeFirstHTMLLines(PrintWriter writer) {
		writer.println("HTTP/1.1 200 OK");
		writer.println("Content-Type: text/html");
		writer.println("");
	}

	private void writeHTMLFromFile() {
		try (//
				FileInputStream input = (new FileInputStream//
				(new File("input/basic.html"))); //
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);//
		) {
			writeFirstHTMLLines(writer);
			copy(input, socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void copy(final FileInputStream in, final OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int count;
		while ((count = in.read(buffer)) != -1) {
			out.write(buffer, 0, count);
		}
		out.flush();
	}

	private void writeHtmlImage() {
		try (//
				FileInputStream stream = (new FileInputStream//
				(new File("input/test.gif"))); //
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);//
		) {
			writer.println("HTTP/1.1 200 OK");
			writer.println("Content-Type: image/gif");
			writer.println("");
			copy(stream, socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeErrorCode() {

		try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
			writer.println("HTTP/1.1 400 ERROR");
			writer.println("Content-Type: text/html");
			writer.println("");

			writer.println("<h1>Cannot parse this URL<h1>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}