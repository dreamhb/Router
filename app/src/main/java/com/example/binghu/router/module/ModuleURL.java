package com.example.binghu.router.module;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.binghu.router.R;
import com.example.binghu.router.router.ResultCallback;
import com.example.binghu.router.router.Router;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

public class ModuleURL extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String uri = Uri.decode(getIntent().getDataString());
		String url = Uri.parse(uri).getQueryParameter("url");

		setContentView(R.layout.activity_module_url);

		BridgeWebView webView = (BridgeWebView) findViewById(R.id.webview);

		webView.loadUrl(url);

		webView.registerHandler("pageJump", new BridgeHandler() {
			@Override
			public void handler(String data, final CallBackFunction function) {
				Router.INSTANCE.openUrl(ModuleURL.this, data, new ResultCallback() {
					@Override
					public void onResult(Bundle result) {
						Toast.makeText(ModuleURL.this, result.getString("tips"), Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
}
