package hydra;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import hydra.zola.core.ZolaConfig;
import hydra.zola.core.ZolaConfig.GUI_Type;
import hydra.zola.core.ZolaController;
import hydra.zola.core.ZolaServer;
import hydra.zola.core.ZolaServerTcpSocketImpl;

public class ZolaMain {

	public static void main(String[] args) {

		Options options = new Options();

		Option portParam = new Option("p", "port", true, "Service Port");
		portParam.setRequired(true);
		options.addOption(portParam);

		Option webPortParam = new Option("wp", "webport", true, "Http Service Port");
		webPortParam.setRequired(true);
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

		String serverPort = cmd.getOptionValue("p");
		String serverHttpPort = cmd.getOptionValue("wp");

		ZolaConfig zolaConfig = new ZolaConfig();

		zolaConfig.servicePort = Integer.valueOf(serverPort);
		zolaConfig.httpServicePort = Integer.valueOf(serverHttpPort);
		zolaConfig.app_name = "Hydra Server (JanuxHsu) Dev 1.12";
		zolaConfig.setGUI_type(GUI_Type.Swing);

		ZolaController zolaController = new ZolaController(zolaConfig);

		// ZolaServerGui gui = new ZolaServerSwingGui();
//		ZolaServer hydraServer = new ZolaServerTcpSocketImpl("Hydra Server (JanuxHsu) Dev 1.1", gui);
//		hydraServer.setPort(serverPort);
//
//		hydraServer.open();
		ZolaServer zolaServer = new ZolaServerTcpSocketImpl(zolaController);
		zolaServer.open();
	}
}
