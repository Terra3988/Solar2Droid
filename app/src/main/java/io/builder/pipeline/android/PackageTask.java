package io.builder.pipeline.android;

import io.builder.core.Task;
import io.solar2d.SolarEnvironment;
import io.solar2d.log.Logger;
import java.io.File;
import net.lingala.zip4j.ZipFile;

public class PackageTask implements Task {
	@Override
	public void execute() {
		Logger.getInstance().log("TASK", "Упаковка .apk...");
		File eData = new File(SolarEnvironment.getExternalData());
		File build = new File(eData, "build");
		ZipFile apk = new ZipFile(new File(eData, "unsigned.apk"));
		
		File[] files = build.listFiles();
		try {
			for(File f : files) {
				if(f.isDirectory()) {
					apk.addFolder(f);
				} else {
					apk.addFile(f);
				}
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}