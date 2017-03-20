package com.example.binghu.router.router;

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
	 * interceptor for router request
	 * @return
	 */
	boolean interceptor();
}
