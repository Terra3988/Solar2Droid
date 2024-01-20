package io.solar2d.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import android.widget.TextView;
import io.solar2d.R;

public class GuessDialog extends Dialog {
	
	public interface Callback {
		public void onOkClick();
		public void onCancelClick();
	}
	
	private Context mContext;
	private Callback callback;
	
	// Data
	private String title;
	private String description;
	
	public GuessDialog(Context pContext) {
		super(pContext);
		
		this.mContext = pContext;
	}
	
	public void setCallback(Callback c) {
		this.callback = c;
	}
	
	public void setTitle(String str) {
		this.title = str;
	}
	
	public void setDescription(String str) {
		this.description = str;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_guess);
		
		TextView wTitle = findViewById(R.id.guess_title);
		TextView wDescription = findViewById(R.id.guess_description);
		
		if (title != null)
			wTitle.setText(title);
		if (description != null)
			wDescription.setText(description);
			
		View cancel = findViewById(R.id.guess_cancel);
		View ok = findViewById(R.id.guess_ok);
		
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (callback != null)
					callback.onCancelClick();
			}
		});
		
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (callback != null)
					callback.onOkClick();
			}
		});
	}
	
	
}