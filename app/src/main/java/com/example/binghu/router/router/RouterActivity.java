package com.example.binghu.router.router;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * this activity only for handle result from target activity
 */
public class RouterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handleIntent(getIntent());
	}


	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		Uri data = intent.getData();
		Intent i = new Intent();
		i.setData(data);
		startActivityForResult(i, Router.REQUEST_CODE_GET_RESULT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Router.REQUEST_CODE_GET_RESULT) {
			Router.INSTANCE.onActivityResult(requestCode, resultCode, data);
		}
		finish();
	}
}
