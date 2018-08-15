package client;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import myutil.Protocol;

/**
 * 封装被控端的方法
 * 
 * @author Administrator
 *
 */
public class Client {

	Socket socket;

	DataOutputStream dos = null;
	Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) screensize.getWidth();
	int height = (int) screensize.getHeight();
	Robot robot;
	static boolean isLive = true;
	JButton button;

	public Client() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 连接服务器
	 */
	public void conn(String address, int port) {
		try {
			socket = new Socket(address, port);
			dos = new DataOutputStream(socket.getOutputStream());
			// dos.writeUTF("client");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取屏幕截图并保存
	 * 
	 * @return
	 */
	public BufferedImage getScreenShot() {
		BufferedImage bfImage = robot.createScreenCapture(new Rectangle(0, 0, width, height));
		return bfImage;
	}

	public void load() {
		byte[] bytes = "client".getBytes();
		Protocol.send(Protocol.TYPE_LOAD, bytes, dos);
	}

	public void sendImage(BufferedImage buff) {
		if (buff == null)
			return;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(buff, "png", baos);
			Protocol.send(Protocol.TYPE_IMAGE, baos.toByteArray(), dos);
			baos.close();
			System.out.println("send file successfully");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 关闭客户端，释放掉资源
	public void close() {
		//向服务器发送消息
		Protocol.send(Protocol.TYPE_LOGOUT, new String("logout").getBytes(), dos);
		// 关闭资源
		try {
			if (dos != null)
				dos.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 图片缩放
	 * 
	 * @param bfImage
	 * @param scale
	 * @return
	 */
	public BufferedImage scale(BufferedImage bfImage, double scale) {
		// 截图压缩
		int width = bfImage.getWidth();
		int height = bfImage.getHeight();
		Image image = bfImage.getScaledInstance((int) (width * scale), (int) (height * scale), Image.SCALE_DEFAULT);
		BufferedImage tag = new BufferedImage((int) (width * scale), (int) (height * scale),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = tag.createGraphics();
		g.drawImage(image, 0, 0, null); // 绘制缩小后的图
		g.dispose();
		return tag;
	}

	/**
	 * 显示系统托盘
	 */
	public void showSystemTray() {
		Image image = Toolkit.getDefaultToolkit().getImage("img/icon.png");
		final TrayIcon trayIcon = new TrayIcon(image);// 创建托盘图标
		trayIcon.setToolTip("屏幕监控系统\r\n客户端");// 设置提示文字
		final SystemTray systemTray = SystemTray.getSystemTray();// 获得系统托盘对象

		final PopupMenu popupMenu = new PopupMenu(); // 创建弹出菜单
		MenuItem item = new MenuItem("退出");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isLive = false;
				close();
			}
		});
		popupMenu.add(item);
		trayIcon.setPopupMenu(popupMenu);// 为托盘图标加弹出菜单
		try {
			systemTray.add(trayIcon);// 为系统托盘加托盘图标
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		final Client client = new Client();
		client.showSystemTray();// 显示托盘
		client.conn("192.168.1.103",33000);
		client.load();// 登录
		client.showSystemTray();// 显示托盘
		while (client.isLive) {
			client.sendImage(client.getScreenShot());
			try {
				Thread.sleep(50);
			} catch (InterruptedException ev) {
				
			}
		}
	}
}
