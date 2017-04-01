package com.example.binghu.router.router;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.binghu.router.router.error.RouterErr;

import java.util.Map;

/**
 * Created by binghu on 3/17/17.
 */

public abstract class AbstractModule extends Activity implements IModule {

	private static final String TAG = AbstractModule.class.getSimpleName();

	@Override
	public  void createURL(Context context, String module, String handler,
	                       Map<String, Object> param) {
		createURL(context, module, handler, param, null);
	}

	@Override
	public void createURL(Context context, String module, String handler, Map<String, Object> param,
	                      ResultCallback callback) {
		if (context == null) {
			Log.e(TAG, " context cannot be null !");
			return;
		}

		if (TextUtils.isEmpty(module) || TextUtils.isEmpty(handler)) {
			Log.e(TAG, " module or handler cannot be null or empty !");
			return;
		}

		String url = createUri(module, handler, param);
		Router.INSTANCE.open(context, url, callback);
	}

	@Override
	public void handleError(Context context, RouterErr routerErr) {
		//base error handled here
		switch (routerErr.getErrCode()) {
			case RouterErr.ERR_URL_INVALID:
				Toast.makeText(context, routerErr.getErrMsg(), Toast.LENGTH_SHORT).show();
				break;
			case RouterErr.ERR_NO_MATCH_PAGE:
				Toast.makeText(context, routerErr.getErrMsg(), Toast.LENGTH_SHORT).show();
				//// TODO: 3/31/17  goto 404 page ?
				break;
			case RouterErr.ERR_MULTI_MATCH:
				Toast.makeText(context, routerErr.getErrMsg(), Toast.LENGTH_SHORT).show();
				break;
		}
	}


	/**
	 *
	 * @param module
	 * @param handler
	 * @param param
	 * @return
	 */
	private String createUri(String module, String handler, Map<String, Object> param) {
		StringBuilder sb = new StringBuilder("eco");
		sb.append("://")
				.append(module)
				.append("/")
				.append(handler)
				.append("?");

		if (null != param) {
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				sb.append(entry.getKey())
						.append("=")
						.append(entry.getValue())
						.append("&");
			}
		}

		return sb.toString();
	}
}
