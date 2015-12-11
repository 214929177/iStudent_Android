package com.yunruiinfo.iclass.student.activity;

import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.AppContext;
import com.yunruiinfo.iclass.student.AppManager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

/**
 * 应用程序Activity的基类
 * @author SYZ
 * @version 1.0
 * @created 2013-11-30
 */
public class BaseActivity extends FragmentActivity {
	AppContext appContext;	//全局Context
	TextView mTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		//添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
		//获取全局Context
		appContext = (AppContext) getApplication();
	}
	
	@Override
	protected void onTitleChanged(CharSequence title, int color) {
		super.onTitleChanged(title, color);
		
		if (mTitle == null) {
			mTitle = (TextView) findViewById(R.id.main_title);
		}
		if (mTitle != null) {
			mTitle.setText(title);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		//结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}
}
