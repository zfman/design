package myutil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 封装一个协议
 * @author Administrator
 *
 */
public class Protocol {

	public static int TYPE_IMAGE=1;//图片
	public static int TYPE_LOAD=2;//登录
	public static int TYPE_LOGOUT=3;//退出
	
	
	public static void send(int type,byte[] bytes,DataOutputStream dos){
		int totalLen=1+4+bytes.length;
			try {
				dos.writeByte(type);
				dos.writeInt(totalLen);
				dos.write(bytes);
				dos.flush();
			} catch (IOException e) {
				System.exit(0);
			}
			
	}
	
	public static Result getResult(DataInputStream dis){
			
			try {
				byte type = dis.readByte();
				int totalLen=dis.readInt();
				byte[] bytes=new byte[totalLen-4-1];
				dis.readFully(bytes);
				return new Result(type&0xFF,totalLen,bytes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	}
}