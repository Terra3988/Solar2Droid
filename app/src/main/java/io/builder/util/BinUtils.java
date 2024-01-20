package io.builder.util;

import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class BinUtils {
	public static void setExecutable(File file) {
		if (!file.exists())
			return;
		
		try {
			file.setExecutable(true, true);
			Runtime.getRuntime().exec(
				"chmod 777 " +
				file.getAbsolutePath()
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String execute(String comm) {
		String[] command = comm.split(" ");
		StringBuilder out = new StringBuilder();
		
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			
			Process process = processBuilder.start();
			
			int code = process.waitFor();
			
			InputStream inputStream = process.getInputStream();
			InputStream err = process.getErrorStream();
			Scanner r = new Scanner(err).useDelimiter("\\A");
			int ch;
			while ((ch = inputStream.read()) != -1) {
				out.append((char) ch);
			}
			Log.w("BIN", "command: " + comm);
			Log.w("BIN", "code: " + code);
			Log.w("BIN", "log: " + (r.hasNext() ? r.next() : ""));
			return out.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}
}