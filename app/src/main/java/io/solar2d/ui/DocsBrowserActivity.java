package io.solar2d.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import io.solar2d.R;

public class DocsBrowserActivity extends AppCompatActivity {
	
	private Context mContext;
	
	private WebView wView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.mContext = this;
		
		setContentView(R.layout.activity_docs);
		
		wView = findViewById(R.id.docs_webview);
		
		wView.getSettings().setJavaScriptEnabled(true);
		wView.setWebViewClient(new MyWebClient());
		
		wView.loadUrl("https://docs.coronalabs.com");
	}
	
	private class MyWebClient extends WebViewClient {
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (Uri.parse(url).getHost().equals("https://docs.coronalabs.com"))
				return true;
			else {
				return false;
			}
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			wView.setInitialScale(50);
		}
	}
}