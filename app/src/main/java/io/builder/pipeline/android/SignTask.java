package io.builder.pipeline.android;

import io.builder.core.BuildError;
import io.builder.core.Task;
import io.solar2d.SolarEnvironment;
import io.solar2d.log.Logger;
import java.io.File;
import kellinwood.security.zipsigner.ZipSigner;

public class SignTask implements Task {
	@Override
	public void execute() {
		Logger.getInstance().log("TASK", "Подпись .apk...");
		File eData = new File(SolarEnvironment.getExternalData());
		
		File unsigned = new File(eData, "unsigned.apk");
		File signed = new File(eData, "signed.apk");
		try {
			ZipSigner signer = new ZipSigner();
			signer.setKeymode("testkey");
			signer.signZip(unsigned.getAbsolutePath(), signed.getAbsolutePath());
			Logger.getInstance().log("note", "Сборка успешно завершена");
		} catch (Exception e) {
			throw new BuildError(e.getMessage());
		}
	}
}