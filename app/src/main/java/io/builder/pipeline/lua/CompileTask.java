package io.builder.pipeline.lua;

import io.builder.core.Task;
import io.solar2d.SolarEnvironment;
import io.solar2d.log.Logger;
import io.builder.util.BinUtils;
import io.builder.util.CommandBuilder;
import io.builder.util.FileUtil;
import java.io.File;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class CompileTask implements Task {
	@Override
	public void execute() {
		Logger.getInstance().log("TASK", "Компиляция скриптов...");
		File eData = new File(SolarEnvironment.getExternalData());
		File pDir = new File(eData, "project");
		
		File target = new File(SolarEnvironment.getExternalData() + "/project");
		Path sD = Paths.get(pDir.getAbsolutePath());
		Path tD = Paths.get(target.getAbsolutePath());

		try {
			Files.walkFileTree(sD, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if(FileUtil.getExtension(file.toFile()).equals("lua"))
						compile(file.toFile());
					return FileVisitResult.CONTINUE;
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void compile(File file) {
		File pDir = new File(SolarEnvironment.getExternalData(), "project");
		
		String fName = file.getAbsolutePath();
		fName = fName.replace(pDir.getAbsolutePath() + "/", "");
		fName = fName.replaceAll("/", ".");
		fName = fName.substring(0, fName.length() - 1);
		
		File nFile = new File(
				pDir,
				fName
			);
		
		File luac = new File(SolarEnvironment.getNativeLibDir(), "libluac.so");
		
		CommandBuilder builder = new CommandBuilder();
		
		builder.addArg(luac.getAbsolutePath());
		builder.addArgs("-o", nFile.getAbsolutePath());
		builder.addArg(file.getAbsolutePath());
		
		Logger.getInstance().log("LUAC", "Compiling: " + fName);
	//	Logger.getInstance().log("FIX", builder.build());
		
		BinUtils.execute(builder.build());
		
		file.delete();
	}
}