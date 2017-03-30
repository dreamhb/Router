package com.example.binghu.router.module;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.binghu.router.R;
import com.example.binghu.router.router.AbsModule;
import com.example.binghu.router.router.error.RouterErr;

public class ModuleAM extends AbsModule {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_module_am);

		intercept(this);
	}

	@Override
	public boolean intercept(Context context) {
		Log.e("MODULE", " module ==> " + ModuleAM.this);
//		Log.e("MODULE", " module ==> " + getApplicationContext());
//		Log.e("MODULE", " module ==> " + getBaseContext());
		return true;
	}


	//if you need specified error, handle here
	@Override
	public void handleError(RouterErr routerErr) {
		super.handleError(routerErr);
	}
}
