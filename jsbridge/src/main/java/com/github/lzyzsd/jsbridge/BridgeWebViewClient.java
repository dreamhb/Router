package com.github.lzyzsd.jsbridge;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by bruce on 10/28/15.
 * when load web page finished,
 * load init.js, and in which we init EcoJsBridge
 */
public class BridgeWebViewClient extends WebViewClient {
	private BridgeWebView webView;
	private BridgeWebView.TitleCallback callback;

	public BridgeWebViewClient(BridgeWebView webView) {
		this.webView = webView;
	}

	public void setCallback(BridgeWebView.TitleCallback callback) {
		this.callback = callback;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		try {
			url = URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if (url.equals(BridgeUtil.JSB_INIT_BRIDGE)) {
			BridgeUtil.webViewLoadLocalJs(view, BridgeWebView.toLoadJs);
			return true;
		}
		if (url.startsWith(BridgeUtil.JSB_RETURN_DATA)) {
			webView.handlerReturnData(url);
			return true;
		} else if (url.startsWith(BridgeUtil.JSB_OVERRIDE_SCHEMA)) {
			webView.flushMessageQueue();
			return true;
		} else {
			return super.shouldOverrideUrlLoading(view, url);
		}
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);

	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);

		if (callback != null) {
			callback.onTitleGot(view.getTitle());
		}

		BridgeUtil.webViewLoadLocalJs(view, BridgeWebView.initJs);

		if (webView.getStartupMessage() != null) {
			for (Message m : webView.getStartupMessage()) {
				webView.dispatchMessage(m);
			}
			webView.setStartupMessage(null);
		}
	}

	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
	}

}