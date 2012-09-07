package com.maveric;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Webview extends MavericBaseActiity {
	WebView webView;
	final Activity activity = this;

	@Override
	protected void setContentToLayout() {
		setContentView(R.layout.webview);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle web = getIntent().getExtras();
		String url = "http://new:new@" + web.getString("url");
		try {
			webView = (WebView) findViewById(R.id.webview);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.setWebChromeClient(new WebChromeClient() {
				public void onProgressChanged(WebView view, int progress) {
					activity.setTitle("Loading...");
					activity.setProgress(progress * 100);

					if (progress == 100)
						activity.setTitle(R.string.app_name);
				}
			});
			webView.setWebViewClient(new WebViewClient() {
				@Override
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
					// Handle the error
				}

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					return true;
				}
			});
			Log.i("manikk", url);
			webView.loadUrl(url);

		} catch (Exception e) {
			Log.i("manikk", url);

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
