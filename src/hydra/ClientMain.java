package hydra;

import java.io.IOException;

import hydra.viper.core.ViperClient;
import hydra.viper.core.ViperClientTcpSocketImpl;
import hydra.viper.gui.ViperClientGui;
import hydra.viper.gui.ViperClientSwingGui;

public class ClientMain {

	public ClientMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		ViperClientGui viperClientGui = new ViperClientSwingGui();
		ViperClient viperClient = new ViperClientTcpSocketImpl("Viper", viperClientGui);
		viperClient.open();

//		List<String> commands = new ArrayList<String>();
//		commands.add("ping");
//		commands.add("localhost");
//		commands.add("-c");
//		commands.add("3");
//		ProcessBuilder builder = new ProcessBuilder(commands);
//		builder.directory(new File("/Users/janux/Desktop/UltraFinder")); // this is where you set the root folder for the
//		// executable to run with
//		builder.redirectErrorStream(true);
//		Process process = builder.start();
//
//		Scanner s = new Scanner(process.getInputStream());
//		StringBuilder text = new StringBuilder();
//
//		String zString = null;
//		while (s.hasNextLine() && (zString = s.nextLine()) != null) {
////			text.append(s.nextLine());
////			text.append("\n");
//
//			System.out.println(zString);
//		}
		// s.close();

		// int result = process.waitFor();

		// System.out.printf("Process exited with result %d and output %s%n", result,
		// text);

	}

}
