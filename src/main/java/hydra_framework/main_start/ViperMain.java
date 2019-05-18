package hydra_framework.main_start;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import hydra_framework.viper.core.ViperClient;
import hydra_framework.viper.core.ViperClientTcpSocketImpl;
import hydra_framework.viper.core.ViperConfig;
import hydra_framework.viper.core.ViperController;

public class ViperMain {
	public static void main(String[] args) {

		Options options = new Options();

		Option serverParam = new Option("s", "server", true, "Status Report Server Host");
		serverParam.setRequired(true);
		options.addOption(serverParam);

		Option portParam = new Option("p", "port", true, "Status Report Server Port");
		portParam.setRequired(true);
		options.addOption(portParam);

		Option webPortParam = new Option("wp", "webport", true, "Status Report Server Web api Port");
		portParam.setRequired(true);
		options.addOption(webPortParam);

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
		String serverWebAPIPort = cmd.getOptionValue("wp");

		ViperConfig viperConfig = new ViperConfig();
		viperConfig.setGUI_type(ViperConfig.ViperGUIType.Swing);
		viperConfig.zolaHost = serverHost;
		viperConfig.zolaPort = serverPort;
		viperConfig.zolaAPIPort = serverWebAPIPort;

		ViperController viperController = new ViperController(viperConfig);
		ViperClient viperClient = new ViperClientTcpSocketImpl(viperController);
		// viperClient.open();
		viperController.connectToTarget();
		viperClient.registerClient();
	}
}
