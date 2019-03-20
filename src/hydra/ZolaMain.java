package hydra;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import hydra.zola.core.ZolaServer;
import hydra.zola.core.ZolaServerTcpSocketImpl;
import hydra.zola.gui.ZolaServerGui;
import hydra.zola.gui.ZolaServerSwingGui;

public class ZolaMain {

	public static void main(String[] args) {

		Options options = new Options();

		Option portParam = new Option("p", "port", true, "Service Port");
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

		
		String serverPort = cmd.getOptionValue("p");

		ZolaServerGui gui = new ZolaServerSwingGui();
		ZolaServer hydraServer = new ZolaServerTcpSocketImpl("Hydra Server", gui);
		hydraServer.setPort(serverPort);
		hydraServer.open();

	}
}
