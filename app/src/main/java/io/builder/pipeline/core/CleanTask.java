package io.builder.pipeline.core;

import io.builder.core.Task;
import io.solar2d.log.Logger;
import io.solar2d.SolarEnvironment;
import io.builder.util.BinUtils;
import java.io.File;

public class CleanTask implements Task {
	@Override
	public void execute() {
		Logger.getInstance().log("task", "Очистка...");
		File dir = new File(SolarEnvironment.getExternalData());
		File[] files = dir.listFiles();
		for(File f: files) {
			if (f.isFile()) {
				f.delete();
				continue;
			}
			
			BinUtils.execute("rm -r " + f.getAbsolutePath());
		}
	}
}