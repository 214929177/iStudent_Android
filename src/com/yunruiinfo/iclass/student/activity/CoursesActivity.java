package com.yunruiinfo.iclass.student.activity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.bean.Base;
import com.yunruiinfo.iclass.student.bean.URLs;
import com.yunruiinfo.iclass.student.bean.User;
import com.yunruiinfo.iclass.student.util.CyptoUtils;
import com.yunruiinfo.iclass.student.util.UIHelper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CoursesActivity extends BaseActivity {
	@ViewInject(R.id.webview)
	private WebView mWebView;
	private User mUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_browser);
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		
		if (appContext.getUserId() == 0) {
			UIHelper.ToastMessage(this, "请先登录...");
			UIHelper.showActivity(this, LoginActivity.class);
			finish();
			return;
		}
		mUser = appContext.getUserInfo();
		
		initView();
		initData();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		mWebView.getSettings().setJavaScriptEnabled(true);
		//开启Dom存储Api，解决Uncaught TypeError: Cannot call method 'getItem' of null at XXXX/build.js:6等错误
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setDefaultTextEncodingName(Base.UTF_8);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				setTitle(title);
				super.onReceivedTitle(view, title);
			}
		});
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}
	
	private void initData() {
		String user = mUser.getUsername();
		String pwd = CyptoUtils.decode("APPICLASS", mUser.getPassword());
		mWebView.loadUrl(URLs.TIMETABLE + "?username=" + user + "&password=" + pwd);
	}
	
	@Override
	public void onBackPressed() {
		if(mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			super.onBackPressed();
		}
	}
	
	@OnClick(R.id.left_button)
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.left_button:
			finish();
			break;
		}
	}
}
