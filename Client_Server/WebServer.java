package client_server;

import java.io.*;
import java.net.*;
import java.util.*;

public class WebServer extends Thread {

	// to read the request coming from the client
	BufferedReader request = null;
	// to load the response to be given to the client
	DataOutputStream response = null;
	// creating the socket object
	Socket skt = null;

	// parameters associated with the file which in our case is test.txt
	String pointer = null;
	String length = null;
	String inputFile = null;
	String fileName = null;

	// output onto a file
	DataOutputStream toFile = null;

	Writer write = null;

	// the beginning and end of the html file
	static final String headHtml = "<html>"
			+ "<title> Web Server Implementation </title>" + "<body>";
	static final String tailHtml = "</body>" + "</html>";

	// a constructor that assigns the socket value
	public WebServer(Socket client) {
		skt = client;
	}

	// to run multiple threads, run() is used
	
	public void run() {
		try {
			request = new BufferedReader(new InputStreamReader(
					skt.getInputStream()));
			response = new DataOutputStream(skt.getOutputStream());

			// reading the request
			String fromClient = request.readLine();
			String main = fromClient;

			StringTokenizer stringToken = new StringTokenizer(main);
			String httpMethod = stringToken.nextToken();

			// prints the method if it's a get method or a post method
			System.out.println("Method : " + httpMethod);

			String queryString = stringToken.nextToken();

			System.out.println("httpQueryString : " + queryString);

			StringBuffer responseBuffer = new StringBuffer();
			responseBuffer.append("<b> HTTP Home Page </b><BR>");
			responseBuffer.append(" Client  <BR>");

			do {
				responseBuffer.append(fromClient + "<BR>");
				System.out.println(fromClient);
				fromClient = request.readLine();
			} while (request.ready());

			// handling get method
			if (httpMethod.equals("GET")) {
				System.out.println("HTTP GET Method");
				if (queryString.equals("/")) {
					// html form to take input from the web browser that acts as
					// the client
					String responseString = WebServer.headHtml
							+ "<form action=\"http://127.0.0.1:7000\" enctype=\"multipart/form-data\""
							+ "method=\"post\">"
							+ "<br> <br> Upload the input file (*.txt Only). <br> <br> Thank You. <br> <br><br> <br> <input name=\"file\" type=\"file\"><br> <br>"
							+ "<input value=\"Upload File\" type=\"submit\"></form>"
							+ WebServer.tailHtml;
					// sending response as 200 i,e success
					sendResponse(200, responseString, false);
				} else {
					String fileName = queryString.replaceFirst("/", "");

					// this is the name of the file that is executed
					fileName = new String("D:\\JAVA '14\\CNProjectNishanth"
							+ fileName);
					if (new File(fileName).isFile()) {
						sendResponse(200, fileName, true);
					} else {
						sendResponse(
								404,
								"<b><h4>HTTP/1.0 <br>404 File Not Found</h4></b>",
								false);
					}
				}
			}

			// handling post method
			else if (httpMethod.equals("POST")) {
				System.out.println(".........Start Posting..........");
				do {
					while (true) {
						pointer = request.readLine();
						if (pointer.indexOf("Content-Length:") != -1) {
							length = pointer.split(" ")[1];
							break;
						}
						String boundary;
						while (true) {
							pointer = request.readLine();
							if (pointer
									.indexOf("Content-Type: multipart/form-data") != -1) {
								boundary = pointer.split("boundary=")[1];
								break;
							}
						}
						while (true) {
							pointer = request.readLine();
							if (pointer.indexOf("--" + boundary) != -1) {
								String nextLine = request.readLine();
								inputFile = nextLine.split("filename=")[1]
										.replaceAll("\"", "");
								String[] filelist = fileName.split("\\"
										+ System.getProperty("file.separator"));
								fileName = filelist[filelist.length - 1];
								System.out.println("File to be uploaded = "
										+ inputFile);
								break;
							}
						}
						@SuppressWarnings("unused")
						String fileContentType = request.readLine().split(" ")[1];
						request.readLine();
						String fileContent = request.readLine();
						toFile = new DataOutputStream(new FileOutputStream(
								"C:\\Users\\Administrator\\Desktop\\"
										+ inputFile));
						toFile.writeBytes(fileContent);

						// sending the success response
						sendResponse(200, "File " + inputFile + " Uploaded",
								false);
					}

				} while (request.ready());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// sending response to the request made by the client
	@SuppressWarnings("unused")
	public void sendResponse(int statusCode, String responseString,
			boolean isFile) throws Exception {

		String status = null;
		String dataLength = null;
		String file = null;
		String contentType = "Content-Type: text/html" + "\r\n";
		FileInputStream fileInput = null;

		if (statusCode == 200)
			status = "HTTP/1.0 200 OK" + "\r\n";
		else
			status = "HTTP/1.0 404 File Not Found" + "\r\n";
		if (isFile) {
			fileName = responseString;
			fileInput = new FileInputStream(fileName);
			dataLength = "Content-Length: "
					+ Integer.toString(fileInput.available()) + "\r\n";
			if (!fileName.endsWith(".htm") && !fileName.endsWith(".html"))
				contentType = "Content-Type: \r\n";
		} else {
			responseString = WebServer.headHtml + responseString
					+ WebServer.tailHtml;
			dataLength = "Content-Length: " + responseString.length() + "\r\n";
		}

		response.writeBytes(status);
		response.writeBytes(contentType);
		response.writeBytes(dataLength);
		response.writeBytes("Connection: Close\r\n");
		response.writeBytes("\r\n");

		if (isFile)
			sendFile(fileInput, response);
		else
			response.writeBytes(responseString);

		response.close();

		// writing to the log file
		try {
			write = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(
							"C:\\Users\\Administrator\\Desktop\\log.txt"),
					"utf-8"));
			write.write(status);
			write.write(contentType);
			write.write(dataLength);
			write.write("Connection: Close ");
			write.append('\n');
			write.write("Successfully logged");
			write.close();
		} catch (IOException e) {
			System.out.println("Could not log the status");
		}
	}

	public void sendFile(FileInputStream fileInput, DataOutputStream out)
			throws Exception {
		byte[] fileData = new byte[1024];
		int dataSize;

		while ((dataSize = fileInput.read(fileData)) != -1) {
			out.write(fileData, 0, dataSize);
		}
		fileInput.close();
	}

	public static void main(String args[]) throws Exception {

		// a new socket is created on the server side to service the request
		// from the client
		ServerSocket serverSocket = new ServerSocket(7000, 10,
				InetAddress.getByName("127.0.0.1"));
		System.out
				.println(" The TCP Server is polling the port 7000 for any client requests");

		while (true) {
			Socket connected = serverSocket.accept();
			(new WebServer(connected)).start();
		}
	}
}