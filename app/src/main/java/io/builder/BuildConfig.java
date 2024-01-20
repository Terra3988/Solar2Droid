package io.builder;

import io.builder.android.UsesFeature;
import io.builder.android.UsesPermission;
import io.builder.plugin.Plugin;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BuildConfig {
	public String appName;
	public String appPkg;
	public String appVCode;
	public String appVName;
	
	public File sourceDirectory;
	
	// Manifest config
	public String defaultOrientation = "portrait";
	public List<String> supportedOrientations = new ArrayList<>();
	
	public List<UsesPermission> usesPermission = new ArrayList<>();
	public List<UsesFeature> usesFeature = new ArrayList<>();
	
	// Dex config
	public List<Plugin> plugins = new ArrayList<>();
}