package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务器类
 * 负责接受客户端的连接
 * 将客户端的连接交付给服务器端线程处理
 */
public class Server {
	//维护客户端的配置信息
	public static List<Map<String,Object>> clients=new ArrayList<>();
	
	//主方法
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(30000);
			while (true) {
				Socket socket = serverSocket.accept();
				new Thread(new ServerThread(socket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
