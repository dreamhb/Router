package com.example.binghu.router;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.binghu.router.module.ModuleAM;
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
				ModuleAM moduleAM = new ModuleAM();
				moduleAM.createURL(this, "moduleA", "classB", null);
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
