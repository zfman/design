package Server;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.Socket;

public class HandleClient implements Runnable{
	private Socket socket;
	private Server server;
	
	public HandleClient(Socket socket){
		this.socket=socket;
		this.server=new Server();
	}
	
	public void run() {
		byte[] b=new byte[1024];
		DataInputStream dis=null;
		FileOutputStream fos = null;
		try {
				dis=new DataInputStream(socket.getInputStream());
				fos=new FileOutputStream(new File("D://wps//"+System.currentTimeMillis()+".png"));
				int len=0;
				while((len=dis.read(b,0,b.length))!=-1){System.out.println("pp");
					System.out.println();
					fos.write(b,0,len);
					fos.flush();
				}
				fos.close();
				dis.close();
				socket.close();
		} catch (IOException e) {
			server.client.remove("client"+socket.getInetAddress().getHostAddress());
			try{
				if(fos!=null) fos.close();
				if(dis!=null) dis.close();
				if(socket!=null) socket.close();
			}catch(IOException ez){
				ez.printStackTrace();
			}
			
		}
	
	}
}
