package com.example.binghu.router.router;

import android.app.Activity;

import com.example.binghu.router.router.error.RouterErr;

/**
 * Created by binghu on 3/17/17.
 */

public abstract class AbsModule extends Activity implements IModule {

	@Override
	public <T> void createURL(String module, String handle, T param) {

	}


	@Override
	public void handleError(RouterErr routerErr) {
		//base error handled here
	}
}
