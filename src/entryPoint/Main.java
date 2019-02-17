package entryPoint;

import HydraServerCore.HydraServer;
import HydraServerCore.HydraTCPSocketImpl;

public class Main {

	public Main() {

	}

	public static void main(String[] args) {

		HydraServer hydraServer = new HydraTCPSocketImpl("Hydra Server");
		hydraServer.open();

	}

}
