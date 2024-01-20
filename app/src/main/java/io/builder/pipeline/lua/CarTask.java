package io.builder.pipeline.lua;

import io.builder.core.Task;
import io.solar2d.SolarEnvironment;
import io.builder.lib.CoronaArchiver;
import io.solar2d.log.Logger;
import io.builder.util.BinUtils;
import io.builder.util.CommandBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;



public class CarTask implements Task {
	@Override
	public void execute() {
		Logger.getInstance().log("TASK", "Архивирование скриптов...");
		File nLibs = new File(SolarEnvironment.getNativeLibDir());
		File eData = new File(SolarEnvironment.getExternalData());
		File assets = new File(eData, "build/assets");
		File resourcecar = new File(assets, "resource.car");
		File pDir = new File(eData, "project");
		File archiver = new File(nLibs, "libarchiver.so");
		File car = new File(nLibs, "libcar.so");
		File c__ = new File(nLibs, "libc++_shared.so");
		
		// Clean dirs
		File[] files = pDir.listFiles();
		for(File f: files) {
			if (f.isDirectory()) {
				BinUtils.execute("rm -r " + f.getAbsolutePath());
			}
		}
		File filestxt = new File(pDir, "files.txt");
		files = pDir.listFiles();
		try {
			FileOutputStream os = new FileOutputStream(filestxt);
			for(File file: files) {
				os.write(file.getAbsolutePath().getBytes(StandardCharsets.UTF_8));
				os.write("\n".getBytes(StandardCharsets.UTF_8));
			}
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		CommandBuilder builder = new CommandBuilder();
		
		builder.addArg(car.getAbsolutePath());
		builder.addArg(archiver.getAbsolutePath());
		builder.addArg(c__.getParentFile().getAbsolutePath());
		builder.addArg(filestxt.getAbsolutePath());
		builder.addArg(resourcecar.getAbsolutePath());
		
		BinUtils.execute(builder.build());
	}
}