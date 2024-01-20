package io.builder.pipeline.project;

import android.widget.Toast;
import io.builder.ApkBuilder;
import io.builder.BuildConfig;
import io.builder.android.UsesPermission;
import io.builder.core.BuildError;
import io.builder.core.Task;
import io.builder.plugin.Plugin;
import io.solar2d.log.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

public class ConfigurationTask implements Task {
	@Override
	public void execute() {
		Logger.getInstance().log("task", "Конфигурация проекта...");
		
		BuildConfig config = ApkBuilder.getConfig();
		File settings = new File(config.sourceDirectory, "build.settings");
		if(!settings.exists())
			throw new BuildError("Файл build.settings не найден");
		
		
		try {
			// Загрузка build.settings
			String source = readFile(settings);
			Globals globals = JsePlatform.standardGlobals();
			globals.load(source).call();
			
			// Парсинг
			LuaTable lSettings = (LuaTable) globals.get("settings");
			// Orientation
			LuaTable lOrientation = (LuaTable) lSettings.get("orientation");
			if (!lOrientation.isnil()) {
				LuaValue dOrient = lOrientation.get("default");
				if(!dOrient.isnil()) {
					config.defaultOrientation = dOrient.toString();
				}
				
				LuaTable sOrient = (LuaTable) lOrientation.get("supported");
				if(!sOrient.isnil()) {
					for(int i = 0; i < sOrient.length(); i++) {
						String orient = sOrient.get(i + 1).toString();
						config.supportedOrientations.add(orient);
					}
				}
			}
			
			// Android
			LuaTable lAndroid = (LuaTable) lSettings.get("android");
			if(!lAndroid.isnil()) {
				LuaTable lUsesPermissions = (LuaTable) lAndroid.get("usesPermissions");
				if(!lUsesPermissions.isnil()) {
					for(int i = 0; i < lUsesPermissions.length(); i++) {
						String permission = lUsesPermissions.get(i + 1).toString();
						UsesPermission u = new UsesPermission();
						u.name = permission;
						config.usesPermission.add(u);
					}
				}
			}
			
			// Plugins
			LuaTable lPlugins = (LuaTable) lSettings.get("plugins");
			if(!lPlugins.isnil()) {
				for(LuaValue key : lPlugins.keys()) {
					LuaTable plugin = (LuaTable) lPlugins.get(key);
					LuaValue publisher = plugin.get("publisherId");
					
					Plugin plugin1 = new Plugin();
					plugin1.name = key.toString();
					plugin1.author = publisher.toString();
					
					config.plugins.add(plugin1);
					
				}
			}
			
		} catch (Exception e) {
			throw new BuildError("Ошибка конфигурации проекта " + e.getMessage());
		}
	}
	
	private String readFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		Scanner scanner = new Scanner(is).useDelimiter("\\A");
		return scanner.hasNext() ? scanner.next() : "";
	}
}