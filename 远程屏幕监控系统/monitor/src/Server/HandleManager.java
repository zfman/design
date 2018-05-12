package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HandleManager implements Runnable {

	private Socket socket;
	private Server server;
	
	public HandleManager(Socket socket) {
		this.socket=socket;
		this.server=new Server();
	}
	
	@Override
	public void run() {
		byte[] b=new byte[1024];
		try {
			DataInputStream dis=new DataInputStream(socket.getInputStream());
			FileOutputStream fos=new FileOutputStream(new File("D://wps//"+System.currentTimeMillis()+".png"));
			int len;
			while((len=dis.read(b,0,b.length))!=-1){
				fos.write(b,0,len);
				fos.flush();
			}
			System.out.println("client"+server.client.size()+":"+server.client.toString());
			fos.close();
			dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

}
