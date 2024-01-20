package io.solar2d;

import android.app.Application;
import java.io.FileInputStream;

public class SolarApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		
		SolarEnvironment.setAppContext(getApplicationContext());
	}
}