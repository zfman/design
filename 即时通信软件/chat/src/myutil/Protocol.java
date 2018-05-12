package myutil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 协议工具类
 * 封装了消息的类型以及发和收的方法
 * @author Administrator
 *
 */
public class Protocol {
	// text
	public static final int TYPE_TEXT = 1;
	
	// 登录
	public static final int TYPE_LOAD = 2;
	
	// 退出
	public static final int TYPE_LOGOUT = 3;
	
	//登录成功
	public static final int TYPE_LOADSUCCESS = 4;
	
	//退出成功
	public static final int TYPE_LOGOUTSUCCESS = 5;

	/**
	 * 向输出流中发送消息
	 * @param type 消息类型
	 * @param bytes 消息内容
	 * @param dos 输出流
	 */
	public static void send(int type, byte[] bytes, DataOutputStream dos){
		int totalLen = 1 + 4 + bytes.length;
		try {
			//依次读取消息的三个部分
			dos.writeByte(type);
			dos.writeInt(totalLen);
			dos.write(bytes);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从输入流中解析消息
	 * @param dis 输入流
	 * @return 解析之后的结果
	 */
	public static Result getResult(DataInputStream dis) {
		byte type;
		try {
			//依次取出消息的三个部分
			type = dis.readByte();
			int totalLen = dis.readInt();
			byte[] bytes = new byte[totalLen - 4 - 1];
			dis.readFully(bytes);
			//返回解析结果
			return new Result(type & 0xFF, totalLen, bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}