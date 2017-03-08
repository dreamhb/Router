package com.example.binghu.router.router;

import android.net.Uri;

/**
 * Created by binghu on 3/6/17.
 * 拦截器，用来将请求重写，依据的是服务器的配置
 */

public interface RouterInterceptor {

	Uri getUri(Uri origin);

	void initRouterConfig();
}
