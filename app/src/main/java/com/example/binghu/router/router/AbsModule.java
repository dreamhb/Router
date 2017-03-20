package com.example.binghu.router.router;

import android.app.Activity;

/**
 * Created by binghu on 3/17/17.
 */

public abstract class AbsModule extends Activity implements IModule {

	@Override
	public <T> void createURL(String module, String handle, T param) {

	}

}
