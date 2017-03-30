package com.example.binghu.router.router;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
	 * 动态匹配模块名和类名
	 * @param origin
	 * @return
	 */
	private Uri getUri(Uri origin) {
		if (serverRouterMap != null) {
			//get module
			String module = serverRouterMap.get(origin.getHost()) == null ? origin.getHost():
					serverRouterMap.get(origin.getHost());
			//get class
			String clazz = serverRouterMap.get(origin.getPath()) == null ? origin.getPath():
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
	 * @param context
	 * @param url schema://host/path?params#fragment
	 */
	@Override
	public void open(Context context, String url) {
		open(context, url, null);
	}


	/**
	 * open url with resultCallback
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

		intercept(getModuleFullName(url, context), context.getApplicationContext());

//		Uri uri = Uri.parse(url);
//		Uri handledUri = getUri(uri);
//
//		// ----------JUMP---------------------------------
//		if (null != resultCallback) {
//			startRouterActivity(context, handledUri);
//		} else {
//			Intent intent = new Intent(Intent.ACTION_MAIN);
//			intent.setData(handledUri);
//			context.startActivity(intent);
//		}
	}


	/**
	 * check url validity
	 * @param url
	 * @return
	 */
	private boolean checkUrl(String url, Context context) {
		if (TextUtils.isEmpty(url)) {
			Log.e(TAG, " url must not empty !");
			return false;
		}

		if(!checkMatchModule(url, context)) {
			//// TODO: 3/13/17 goto 404 page 
			Log.e(TAG, " url is invalid or the module you called is not ready !");
			return false;
		}
		return true;
	}


	/**
	 * check whether has matched activity for the url
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
	 *
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

		//// TODO: 3/30/17  add for test 
		context.startActivity(intent);
		
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> rls = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA);

		if (null == rls || rls.size() == 0) {
			//// TODO: 3/30/17 error handler
			handleError();
		} else if (rls.size() > 1){
			handleError();
		} else {
			ResolveInfo ri = rls.get(0);
			String fullName = ri.activityInfo.name;
			return fullName;
		}

		return "";
	 }


	 private void intercept(String name, Context context) {
		 try {
			 Class clazz = Class.forName(name, false, getClass().getClassLoader());
			 IModule module = (IModule) clazz.newInstance();
			 boolean result = module.intercept(context);
			 Log.e("RESULT", " result ==> " + result);

//			 Method method = clazz.getDeclaredMethod("intercept", Context.class);
//			 Object o = method.invoke(clazz.newInstance(), context);
//			 Log.e("RESULT", " result ==> o " + o);
		 } catch (ClassNotFoundException e) {
			 e.printStackTrace();
		 } catch (InstantiationException e) {
			 e.printStackTrace();
		 } catch (IllegalAccessException e) {
			 e.printStackTrace();
		 }
//		 } catch (NoSuchMethodException e) {
//			 e.printStackTrace();
//		 } catch (InvocationTargetException e) {
//			 e.printStackTrace();
//		 }
	 }

	 private void handleError() {

	 }
}
