package io.builder.pipeline.core;

import io.builder.core.Task;
import io.solar2d.SolarEnvironment;
import io.solar2d.log.Logger;
import io.builder.util.FileUtil;
import java.io.File;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;


public class CopyAssets implements Task {
	
	private File workDir;
	
	public CopyAssets(File pdir) {
		this.workDir = pdir;
	}
	
	@Override
	public void execute() {
		Logger.getInstance().log("TASK", "Копирование ресурсов...");
		File target = new File(SolarEnvironment.getExternalData() + "/build/assets");
		Path sD = Paths.get(workDir.getAbsolutePath());
		Path tD = Paths.get(target.getAbsolutePath());

		try {
			Files.walkFileTree(sD, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					Path targetPath = tD.resolve(sD.relativize(dir));
					try {
						Files.copy(dir, targetPath, StandardCopyOption.COPY_ATTRIBUTES);
					} catch (FileAlreadyExistsException e) {
						
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					File f = file.toFile();
					if (!FileUtil.getExtension(f).equals("lua")) {
						Files.copy(file, tD.resolve(sD.relativize(file)), StandardCopyOption.COPY_ATTRIBUTES);
						f.delete();
					}
					return FileVisitResult.CONTINUE;
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}