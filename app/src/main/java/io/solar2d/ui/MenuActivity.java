package io.solar2d.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.os.Environment;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import io.builder.ApkBuilder;
import io.git.api.CloneCommand;
import io.git.api.ProgressMonitor;
import io.solar2d.R;
import io.solar2d.BuildConfig;
import io.solar2d.SolarEnvironment;
import io.solar2d.filepicker.FilePicker;
import io.solar2d.log.Logger;
import io.solar2d.ui.dialog.BuildDialog;
import io.solar2d.ui.dialog.BuildLogDialog;
import io.solar2d.ui.dialog.GuessDialog;
import io.builder.util.FileUtil;
import java.io.File;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

	public static MenuActivity master;

	private Context mContext;

	private View wCreate;
	private View wBuild;
	private View wDocs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		master = this;
		mContext = this;

		setContentView(R.layout.activity_main);

		wCreate = findViewById(R.id.menu_create);
		wBuild = findViewById(R.id.menu_build);
		wDocs = findViewById(R.id.menu_docs);

		wCreate.setOnClickListener(this);
		wBuild.setOnClickListener(this);
		wDocs.setOnClickListener(this);
		
		TextView wVersion = findViewById(R.id.menu_app_version);
		wVersion.setText(BuildConfig.VERSION_NAME + " - " + BuildConfig.BUILD_TYPE);
	}

	@Override
	protected void onStart() {
		super.onStart();

		getPermissions();
		checkPluginBase();
	}
	
	private void checkPluginBase() {
		File internal = new File(SolarEnvironment.getExternalData());
		File dbDir = new File(internal, "plugins");
		if(!dbDir.exists()) {
			GuessDialog dialog = new GuessDialog(mContext);
			
			dialog.setTitle("Подтверждение");
			dialog.setDescription("Для работы плагинов требуется скачать их базу данных");
			dialog.setCallback(new GuessDialog.Callback() {
				@Override
				public void onOkClick() {
					BuildLogDialog d = new BuildLogDialog(mContext);
					d.show();
					dbDir.mkdir();
					CloneCommand clone = new CloneCommand();
					clone.setGitToken(SolarEnvironment.GIT_TOKEN);
					clone.setRepo("solar2d", "plugins.solar2d.com");
					clone.setDirectory(dbDir);
					clone.setCopyDir("plugins");
					clone.setProgressMonitor(new ProgressMonitor() {
						@Override
						public void onStart() {
							Logger.getInstance().log("note", "Скачивание началось");
						}

						@Override
						public void onUpdate(int completed) {
							Logger.getInstance().log("note", "Скачано " + completed + "%");
						}

						@Override
						public void onFinish() {
							Logger.getInstance().log("note", "Скачивание базы завершено");
						}
						
					});
					clone.call();
				}

				@Override
				public void onCancelClick() {
				}
			});
			
			dialog.show();
		}
	}

	public void create(String path) {
		File dir = new File(path);
		File zip = new File(dir, "project.zip");
		FileUtil.copyAsset("project_template.zip", zip);
		ZipFile zipFile = new ZipFile(zip);
		try {
			zipFile.extractAll(dir.getAbsolutePath());
		} catch (ZipException e) {
			e.printStackTrace();
		}

		zip.delete();

		Toast.makeText(mContext, "Проект успешно создан", 0).show();
	}

	@Override
	public void onClick(View view) {
		FilePicker dialog;
		BuildDialog buildDialog;
		switch (view.getId()) {
		case R.id.menu_create:
			dialog = new FilePicker(mContext);
			dialog.setFileFilter(new FilePicker.FileFilter() {
				@Override
				public boolean filter(File file) {
				    return file.isDirectory();
				}
			});
			dialog.setOnFilePickedListener(new FilePicker.OnFilePickedListener() {
				@Override
				public void onFilePicked(File pickedFile) {
					create(pickedFile.getAbsolutePath());
				}
			});
			dialog.show();
			break;
		case R.id.menu_build:
			buildDialog = new BuildDialog(mContext);
			buildDialog.show();
			break;
		case R.id.menu_docs:
			Intent i = new Intent(mContext, DocsBrowserActivity.class);
			startActivity(i);
			break;
		}
	}

	private void getPermissions() {
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
			if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				return;
		} else {
			if (Environment.isExternalStorageManager())
				return;
		}

		GuessDialog guessDialog = new GuessDialog(mContext);

		guessDialog.setTitle("Подтверждение");
		guessDialog.setDescription("Для работы приложения требуется " + "разрешение на работу с файлами");

		guessDialog.setCallback(new GuessDialog.Callback() {
			@Override
			public void onOkClick() {
				try {
					ActivityCompat.requestPermissions(MenuActivity.this,
							new String[] { Manifest.permission.READ_EXTERNAL_STORAGE,
									Manifest.permission.WRITE_EXTERNAL_STORAGE,
									Manifest.permission.MANAGE_EXTERNAL_STORAGE },
							100);
					Intent i = new Intent();
					if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
						i.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
					} else {
						i.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
					}
					startActivity(i);
				} catch (Exception e) {
					Toast.makeText(mContext, e.getMessage(), 1).show();
				}
			}

			@Override
			public void onCancelClick() {
				Toast.makeText(mContext, "Приложению требуется разрешение", 0).show();
				finish();
			}
		});

		guessDialog.show();
	}

}