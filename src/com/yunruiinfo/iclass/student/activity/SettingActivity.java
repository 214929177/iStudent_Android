package com.yunruiinfo.iclass.student.activity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.fragment.SettingNewFragment;

import android.os.Bundle;
import android.view.View;

public class SettingActivity extends BaseActivity{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ViewUtils.inject(this);	
		
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.container, new SettingNewFragment())
		.commit();
	}
	
	@OnClick(R.id.left_button)
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.left_button:
			int count = getSupportFragmentManager().getBackStackEntryCount();
			if (count > 0) {
				getSupportFragmentManager().popBackStack();
			} else {
				finish();
			}
			break;
		}
	}
}
