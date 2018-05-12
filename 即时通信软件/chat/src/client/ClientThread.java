package client;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import myutil.Protocol;
import myutil.Result;

/**
 * 客户端消息线程
 * 用以接收服务器消息
 * @author Administrator
 *
 */
public class ClientThread extends Thread {
	private Socket socket;//套接字
	private DataInputStream dis;//输入流
	
	//初始化套接字与输入流
	public ClientThread(Socket socket) {
		this.socket=socket;
		try {
			dis=new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true){
			//解析消息
			Result result =  Protocol.getResult(dis);
			if(result!=null)
			//根据消息类型处理
			handleType(result.getType(),result.getData());
		}
	}
	
	/**
	 * 根据消息的类型对消息处理
	 * @param type 消息类型
	 * @param data 消息内容
	 */
	private void handleType(int type, byte[] data) {
		SimpleDateFormat df=new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
		String time=df.format(new Date());
		switch (type) {
		case 1:
			//文本
			String[] args=new String(data).split("说：");
			
			View.area.append("  "+args[0]+"("+time+")\n  "+args[1]+"\n");
			break;
		case 4:
			View.area.append("  "+new String(data)+"\n");
			break;
		case 5:
			View.area.append("  "+new String(data)+"\n");
		default:
			break;
		}
		View.area.select(View.area.getText().length(), View.area.getText().length());
	}
}
