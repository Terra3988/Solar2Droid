package io.builder.pipeline.core;

import io.builder.core.Task;
import io.solar2d.SolarEnvironment;
import io.builder.util.FileUtil;
import java.io.File;

public class CopyTask implements Task {
	@Override
	public void execute() {
		File eData = new File(SolarEnvironment.getExternalData());
		
		File androidJar = new File(eData, "android.jar");
		FileUtil.copyAsset("android.jar", androidJar);
		
		File resZip = new File(eData, "res.zip");
		FileUtil.copyAsset("res.zip", resZip);
		
		File baseApk = new File(eData, "base.apk");
		FileUtil.copyAsset("base.apk", baseApk);
		
		File manifest = new File(eData, "AndroidManifest.xml");
		FileUtil.copyAsset("template_manifest.xml", manifest);
	}
}