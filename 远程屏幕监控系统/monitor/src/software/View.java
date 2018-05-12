package software;

import java.awt.AWTException;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class View {
	Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width;
	private int height;
	
	public View(){
		width = (int) screensize.getWidth();
		height = (int) screensize.getHeight();
	}
	
	/**
	 * 得到屏幕截图并转化为Icon
	 * @return
	 */
	public Icon getScreenShot(){
		Robot robot;
		ImageIcon icon = null;
		try {
			robot = new Robot();
			//获取屏幕截图
			BufferedImage fullScreenImage = robot.createScreenCapture(new Rectangle(Toolkit  
	                .getDefaultToolkit().getScreenSize()));  
			//将BufferedImage转为Icon
	        icon = new ImageIcon(fullScreenImage); 
		} catch (AWTException e) {
			e.printStackTrace();
		}
        return icon;  
	}
	
	public void create(){
		final JFrame frame=new JFrame();
		JLabel label=new JLabel(getScreenShot());
		
		frame.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
		Container container=frame.getContentPane();
		((JPanel)container).setOpaque(false);
		
		frame.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_DELETE)
				frame.setVisible(false);
			}
		});
		label.setBounds(0, 0,width,height);
		frame.setSize(width,height);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.setVisible(true);
		
	}
	
	public void changeStart(boolean isStartAtLogon){  
	      String regKey = "HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run";  
	      String myAppName = "MyApp";  
	      String exePath = "C:\\price\\price.exe";//开机启动程序本地目录  
	      try {
			Runtime.getRuntime().exec("reg "+(isStartAtLogon?"add ":"delete ")+regKey+" /v "+myAppName+(isStartAtLogon?" /t reg_sz /d "+exePath:" /f"));
		} catch (IOException e) {
			e.printStackTrace();
		}  
	  }  
	
	
	public static void main(String[] args) {
		View view=new View();
		view.create();
		view.changeStart(true);
	}
}
