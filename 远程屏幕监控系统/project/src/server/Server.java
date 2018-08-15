package server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Server {
	public static Map<String,Socket> client=new HashMap<String,Socket>();
	public static View view=new View();
	public static String curKey=null;
	public static boolean serverLive=true;
	public static void main(String[] args) {
		try {
			System.out.println(InetAddress.getLocalHost());
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		try {
			ServerSocket serverSocket=new ServerSocket(33000);
			view.create();
			while(serverLive){
				Socket socket=serverSocket.accept();
				new Thread(new HandleClient(socket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
