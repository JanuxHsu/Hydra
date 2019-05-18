package hydra_framework.main_start;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import hydra_framework.zola.core.ZolaConfig;
import hydra_framework.zola.core.ZolaController;
import hydra_framework.zola.core.ZolaServer;
import hydra_framework.zola.core.ZolaServerTcpSocketImpl;
import hydra_framework.zola.core.ZolaConfig.GUI_Type;


public class ZolaMain {

	public static void main(String[] args) {

		Options options = new Options();

		Option portParam = new Option("p", "port", true, "Service Port");
		portParam.setRequired(true);
		options.addOption(portParam);

		Option webPortParam = new Option("wp", "webport", true, "Http Service Port");
		webPortParam.setRequired(true);
		options.addOption(webPortParam);

		Option hydraCurrentVersion = new Option("hv", "hydraClient Ver.", true, "hydraClient Version");
		hydraCurrentVersion.setRequired(false);
		options.addOption(hydraCurrentVersion);

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
		String serverHttpPort = cmd.getOptionValue("wp");
		String hydraClientVersion = cmd.getOptionValue("hv");

		ZolaConfig zolaConfig = new ZolaConfig();
		if (hydraClientVersion != null) {
			ZolaConfig.hydraClientVersion = hydraClientVersion;
		}

		zolaConfig.servicePort = Integer.valueOf(serverPort);
		zolaConfig.httpServicePort = Integer.valueOf(serverHttpPort);
		zolaConfig.app_name = "Hydra Server (JanuxHsu) Dev " + ZolaConfig.zolaVersion;
		zolaConfig.setGUI_type(GUI_Type.Swing);

		ZolaController zolaController = new ZolaController(zolaConfig);

		ZolaServer zolaServer = new ZolaServerTcpSocketImpl(zolaController);
		zolaServer.open();
	}
}
