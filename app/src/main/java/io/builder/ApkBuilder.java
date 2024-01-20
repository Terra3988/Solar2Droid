package io.builder;

import android.Manifest;
import io.builder.pipeline.android.*;
import io.builder.pipeline.project.*;
import io.builder.pipeline.core.*;
import io.builder.pipeline.lua.*;
import io.builder.core.*;

import io.solar2d.log.Logger;
import io.solar2d.SolarEnvironment;

import java.io.File;

public class ApkBuilder {
	
	private File sourceDirectory;
	private static BuildConfig config;

	public ApkBuilder(File src) {
		this.sourceDirectory = src;
		config = new BuildConfig();
		config.sourceDirectory = src;
		
		Logger logger = new Logger();
	}
	
	public void setAppName(String appName) {
		config.appName = appName;
	}
	
	public void setAppPkg(String pkg) {
		config.appPkg = pkg;
	}
	
	public void setAppVersionCode(String code) {
		config.appVCode = code;
	}
	
	public void setAppVersionName(String name) {
		config.appVName = name;
	}
	
	public static BuildConfig getConfig() {
		return config;
	}
	
	public void build() {
		TaskExecutor executor = new TaskExecutor();
	
		executor.add(new CleanTask());
		executor.add(new ConfigurationTask());
		executor.add(new DownloadPluginTask());
		executor.add(new CopyTask());
		executor.add(new PrepareTask());
		executor.add(new CopyProject(sourceDirectory));
		executor.add(new CopyAssets(new File(SolarEnvironment.getExternalData() + "/project")));
		executor.add(new CompileTask());
		executor.add(new CarTask());
		executor.add(new PrepareManifestTask(config));
		executor.add(new AaptTask());
		executor.add(new PackageTask());
		executor.add(new SignTask());
		
		new Thread() {
			@Override
			public void run() {
				executor.execute();
	//			SolarEnvironment.installPackage(new File(SolarEnvironment.getExternalData() + "/signed.apk"));
			}
		}.start();
	}
}