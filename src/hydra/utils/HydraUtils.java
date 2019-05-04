package hydra.utils;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.sun.jna.platform.mac.SystemB;

import hydra.HydraMain;

public class HydraUtils {
	public static int getHydraPid() {
		int pid = -1;
		try {
			String pid_host = ManagementFactory.getRuntimeMXBean().getName();
			pid = Integer.parseInt(pid_host.split("@")[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pid;

	}

	public static String getCMDOutput(String[] commands) {
		String[] command = commands;

		String stdout;
		try {
			Process process = new ProcessBuilder(command).start();
			// String stderr = IOUtils.toString(process.getErrorStream(),
			// Charset.defaultCharset());

			stdout = IOUtils.toString(process.getInputStream(), Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			stdout = e.getMessage();
		}

		return stdout;
	}

	public static List<Integer> getAllHydraPid() {

		ArrayList<Integer> pids = new ArrayList<Integer>();

		Optional<String> java_home = Optional.ofNullable(System.getProperty("java.home"));
		String java_path = java_home.orElseGet(() -> {
			String[] command = { "where", "java" };
			String reString = HydraUtils.getCMDOutput(command);

			String[] paths = reString.split("\n");
			return paths.length > 0 ? paths[0] : "";
		});

		File javaExecFile = new File(java_path);

		Path jpsPath = Paths.get(javaExecFile.getParent(), "bin", "jps");

		String[] command = { jpsPath.toString(), "-l", "-m" };

		String[] processes = HydraUtils.getCMDOutput(command).split("\n");

		for (String raw : processes) {
			if (raw.toLowerCase().contains("hydraclient.jar")) {

				int pid = Integer.parseInt(raw.split(" ")[0]);
				pids.add(pid);
			}

		}

		return pids;

	}

	public static String getRunningJarName() {
		String hydraJarName = new java.io.File(
				HydraMain.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();

		return hydraJarName == null ? "unknown" : hydraJarName;
	}

	public static boolean isLaunchByJar() {
		String hydraJarName = HydraUtils.getRunningJarName();

		return hydraJarName.toString().toLowerCase().contains("hydraclient.jar") ? true : false;
	}

	public static boolean renameCurrentJar() {

		String hydraJarName = HydraUtils.getRunningJarName();

		File currentJar = new File(FilenameUtils.concat(System.getProperty("user.dir"), hydraJarName));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hh.mm.ss");
		File latencyJar = new File(FilenameUtils.concat(System.getProperty("user.dir"),
				sdf.format(Calendar.getInstance().getTime()) + "." + hydraJarName));
		System.out.println("trying to rename the jar : " + currentJar.getName() + " to " + latencyJar.getName());

		boolean isSuccess = false;
		try {
			FileUtils.moveFile(currentJar, latencyJar);
			isSuccess = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isSuccess;
	}

	public static boolean downloadNewClient(String url, String newFileName) {

		File currentJar = new File(FilenameUtils.concat(System.getProperty("user.dir"), newFileName));
		boolean isOK = false;
		try {

			System.out.println("Downloading files...");
			java.net.URL fileUrl = new java.net.URL(url);
			FileUtils.copyURLToFile(fileUrl, currentJar, 10000, 10000);
			isOK = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isOK;
	}

}