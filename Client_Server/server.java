//
//Name : Harish Kumar Chikkamaranahalli Ashokkumar
//Student ID : 1001100559
//
import java.io.*;
import java.net.*;
import java.util.*;
public class server {

	public static void main(String args[]){
		int port;
		//reading the arguments for server
		if(args.length>0){
			port = Integer.parseInt(args[0]);
		}else{
			port = 8080;
		}
		ServerSocket serversocket = null;
		Socket mySocket;
		//creating a server socket to listen to port
		try{
			serversocket = new ServerSocket(port);
			System.out.println("Server socket created. Waiting for connection...");
		}catch(Exception e){
			System.out.println("Unable to create socket over port "+port);
			System.exit(-1);
		}
		//trying for http connection after the server socket is created.
		//Multithreading is implemented in this part of the code.
		while(true){
			try{
				mySocket = serversocket.accept();
				System.out.println("Connection received from " + mySocket.getInetAddress().getHostName() + " : " + mySocket.getPort());
				HttpRequest request = new HttpRequest(mySocket);
				Thread thread = new Thread(request);
				thread.start();
			}catch(Exception e){
				System.out.println("Excpetion encountered on accept");
				System.out.println(e);
			}
		}

	}

}

final class HttpRequest implements Runnable{

	final static String CRLF = "\r\n";
	Socket socket;

	public HttpRequest(Socket sock) throws Exception{
		// TODO Auto-generated constructor stub
		//Initializing the Socket
		this.socket = sock;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			processRequest();
		}catch(Exception e){
			System.out.println("Inside run "+e);
		}
	}

	private void processRequest() throws Exception{
		String line , input = "",httpMethod="",httpQuery="";
		String [] lines;
		int i=0;
		try
		{
			//get socket writing and reading streams
			DataInputStream in = new DataInputStream(socket.getInputStream());
			PrintStream out = new PrintStream(socket.getOutputStream());
			PrintWriter pout = new PrintWriter(socket.getOutputStream());
			DataOutputStream out1 = new DataOutputStream(socket.getOutputStream());
			//reading the first line of the inputStream and tokenizing them
			String main = in.readLine();
			StringTokenizer stringToken = new StringTokenizer(main);
			if(stringToken.countTokens()>1){
				httpMethod = stringToken.nextToken();
				httpQuery = stringToken.nextToken();
				System.out.println("For browser");
			}else{
				httpMethod = main;

			}


			if(httpMethod.equals("POST")){
				//Now start reading input from client for the post method
				while((line = in.readLine()) != null&& !line.equals(".")) 
				{
					//System.out.println("Inside POST");
					if(line.contains("body")){
						System.out.println("Printing the body of the request");
						String body = line;
						body = body.replaceFirst("body ", "");
						out.println(body);
						System.out.println(body);
					}else{
						System.out.println(line);
						continue;
					}




				}
			}else if(httpMethod.equals("GET")){
				//GET Method which works for the browser without  a filename.
				if(httpQuery.equals("/")||httpQuery.equals("/index.html")||httpQuery.equals("/index.htm")){
					File file = new File("index.html");
					int ch;
					StringBuffer strContent = new StringBuffer("");
					FileInputStream fin = null;
					//writing the file contents into string buffer
					try
					{
						fin = new FileInputStream(file);
						while( (ch = fin.read()) != -1)
							strContent.append((char)ch);
						fin.close();
					}
					catch(FileNotFoundException e)
					{
						System.out.println("HTTP/1.0 404 File Not Found\r\n");
						out.println("HTTP/1.0 404 File Not Found\r\n");
					}
					catch(IOException ioe)
					{
						System.out.println("Exception while reading the file" + ioe);
					}
					//writing data into the socket via output stream onto the browser
					String str = strContent.toString();
					out1.writeBytes("HTTP/1.1 200 OK\r\n");
					out1.writeBytes("Content-Type: text/html; charset=UTF-8\r\n");
					out1.writeBytes("\r\n");
					System.out.println(strContent);
					out1.writeBytes(str);
					out1.flush();
					out1.close();
				}
				else if(httpQuery.length()>0)
				{
					//browser request for file
					System.out.println("Inside else if"+httpQuery);
					String fp = httpQuery;
					fp = fp.substring(1);
					System.out.println("text file"+fp);
					File file = new File(fp);
					int ch;
					StringBuffer strContent = new StringBuffer("");
					FileInputStream fin = null;
					try
					{
						fin = new FileInputStream(file);
						while( (ch = fin.read()) != -1)
							strContent.append((char)ch);
						fin.close();
					}
					catch(FileNotFoundException e)
					{
						System.out.println("HTTP/1.0 404 File Not Found\r\n");
						out.println("HTTP/1.0 404 File Not Found\r\n");
					}
					catch(IOException ioe)
					{
						System.out.println("Exception while reading the file" + ioe);
					}
					//writing data into the socket via output stream onto the browser
					String str = strContent.toString();
					out1.writeBytes("HTTP/1.1 200 OK\r\n");
					out1.writeBytes("Content-Type: text/html; charset=UTF-8\r\n");
					out1.writeBytes("\r\n");
					System.out.println(strContent);
					out1.writeBytes(str);
					out1.flush();
					out1.close();
				}else{
					while((line = in.readLine()) != null&& !line.equals(".")) 
					{
						//browse index html from the web client
						StringTokenizer stringToken1 = new StringTokenizer(line);
						String httpMethod1 = stringToken1.nextToken();
						System.out.println(httpMethod1);
						String httpQ = stringToken1.nextToken();
						System.out.println(httpQ);
						if(httpQ.equals("/index.html")||httpQ.equals("/index.htm")||httpQ.equals("/")){
							File file = new File("index.html");
							int ch;
							StringBuffer strContent = new StringBuffer("");
							FileInputStream fin = null;
							try
							{
								fin = new FileInputStream(file);
								while( (ch = fin.read()) != -1)
									strContent.append((char)ch);
								fin.close();
							}
							catch(FileNotFoundException e)
							{
								System.out.println("HTTP/1.0 404 File Not Found\r\n");
								out.println("HTTP/1.0 404 File Not Found\r\n");
							}
							catch(IOException ioe)
							{
								System.out.println("Exception while reading the file" + ioe);
							}
							//writing data into the socket via output stream onto the browser
							out.print("HTTP/1.0 200 OK\r\n");
							out.print("Content-Type: text/html\r\n");
							System.out.println(strContent);
							out.println(strContent);
							out.flush();
						}else{
							//requesting file from the web client
							String fp = httpQ;
							fp = fp.substring(1);
							System.out.println(fp);
							File file = new File(fp);
							int ch;
							StringBuffer strContent = new StringBuffer("");
							FileInputStream fin = null;
							try
							{
								fin = new FileInputStream(file);
								while( (ch = fin.read()) != -1)
									strContent.append((char)ch);
								fin.close();
							}
							catch(FileNotFoundException e)
							{
								System.out.println("HTTP/1.0 404 File Not Found\r\n");
								out.println("HTTP/1.0 404 File Not Found\r\n");
							}
							catch(IOException ioe)
							{
								System.out.println("Exception while reading the file" + ioe);
							}
							//writing data into the socket via output stream onto the browser
							out.print("HTTP/1.1 200 OK\r\n");
							out.print("Content-Type: text/html\r\n");
							System.out.println(strContent);
							out.println(strContent);
							out.flush();
						}
					}
				}
			}
			//client disconnected, so close socket
			socket.close();
			System.out.println("The Client Socket has been closed");
		} 

		catch (IOException e) 
		{
			System.out.println("IOException on socket : " + e);
			e.printStackTrace();
		}
	}

}
