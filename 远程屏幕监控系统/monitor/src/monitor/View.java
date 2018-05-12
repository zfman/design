package monitor;

import javax.swing.JFrame;

public class View {
	public void create(){
		JFrame frame=new JFrame("多终端屏幕分享系统");
		frame.setSize(500,500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
