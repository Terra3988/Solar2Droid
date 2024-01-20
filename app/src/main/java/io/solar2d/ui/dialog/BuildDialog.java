package io.solar2d.ui.dialog;
import android.os.Bundle;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import io.builder.ApkBuilder;
import io.solar2d.R;
import io.solar2d.filepicker.FilePicker;
import java.io.File;

public class BuildDialog extends Dialog implements View.OnClickListener {
	
	private Context mContext;
	
	private EditText wAppName;
	private EditText wPkg;
	private EditText wVCode;
	private EditText wVName;
	
	private TextView wProjectPath;
	private View wPickFile;
	private View wBuild;
	
	private File pickedFile;
	
	public BuildDialog(Context pContext) {
		super(pContext);
		this.mContext = pContext;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dialog_build);
		
		wAppName = findViewById(R.id.dialog_build_name_field);
		wPkg = findViewById(R.id.dialog_build_pkg_field);
		wVCode = findViewById(R.id.dialog_build_vcode_field);
		wVName = findViewById(R.id.dialog_build_vname_field);
		
		wProjectPath = findViewById(R.id.dialog_build_project_path);
		wPickFile = findViewById(R.id.dialog_build_pick);
		wBuild = findViewById(R.id.dialog_build_build);
		
		wPickFile.setOnClickListener(this);
		wBuild.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()) {
			case R.id.dialog_build_build:
				build();
				break;
			case R.id.dialog_build_pick:
				FilePicker filePicker = new FilePicker(mContext);
				filePicker.setFileFilter(new FilePicker.FileFilter() {
					@Override
					public boolean filter(File file) {
					    return file.isDirectory();
					}
				});
				filePicker.setOnFilePickedListener(new FilePicker.OnFilePickedListener() {
					@Override
					public void onFilePicked(File pickedFil) {
						pickedFile = pickedFil;
						wProjectPath.setText(pickedFile.getAbsolutePath());
					}
				});
				filePicker.show();
				break;
		}
	}
	
	// Building
	
	private void build() {
		String appName = wAppName.getText().toString();
		if(!validInput(appName)) {
			Toast.makeText(mContext, "Измените название приложения", 0).show();
			return;
		}
		String pkg = wPkg.getText().toString();
		if(!validPackage(pkg)) {
			Toast.makeText(mContext, "Измените пакет приложения", 0).show();
			return;
		}
		String vcode = wVCode.getText().toString();
		String vname = wVName.getText().toString();
		
		ApkBuilder builder = new ApkBuilder(pickedFile);
		
		builder.setAppName(appName);
		builder.setAppPkg(pkg);
		builder.setAppVersionCode(vcode);
		builder.setAppVersionName(vname);
		
		BuildLogDialog buildLogDialog = new BuildLogDialog(mContext);
		buildLogDialog.show();
		
		builder.build();
		dismiss();
	}
	
	private boolean validInput(String input) {
		if(input.length() < 3)
			return false;
		return true;
	}
	
	private boolean validPackage(String pkg) {
		if (pkg.length() < 3) {
			return false;
		}
		if (!pkg.contains(".")) {
			return false;
		}
		String[] parts = pkg.split("\\.");
		if(parts.length < 2) {
			return false;
		}
		for(int i = 0; i < parts.length; i++) {
			String part = parts[i];
			if(part.length() < 1) {
				return false;
			}
			if(!Character.isLetter(part.charAt(0))) {
				return false;
			}
		}
		
		return true;
	}
}