package hydra.utils;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

public class HydraUtils {
	public static int getHydraPid() {
		int pid = -1;
		try {
			String pid_host = ManagementFactory.getRuntimeMXBean().getName();
			System.out.println(pid_host);
			pid = Integer.parseInt(pid_host.split("@")[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pid;

	}

	public static int[] getAllHydraPid() {

		try {
			String[] command = { "jps", "-l", "-m" };
			Process process = new ProcessBuilder(command).start();
			// String stderr = IOUtils.toString(process.getErrorStream(),
			// Charset.defaultCharset());

			String stdout = IOUtils.toString(process.getInputStream(), Charset.defaultCharset());

			String[] processes = stdout.split("\n");

			for (String raw : processes) {
				System.out.println(raw);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}
}
