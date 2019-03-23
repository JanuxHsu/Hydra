package hydra;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import hydra.viper.core.ViperClient;
import hydra.viper.core.ViperClientTcpSocketImpl;
import hydra.viper.core.ViperConfig;
import hydra.viper.core.ViperController;

public class ViperMain {
	public static void main(String[] args) {
		
		Options options = new Options();

		Option serverParam = new Option("s", "server", true, "Status Report Server Host");
		serverParam.setRequired(true);
		options.addOption(serverParam);

		Option portParam = new Option("p", "port", true, "Status Report Server Port");
		portParam.setRequired(true);
		options.addOption(portParam);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("--", options);

			System.exit(1);
		}

		String serverHost = cmd.getOptionValue("s");
		String serverPort = cmd.getOptionValue("p");
		
		ViperConfig viperConfig = new ViperConfig();
		viperConfig.setGUI_type(ViperConfig.Swing);
		viperConfig.zolaHost = serverHost;
		viperConfig.zolaPort = serverPort;

		ViperController viperController = new ViperController(viperConfig);
		ViperClient viperClient = new ViperClientTcpSocketImpl(viperController);
		// viperClient.open();
		viperController.connectToTarget();
		viperClient.registerClient();
	}
}
