package Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
	public Map<String,Socket> client=new HashMap<>();
	public Map<String,Socket> clientManager=new HashMap<>();
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket=new ServerSocket(33000);
			Server server=new Server();
			while(true){
				Socket socket=serverSocket.accept();
				DataInputStream dis=new DataInputStream(socket.getInputStream());
				String power=dis.readUTF();
				String clientKey="client"+socket.getInetAddress().getAddress();
				String managerKey="manager"+socket.getInetAddress().getAddress();
				if(power!=null&&power.equals("client")){
					if(!server.client.containsKey(clientKey)) server.client.put(clientKey,socket);
					new Thread(new HandleClient(socket)).start();
				}else{
					if(!server.clientManager.containsKey(managerKey)) server.clientManager.put(managerKey,socket);
					new Thread(new HandleManager(socket)).start();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
