package com.example.binghu.router.router;

import android.content.Context;

import com.example.binghu.router.router.error.RouterErr;

/**
 * Created by binghu on 3/17/17.
 */

public interface IModule {

	/**
	 * convert all params to url
	 * @param module
	 * @param handle
	 * @param param
	 * @param <T>
	 */
	<T> void createURL(String module, String handle, T param);

	/**
	 * intercept for router request
	 * @param context module implements this method, should use this context
	 * @return
	 */
	boolean intercept(Context context);

	/**
	 * handle error
	 * @param routerErr
	 */
	void handleError(RouterErr routerErr);
}
