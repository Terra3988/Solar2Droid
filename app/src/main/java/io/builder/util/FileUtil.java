package io.builder.util;

import android.content.res.AssetManager;
import io.solar2d.SolarEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class FileUtil {
	
	public static String readFileFully(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		Scanner reader = new Scanner(is).useDelimiter("\\A");
		return reader.hasNext() ? reader.next() : "";
	}
	
	public static String getExtension(File file) {
		String name = file.getName();
		if(name.contains(".")) {
			return name.substring(name.lastIndexOf('.') + 1, name.length());
		} else {
			return "";
		}
	}
	
	public static void mkdirs(File dir) {
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
	
	private void _rmdir(String path)
	{
		File dir = new File(path);
		File[] list = dir.listFiles();
		for(File file : list) {
			if (file.isDirectory()) {
				_rmdir(file.getAbsolutePath());
			} else {
				file.delete();
			}
		}
		
		dir.delete();
	}
	
	public void rm(String path)
	{
		File file = new File(path);
		if (file.exists()) {
			if (file.isDirectory()) {
				_rmdir(path);
			} else {
				file.delete();
			}
		}
	}
	
	public static void copyAsset(String aName, File fout) {
		copyAsset(aName, fout.getAbsolutePath());
	}
	
	public static void copyAsset(String assetFileName, String fout)
	{
		try {
			AssetManager assetManager = SolarEnvironment.getAppContext().getAssets();
			InputStream is = assetManager.open(assetFileName);
			new File(fout).createNewFile();
			OutputStream os = new FileOutputStream(fout);
			copyFile2(is, os);
			is.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void copyFile2(InputStream is, OutputStream os) throws IOException
	{
		byte[] buffer = new byte[1024];
		while (true) {
			int read = is.read(buffer);
			if (read != -1) {
				os.write(buffer, 0, read);
			} else {
				return;
			}
		}
	}
}