package com.example.binghu.router.module;

import android.content.Context;
import android.os.Bundle;

import com.example.binghu.router.router.AbstractModule;
import com.example.binghu.router.router.error.RouterErr;

public class ModuleAM extends AbstractModule {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public int intercept(Context context) {
		return RouterErr.OK;
	}


	//if you need specified error, handle here
	@Override
	public void handleError(Context context, RouterErr routerErr) {
		super.handleError(context, routerErr);
	}
}
