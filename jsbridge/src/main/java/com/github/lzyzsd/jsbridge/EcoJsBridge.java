package com.github.lzyzsd.jsbridge;


public interface EcoJsBridge {
	
	public void send(String data);
	public void send(String data, CallBackFunction responseCallback);
	
}
