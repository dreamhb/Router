package com.example.binghu.router;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.binghu.router.router.ResultCallback;
import com.example.binghu.router.router.Router;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entry);
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_module_a:
//				Router.INSTANCE.open(this, "eco://moduleA/classA?one=1&two=2&three=3", new ResultCallback() {
//					@Override
//					public void onResult(Bundle result) {
//						Toast.makeText(EntryActivity.this, result.getString("tips"), Toast.LENGTH_SHORT).show();
//					}
//				});
				Router.INSTANCE.open();
				break;
			case R.id.btn_module_b:
				Router.INSTANCE.open(this, "eco://moduleB/classB?girl=love&love=forever");
				break;
			case R.id.btn_module_url:
				Router.INSTANCE.open(this, "eco://moduleUrl/classUrl?url=http://10.88.98.44:8080/router/");
				break;
		}
	}
}
