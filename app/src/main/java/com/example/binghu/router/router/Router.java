package com.example.binghu.router.router;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.binghu.router.router.error.RouterErr;

import java.util.List;
import java.util.Map;


/**
 * Created by binghu on 3/6/17.
 */

public enum Router implements IRouter {

	INSTANCE;

	private static final String TAG = Router.class.getSimpleName();

	private Map<String, String> serverRouterMap;

	private ResultCallback resultCallback;
	static final int REQUEST_CODE_GET_RESULT = 0x3000;

	/**
	 * 实例化后的module
	 */
	private IModule iModule;

	/**
	 * 动态匹配模块名和类名
	 *
	 * @param origin
	 * @return
	 */
	private Uri getUri(Uri origin) {
		if (serverRouterMap != null) {
			//get module
			String module = serverRouterMap.get(origin.getHost()) == null ? origin.getHost() :
					serverRouterMap.get(origin.getHost());
			//get class
			String clazz = serverRouterMap.get(origin.getPath()) == null ? origin.getPath() :
					serverRouterMap.get(origin.getPath());

			return new Uri.Builder()
					.scheme(origin.getScheme())
					.authority(module)
					.path(clazz)
					.query(origin.getQuery())
					.fragment(origin.getFragment())
					.build();
		} else {
			initRouterConfig();
			return getUri(origin);
		}
	}

	/**
	 * 初始化动态的router配置表,
	 * 如果没有就要从服务器获取
	 */
	private void initRouterConfig() {
		//get config from server
		if (null == serverRouterMap) {
			getServerRouterMap();
		}
	}


	/**
	 * dynamic router here
	 */
	private void getServerRouterMap() {
		serverRouterMap = new android.support.v4.util.ArrayMap<>();
		//dynamic router config
		//serverRouterMap.put("/moduleA", "/moduleB");
		//serverRouterMap.put("/moduleB", "/moduleA");
	}

	/**
	 * open url
	 *
	 * @param context
	 * @param url     schema://host/path?params#fragment
	 */
	@Override
	public void open(Context context, String url) {
		open(context, url, null);
	}


	/**
	 * open url with resultCallback
	 *
	 * @param context
	 * @param url
	 * @param resultCallback callback for get result
	 */
	@Override
	public void open(Context context, String url, ResultCallback resultCallback) {

		if (!checkUrl(url, context)) {
			return;
		}

		this.resultCallback = resultCallback;

		boolean result = intercept(getModuleFullName(url, context), context.getApplicationContext());

		if (result) {
			Uri uri = Uri.parse(url);
			Uri handledUri = getUri(uri);

			if (null != resultCallback) {
				startRouterActivity(context, handledUri);
			} else {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setData(handledUri);
				context.startActivity(intent);
			}
		}
	}


	/**
	 * check url validity
	 *
	 * @param url
	 * @return
	 */
	private boolean checkUrl(String url, Context context) {
		if (TextUtils.isEmpty(url)) {
			Log.e(TAG, " url must not empty !");
			handleError(context, new RouterErr(RouterErr.ERR_URL_INVALID, "URL INVALID"));
			return false;
		}

		if (!checkMatchModule(url, context)) {
			//// TODO: 3/13/17 goto 404 page
			Log.e(TAG, " url is invalid or the module you called is not ready !");
			handleError(context, new RouterErr(RouterErr.ERR_NO_MATCH_PAGE, "NO PAGE MATCH"));
			return false;
		}
		return true;
	}


	/**
	 * check whether has matched activity for the url
	 *
	 * @param url
	 * @return
	 */
	private boolean checkMatchModule(String url, Context context) {
		Uri uri = Uri.parse(url);
		Intent intent = new Intent();
		intent.setData(uri);

		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
		if (resolveInfoList == null || resolveInfoList.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * start activity to get result
	 *
	 * @param context
	 */
	private void startRouterActivity(Context context, Uri uri) {
		Intent intent = new Intent(context, RouterActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setData(uri);
		context.startActivity(intent);
	}


	/**
	 * handle result from activity
	 *
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_GET_RESULT) {
			if (resultCallback != null && data != null) {
				resultCallback.onResult(data.getBundleExtra("result"));
			}
		}
	}

	/**
	 * @param url
	 * @param context
	 * @return
	 */
	private String getModuleFullName(String url, Context context) {
		Uri origin = Uri.parse(url);
		Uri moduleUri = new Uri.Builder()
				.authority(origin.getAuthority())
				.scheme("m")
				.path("mPath")
				.build();

		Intent intent = new Intent();
		intent.setData(moduleUri);

		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> rls = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA);

		if (null == rls || rls.size() == 0) {
			handleError(context, new RouterErr(RouterErr.ERR_NO_MATCH_PAGE, "NO MATCH"));
		} else if (rls.size() > 1) {
			handleError(context, new RouterErr(RouterErr.ERR_MULTI_MATCH, "MULTI MATCH"));
		} else {
			ResolveInfo ri = rls.get(0);
			return ri.activityInfo.name;
		}

		return "";
	}


	/**
	 * @param name
	 * @param context
	 */
	private boolean intercept(String name, Context context) {
		try {
			Class clazz = Class.forName(name, false, getClass().getClassLoader());
			iModule = (IModule) clazz.newInstance();
			int errCode = iModule.intercept(context);
			if (errCode == RouterErr.OK) {
				return true;
			} else {
				handleError(context, new RouterErr(errCode, ""));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * error handle
	 * base error handle in {@link AbstractModule}
	 * if you need handle module specified error
	 * please override {@link AbstractModule#handleError(Context, RouterErr)}
	 * @param routerErr
	 */
	private void handleError(Context context, RouterErr routerErr) {
		if (iModule != null) {
			iModule.handleError(context, routerErr);
		} else {
			handleBaseError(context, routerErr);
		}
	}


	/**
	 * 基本错误可以放在这里，如果需要模块处理的话，就需要实例化模块
	 * 看如何取舍吧
	 * @param context
	 * @param routerErr
	 */
	private void handleBaseError(Context context, RouterErr routerErr) {
		Toast.makeText(context, routerErr.getErrMsg(), Toast.LENGTH_SHORT).show();
	}
}
