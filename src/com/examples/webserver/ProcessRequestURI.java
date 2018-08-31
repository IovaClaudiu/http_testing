package com.examples.webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public enum ProcessRequestURI {

	DEFAULT("/") {

		@Override
		public void processRequest(Socket socket) {
			try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
				writer.println("HTTP/1.1 200 OK");
				writer.println("Content-Type: text/html");
				writer.println("");
				writer.println("<h1> Hello world </h1>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	},

	HTML_FILE("/file") {

		@Override
		public void processRequest(Socket socket) {
			try (FileInputStream input = //
					(new FileInputStream(new File("input/basic.html"))); //
					PrintWriter writer = //
							new PrintWriter(socket.getOutputStream(), true);//
			) {
				writer.println("HTTP/1.1 200 OK");
				writer.println("Content-Type: text/html");
				writer.println("");
				copy(input, socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	},

	IMAGE_FILE("/image") {

		@Override
		public void processRequest(Socket socket) {
			try (FileInputStream stream = //
					(new FileInputStream(new File("input/test.gif"))); //
					PrintWriter writer = //
							new PrintWriter(socket.getOutputStream(), true);//
			) {
				writer.println("HTTP/1.1 200 OK");
				writer.println("Content-Type: image/gif");
				writer.println("");
				copy(stream, socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	},

	ERROR("") {

		@Override
		public void processRequest(Socket socket) {
			try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
				writer.println("HTTP/1.1 404 Not Found");
				writer.println("Content-Type: text/html");
				writer.println("");
				writer.println("<head><title>404 Not Found</title></head>");
				writer.println("<h1>Not found</h1>");
				writer.println("<p>The requested URL cannot be parsed by the server!</p>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	private String path;

	private ProcessRequestURI(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public static ProcessRequestURI getRespondFromURL(String url) {
		return Arrays.stream(values()).//
				filter(s -> s.getPath().equals(url)).//
				findFirst().//
				orElse(ProcessRequestURI.ERROR);
	}

	private static void copy(final FileInputStream in, final OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int count;
		while ((count = in.read(buffer)) != -1) {
			out.write(buffer, 0, count);
		}
		out.flush();
	}

	public abstract void processRequest(Socket socket);

}
