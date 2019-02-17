package entryPoint;

import HydraClientCore.HydraClient;
import HydraClientCore.HydraClientTCPSocketImpl;

public class ClientMain {

	public ClientMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		HydraClient hydraClient = new HydraClientTCPSocketImpl();
		hydraClient.open();
	}

}
