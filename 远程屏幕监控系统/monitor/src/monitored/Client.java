package monitored;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

/**
 * 封装被控端的方法
 * 
 * @author Administrator
 *
 */
public class Client {

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
	 * 获取屏幕截图并保存
	 * 
	 * @param savePath
	 *            保存路径
	 * @param fileName
	 *            保存的文件名
	 * @param format
	 *            保存的图片格式
	 * @return
	 */
	public File getScreenShot(String savePath, String fileName) {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screensize.getWidth();
		int height = (int) screensize.getHeight();
		BufferedImage bfImage = null;
		File file = null;
		try {
			Robot robot = new Robot();
			bfImage = robot.createScreenCapture(new Rectangle(0, 0, width, height));
			File path = new File(savePath);
			file = new File(path, fileName + ".png");
			ImageIO.write(bfImage,"png", file);
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	public void sendFile(File file){
		byte[] b=new byte[1024];
		if(file==null) return;
		DataOutputStream dos = null;
		FileInputStream fis = null;
		try {
			dos=new DataOutputStream(socket.getOutputStream());
			fis=new FileInputStream(file);
			dos.writeUTF("client");
//			dos.writeLong(file.getTotalSpace());
			int n=fis.read(b);
			while(n!=-1){
				dos.write(b,0,n);
				dos.flush();
				n=fis.read(b);
			}
			dos.writeUTF("finish");
			System.out.println("send file successfully");
		} catch (IOException e) {
			try{
				if(fis!=null) fis.close();
				if(dos!=null) dos.close();
				if(socket!=null) socket.close();
			}catch(IOException ez){
				ez.printStackTrace();
			}
		}
		
	}
}
