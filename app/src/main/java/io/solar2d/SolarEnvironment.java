package io.solar2d;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import io.solar2d.log.Logger;
import io.solar2d.ui.MenuActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import org.w3c.dom.NameList;

public class SolarEnvironment {
	
	private static Context appContext;
	private static String EXTERNAL_DATA;
	private static String INTERNAL_DATA;
	private static String INTERNAL_CACHE;
	private static String NATIVE_LIBS;
	public static final String GIT_TOKEN = "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";
	
	public static void setAppContext(Context aCtx) {
		appContext = aCtx;
		new Logger();
		EXTERNAL_DATA = appContext.getExternalFilesDir("solar2d").getAbsolutePath();
		INTERNAL_DATA = appContext.getDataDir().getAbsolutePath();
		INTERNAL_CACHE = appContext.getCacheDir().getAbsolutePath();
		NATIVE_LIBS = appContext.getApplicationInfo().nativeLibraryDir;
		try {
			File log = new File(appContext.getExternalFilesDir("logs"), "latest.txt");
			System.setOut(new PrintStream(new FileOutputStream(log)));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void installPackage(File apk) {
		Uri apkUri = FileProvider.getUriForFile(appContext, BuildConfig.APPLICATION_ID + ".provider", apk);
		Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
		intent.setData(apkUri);
		intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		MenuActivity.master.startActivity(intent);
	}
	
	public static Context getAppContext() {
		return appContext;
	}
	
	public static String getExternalData() {
		return EXTERNAL_DATA;
	}
	
	public static String getNativeLibDir() {
		return NATIVE_LIBS;
	}
	
	public static String getInternalData() {
		return INTERNAL_DATA;
	}
	
	public static String getInternalCache() {
		return INTERNAL_CACHE;
	}
}