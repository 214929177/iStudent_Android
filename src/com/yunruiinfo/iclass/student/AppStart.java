package com.yunruiinfo.iclass.student;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.networkbench.agent.impl.NBSAppAgent;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.activity.MainActivity;
import com.yunruiinfo.iclass.student.bean.URLs;

/**
 * 应用程序启动类：显示欢迎界面并跳转到主界面
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
@SuppressLint("HandlerLeak")
public class AppStart extends Activity {
    
	private static final String VERSION_KEY = "version";
	@ViewInject(R.id.flip)			private ImageView mBtnEnter;
	@ViewInject(R.id.start_image)	private ImageView mStartImage;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.app_start, null);
		setContentView(view);
		NBSAppAgent.setLicenseKey("d8bd3a33837242539d426e102f443369").withLocationServiceEnabled(true).start(this);
		ViewUtils.inject(this);
		
		//加载欢迎图片
		BitmapUtils bmpUtils = new BitmapUtils(this);
		bmpUtils.display(mStartImage, URLs.WELCOME);

		//渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
		aa.setDuration(1000);          //2000
		view.startAnimation(aa);
		//处理初始化业务
		initThread = new Thread(doInit);
		initThread.start();
    }

	private Thread initThread;
    private Runnable doInit = new Runnable() {
		@Override
		public void run() {
			try {
				init();
				Thread.sleep(2000);
				mHandler.obtainMessage(1).sendToTarget();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1) {
				redirectTo(MainActivity.class);
			}
		}
	};
    /**
     * 跳转到...
     */
    private void redirectTo(Class<?> activity){        
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }
    private void init() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
			int currentVersion = info.versionCode;
	    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    	int lastVersion = prefs.getInt(VERSION_KEY, 0);
	    	//AppContext appContext = (AppContext) getApplication();
	    	if (currentVersion > lastVersion) {
	    	     //如果当前版本大于上次版本，该版本属于第一次启动
	    		 //do something
	    	     //将当前版本写入preference中，则下次启动的时候，据此判断，不再为首次启动
	    	     prefs.edit().putInt(VERSION_KEY, currentVersion).commit();
	    	} else {
	    		//do something    	
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	@OnClick(R.id.flip)
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.flip:
			redirectTo(MainActivity.class);
			break;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (initThread != null) {
			initThread.interrupt();
			initThread = null;
		}
	}
}