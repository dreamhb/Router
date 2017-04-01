package com.example.binghu.router.router;

import android.content.Context;

import com.example.binghu.router.router.error.RouterErr;

import java.util.Map;

/**
 * Created by binghu on 3/17/17.
 */

public interface IModule {

	/**
	 * convert all params to url
	 * @param context
	 * @param module
	 * @param handle
	 * @param param
	 */
	void createURL(Context context, String module, String handle, Map<String, Object> param);


	/**
	 * convert url params to url need result
	 * @param context
	 * @param module
	 * @param handle
	 * @param param
	 * @param callback
	 */
	void createURL(Context context, String module, String handle, Map<String, Object> param,
	               ResultCallback callback);

	/**
	 * intercept for router request
	 * @param context module implements this method, should use this context
	 * @return error code, ok if no error
	 */
	int intercept(Context context);

	/**
	 * handle error
	 * @param routerErr
	 */
	void handleError(Context context, RouterErr routerErr);
}
