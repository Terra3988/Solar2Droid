package io.builder.pipeline.android;

import io.builder.core.Task;
import io.solar2d.SolarEnvironment;
import io.solar2d.log.Logger;
import io.builder.util.BinUtils;
import io.builder.util.CommandBuilder;
import java.io.File;
import net.lingala.zip4j.ZipFile;

public class AaptTask implements Task {
	@Override
	public void execute() {
		Logger.getInstance().log("TASK", "Компиляция ресурсов...");
		File eData = new File(SolarEnvironment.getExternalData());
		File res = new File(eData, "res");
		File manifest = new File(eData, "AndroidManifest.xml");
		File jar = new File(eData, "android.jar");
		File nLibs = new File(SolarEnvironment.getNativeLibDir());
		File aapt = new File(nLibs, "libaapt.so");
		File out = new File(eData, "gen.apk.res");
		
		CommandBuilder builder = new CommandBuilder();
		
		builder.addArg(aapt.getAbsolutePath());
		builder.addArgs("package", "-v", "-f", "-m");
		builder.addArgs("--auto-add-overlay", "--no-version-vectors");
		builder.addArgs("-S", res.getAbsolutePath());
		builder.addArgs("-M", manifest.getAbsolutePath());
		builder.addArgs("-I", jar.getAbsolutePath());
		builder.addArgs("-F", out.getAbsolutePath());
		
		BinUtils.execute(builder.build());
		
		ZipFile gen = new ZipFile(out);
		try {
			gen.extractAll(new File(eData, "build").getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}