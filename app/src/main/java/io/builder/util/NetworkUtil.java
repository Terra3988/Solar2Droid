package io.builder.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class NetworkUtil {
	public static void downloadFile(String inUrl, File outFile) throws IOException {
		URL url = new URL(inUrl);
		InputStream is = url.openStream();
		Path outPath = Paths.get(outFile.getAbsolutePath());
		Files.copy(is, outPath, StandardCopyOption.REPLACE_EXISTING);
	}
}