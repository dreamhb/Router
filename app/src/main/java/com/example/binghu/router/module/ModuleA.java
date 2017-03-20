package com.example.binghu.router.module;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.binghu.router.R;
import com.example.binghu.router.router.AbsModule;

public class ModuleA extends AbsModule implements View.OnClickListener {

	private EditText etResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		etResult = (EditText) findViewById(R.id.et_result);
	}

	@Override
	public void onClick(View v) {
		Intent intent = getIntent();
		Bundle data = new Bundle();
		data.putString("tips", etResult.getEditableText().toString());
		setResult(RESULT_OK, intent.putExtra("result", data));
		finish();
	}
	
	@Override
	public boolean interceptor() {
		return true;
	}
}
