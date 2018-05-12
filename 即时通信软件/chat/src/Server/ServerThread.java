package Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import myutil.Protocol;
import myutil.Result;

/**
 * 服务器端线程
 * 负责与客户端通信
 * @author Administrator
 *
 */
public class ServerThread implements Runnable{
	//套接字
	public Socket socket;
	
	//输入、输出流
	public DataInputStream dis=null;
	public DataOutputStream dos=null;
	
	//用户昵称
	public String userName=null;
	
	//用户配置信息的Map
	public Map<String, Object> thisMap=null;
	
	//标志线程是否生存
	public boolean isLive=true;
	
	/**
	 * 构造服务器端线程实体
	 * 初始化输入、输出流
	 * @param socket 客户端套接字
	 */
	public ServerThread(Socket socket){
		this.socket=socket;
		try {
			dis=new DataInputStream(socket.getInputStream());
			dos=new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 线程体
	 */
	public void run() {
		while(isLive){
			//解析消息
			Result result = null;
			result = Protocol.getResult(dis);
			if(result!=null)
			//按类型处理
			handleType(result.getType(),result.getData());
		}
	}

	/**
	 * 根据消息类型执行相应操作
	 * @param type 类型
	 * @param data 消息内容
	 */
	public void handleType(int type, byte[] data) {
		switch (type) {
		case 1:
			//遍历集合，获取输出流
			//向所有用户转发消息
			for(int i=0;i<Server.clients.size();i++){
				System.out.println("message:"+new String(data));
				DataOutputStream  dos2=(DataOutputStream) Server.clients.get(i).get("dos");
				String msg=new String(data);
				Protocol.send(Protocol.TYPE_TEXT,(userName+"说："+msg).getBytes(),dos2);
			}
			break;
		case 2:
			//设置配置信息并添加至服务器端的集合中
			userName=new String(data);
			Map<String,Object> map=new HashMap<>();
			map.put("dos",dos);
			map.put("user",userName);
			Server.clients.add(map);
			
			//通知所有用户有人登陆聊天室
			thisMap=map;
			for(int i=0;i<Server.clients.size();i++){
				DataOutputStream  dos2=(DataOutputStream) Server.clients.get(i).get("dos");
				Protocol.send(Protocol.TYPE_LOADSUCCESS, ("   系统："+userName+"进入聊天室").getBytes(), dos2);
			}
			break;
		case 3:
			//告知所有用户有人要退出聊天室
			for(int i=0;i<Server.clients.size();i++){
				DataOutputStream  dos2=(DataOutputStream) Server.clients.get(i).get("dos");
				Protocol.send(Protocol.TYPE_LOGOUTSUCCESS, ("   系统："+userName+"退出聊天室").getBytes(), dos2);
			}
			//删除集合中保存的该客户端信息
			Server.clients.remove(thisMap);
			isLive=false;
			break;
		default:
			break;
		}
	}
}
