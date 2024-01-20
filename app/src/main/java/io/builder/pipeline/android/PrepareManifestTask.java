package io.builder.pipeline.android;

import android.util.Log;
import io.builder.ApkBuilder;
import io.builder.BuildConfig;
import io.builder.core.BuildError;
import io.builder.core.Task;
import io.solar2d.SolarEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Scanner;

public class PrepareManifestTask implements Task {
	
	private BuildConfig config;
	
	public PrepareManifestTask(BuildConfig cfg) {
		config = cfg;
	}
	
	@Override
	public void execute() {
		File eData = new File(SolarEnvironment.getExternalData());
		File manifest = new File(eData, "AndroidManifest.xml");
		
		String supportedOrientations = "";
		
		for(String orient: config.supportedOrientations) {
			supportedOrientations += orient;
			supportedOrientations += "|";
		}
		
		supportedOrientations = supportedOrientations.substring(0, supportedOrientations.length() - 1);
		
		try {
			InputStream is = new FileInputStream(manifest);
			Scanner sc = new Scanner(is).useDelimiter("\\A");
			String mSrc = sc.next();
			
			mSrc = mSrc.replaceAll("APP_PKG", config.appPkg);
			mSrc = mSrc.replaceAll("APP_VCODE", config.appVCode);
			mSrc = mSrc.replaceAll("APP_VNAME", config.appVName);
			mSrc = mSrc.replaceAll("APP_NAME", config.appName);
			mSrc = mSrc.replaceAll("DEFAULT_ORIENTATION", config.defaultOrientation);
			mSrc = mSrc.replaceAll("SUPPORTED_ORIENTATIONS", supportedOrientations);
			
			FileWriter writer = new FileWriter(manifest);
			writer.write(mSrc);
			writer.flush();
			
		} catch (Exception e) {
			throw new BuildError(e.getMessage());
		}
	}
}