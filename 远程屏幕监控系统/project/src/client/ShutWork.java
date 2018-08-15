package client;

public class ShutWork extends Thread{

	private Client client;
	public ShutWork(Client client) {
		this.client=client;
	}
	@Override
	public void run() {
		client.close();
	}
}
