package io.builder.pipeline.core;

import io.builder.core.Task;
import io.solar2d.SolarEnvironment;
import java.io.File;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class PrepareTask implements Task {
	@Override
	public void execute() {
		File eData = new File(SolarEnvironment.getExternalData());
		
		File p = new File(eData, "project");
		p.mkdir();
		
		File resZip = new File(eData, "res.zip");
		File res = new File(eData, "res");
		res.mkdir();
		
		ZipFile zip = new ZipFile(resZip);
		try {
			zip.extractAll(res.getAbsolutePath());
		} catch (ZipException e) {
			e.printStackTrace();
		}
		resZip.delete();
		
		File baseApk = new File(eData, "base.apk");
		File build = new File(eData, "build");
		build.mkdir();
		
		ZipFile zip2 = new ZipFile(baseApk);
		try {
			zip2.extractAll(build.getAbsolutePath());
		} catch (ZipException e) {
			e.printStackTrace();
		}
		baseApk.delete();
	}
}