package com.example.binghu.router.router;

import android.content.Context;

/**
 * Created by binghu on 3/13/17.
 */

interface IRouter {

	void open(Context context, String url);

	void open(Context context, String url, ResultCallback resultCallback);

}
