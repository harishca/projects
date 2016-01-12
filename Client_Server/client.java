
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Trivial client for the date server.
 */
public class client {
	
	static Socket s = new Socket();
	static PrintWriter s_out = null;
	static BufferedReader s_in = null;
	public static void main(String[] args) throws IOException {
		//reading arguments from command line for client
		String request_type=args[0];
		String host = args[2];
		String file_path="";
		int port = 8080;
		for(int i=0;i<args.length;i++){
			if(args[i].equals("-p")){
				port = Integer.parseInt(args[i+1]);
			}else if(args[i].equals("-f")){
				file_path=args[i+1];
			}
		}		
		try
		{
			s.connect(new InetSocketAddress(host , port));
			System.out.println("Connected");

			//writer for socket
			s_out = new PrintWriter( s.getOutputStream(), true);
			//reader for socket
			s_in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			//checking the type of request being sent
			if(request_type.equals("POST")){
			sendPost(request_type);
			}
			else
			{
				System.out.println("else pasrt");
			getPost(request_type,file_path);}
			
		}
		//Host not found
		catch (UnknownHostException e) 
		{
			System.err.println("Don't know about host : ");
			System.exit(1);
		}
		/*//Send message to server
		*/
		//Get response from server
		String response;
		while ((response = s_in.readLine()) != null) 
		{
			System.out.println( response );
		}
	}
	//sending the POST
	public static void sendPost(String req){
		String header="";
		String body="body read=harish";
		String content_type="application/x-www-form-urlencoded",host="127.0.0.1";
		int content_length=body.length()-5;
		header = req+"\r\nPOST /auth HTTP/1.1\r\nContent-Type: "+content_type+"\r\nContent-Length: "+content_length+"\r\nHost: "+host+"\r\nConnection: close\r\n";
		String payload = header+"\n\n"+body;
		s_out.println(payload);
		System.out.println("Post Payload sent");
	}
	//sending the GET
	public static void getPost(String req,String file){
		String message = req+"\r\nGET /"+file+" HTTP/1.1\r\n";
		s_out.println(message);
		System.out.println(message);
		System.out.println("Get Payload sent");
	}
	//sending the GET
	public static void getPost(String req1){
		String message = req1+"\r\nGET / HTTP/1.1\r\n";
		s_out.println(message);
		System.out.println("Get Payload sent");
	}
}

