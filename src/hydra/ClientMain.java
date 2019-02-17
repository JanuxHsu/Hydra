package hydra;

import hydra.client.core.HydraClient;
import hydra.client.core.HydraClientTcpSocketImpl;

public class ClientMain {

	public ClientMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		HydraClient hydraClient = new HydraClientTcpSocketImpl();
		hydraClient.open();
	}

}
