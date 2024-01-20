package io.solar2d.ui.dialog;
import android.os.Bundle;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.solar2d.R;
import io.solar2d.log.Logger;
import io.solar2d.ui.MenuActivity;

public class BuildLogDialog extends Dialog implements Logger.OnLogListener {
	
	private Context mContext;
	
	private LinearLayout container;
	
	public BuildLogDialog(Context pContext) {
		super(pContext);
		this.mContext = pContext;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_build_log);
		Logger.getInstance().setOnLogListener(this);
		container = findViewById(R.id.dialog_build_log_container);
	}
	
	@Override
	public void onNewLog(String tag, String log) {
		MenuActivity.master.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				TextView wLog = (TextView) inflater.inflate(R.layout.build_log_row, container, false);
				wLog.setText("> " + tag + ": " + log);
		
				container.addView(wLog);
			}
		});
	}
}