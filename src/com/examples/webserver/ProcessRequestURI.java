package com.examples.webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;

public enum ProcessRequestURI {

	DEFAULT("/") {

		@Override
		public void processRequest(Socket socket) {
			try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
				createResponsHeader(writer, "200 OK", "text/html");
				writer.println("<h1> Hello world </h1>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	},

	HELP("/help") {

		@Override
		public void processRequest(Socket socket) {
			try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
				createResponsHeader(writer, "200 OK", "text/html");
				writer.println("<h2>The available paths are:</h2>");
				Arrays.stream(values()).forEach(s -> writer.println("<p>" + s.getPath() + "</p>"));
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
				createResponsHeader(writer, "200 OK", "text/html");
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
				createResponsHeader(writer, "200 OK", "image/gif");
				copy(stream, socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	},

	IMAGE_JPG("/imagejpg") {

		@Override
		public void processRequest(Socket socket) {
			try (PrintWriter writer = //
					new PrintWriter(socket.getOutputStream(), true)//
			) {
				byte[] imgBytes = Files.readAllBytes(new File("input/test1.jpg").toPath());
				createResponsHeader(writer, "200 OK", "text/html");
				StringBuilder builder = new StringBuilder();
				builder.append("data:image/jpg;base64,");
				builder.append(Base64.getEncoder().encodeToString(imgBytes));
				writer.println("<img src=\"" + builder.toString() + "\" alt=\"This is a local  jpg image\"></img>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	},

	IMAGE_GIF("/imagegif") {

		@Override
		public void processRequest(Socket socket) {
			try (PrintWriter writer = //
					new PrintWriter(socket.getOutputStream(), true)//
			) {
				byte[] imgBytes = Files.readAllBytes(new File("input/test.gif").toPath());
				createResponsHeader(writer, "200 OK", "text/html");
				StringBuilder builder = new StringBuilder();
				builder.append("data:image/gif;base64,");
				builder.append(Base64.getEncoder().encodeToString(imgBytes));
				writer.println("<img src=\"" + builder.toString() + "\" alt=\"This is a local  gif image\"></img>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	},

	IMAGE_STREAMING("/imageStream") {

		@Override
		public void processRequest(Socket socket) {
			try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
				createResponsHeader(writer, "200 OK", "text/html");
				writer.println("<img src=\"/poza\" alt=\"Streaming image\"> </img>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	},

	POZA("/poza") {

		@Override
		public void processRequest(Socket socket) {
			try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
				writer.println("HTTP/1.1 200 OK");
				writer.println("Content-Type: image/jpg");
				writer.println("Content-length: " + new File("input/test1.jpg").length());
				writer.println("");
				copy(new FileInputStream(new File("input/test1.jpg")), socket.getOutputStream());
				// Files.copy(new File("input/test1.jpg").toPath(), socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	},

	ERROR("") {

		@Override
		public void processRequest(Socket socket) {
			try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
				createResponsHeader(writer, "404 Not Found", "text/html");
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

	private static void createResponsHeader(PrintWriter writer, String status, String contentType) {
		writer.println("HTTP/1.1 " + status);
		writer.println("Content-Type: " + contentType);
		writer.println("");
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
