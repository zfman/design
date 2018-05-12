package monitor;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.ImageIcon;

public class ClientManager {
	Socket socket;

	/**
	 * 连接服务器
	 */
	public void conn(int port) {
		try {
			socket = new Socket(InetAddress.getLocalHost(), port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 得到屏幕截图
	 * @return
	 */
	public ImageIcon getScreenShot(){
		ImageIcon icon = null;
		try {
			DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
			DataInputStream dis=new DataInputStream(socket.getInputStream());
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			dos.writeUTF("manager");
			byte[] b=new byte[1024];
			byte[] buffer=new byte[1024];
			int len;
			while((len=dis.read(b,0,b.length))>0){
				bos.write(buffer,0,len);
			}
			icon=new ImageIcon(buffer);
			bos.close();
			dis.close();
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return icon;
	}
}
