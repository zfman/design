package monitor;

import javax.swing.ImageIcon;
public class Test {

	public static void main(String[] args) {
		ClientManager manager=new ClientManager();
		manager.conn(33000);
		ImageIcon icon=manager.getScreenShot();
	}

}
