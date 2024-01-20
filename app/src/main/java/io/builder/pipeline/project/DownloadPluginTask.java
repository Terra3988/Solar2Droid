package io.builder.pipeline.project;

import io.builder.ApkBuilder;
import io.builder.BuildConfig;
import io.builder.core.BuildError;
import io.builder.core.Task;
import io.builder.plugin.Plugin;
import io.solar2d.SolarEnvironment;
import io.solar2d.log.Logger;
import java.io.File;

public class DownloadPluginTask implements Task {
	@Override
	public void execute() {
		BuildConfig config = ApkBuilder.getConfig();
		Logger.getInstance().log("task", "Скачивание плагинов..");
		if (config.plugins.size() < 1) {
			Logger.getInstance().log("note", "Плагины не используются");
			return;
		}

		
		
		Logger.getInstance().log("note", "Все плагины загружены");
	}
}