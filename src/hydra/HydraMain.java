package hydra;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import hydra.hydra.core.HydraClientTcpSocketImpl;
import hydra.hydra.core.HydraConfig;
import hydra.hydra.core.HydraController;

public class HydraMain {

	public HydraMain() {

	}

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

		HydraConfig hydraConfig = new HydraConfig();
		hydraConfig.setGUI_type(HydraConfig.Swing);
		hydraConfig.app_name = "Hydra (JanuxHsu Dev 1.0)";
		hydraConfig.zolaHost = serverHost;
		hydraConfig.zolaPort = serverPort;

		HydraController hydraController = new HydraController(hydraConfig);
		// HydraClient hydraClient = new HydraClientTcpSocketImpl(hydraController);
		new HydraClientTcpSocketImpl(hydraController);

	}

}
