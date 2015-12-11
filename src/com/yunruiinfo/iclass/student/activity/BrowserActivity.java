package com.yunruiinfo.iclass.student.activity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.bean.Base;
import com.yunruiinfo.iclass.student.util.FileUtils;
import com.yunruiinfo.iclass.student.util.StringUtils;
import com.yunruiinfo.iclass.student.util.UIHelper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowserActivity extends BaseActivity {
	@ViewInject(R.id.webview)
	private WebView mWebView;
	private String mUrl;
	private String mData;
	private String mTitle;
	private String mBaseUrl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_browser);
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		
		mTitle = getIntent().getStringExtra("title");
		if (!StringUtils.isEmpty(mTitle)) {
			setTitle(mTitle);
		} else {
			setTitle("正在跳转...");
		}
		mUrl = getIntent().getStringExtra("url");
		mData = getIntent().getStringExtra("data");
		if (mData == null) {
			mData = FileUtils.read(this, getIntent().getStringExtra("file"));
		}
		mBaseUrl = getIntent().getStringExtra("baseUrl");
		if (StringUtils.isEmpty(mUrl) && mData == null) {
			UIHelper.ToastMessage(this, "没有内容可以显示");
			return;
		}
		
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
		if (mUrl != null) {
			mWebView.loadUrl(mUrl);
		} else if (mData != null) {
			mWebView.loadDataWithBaseURL(mBaseUrl, mData, "text/html", Base.UTF_8, null);
		}
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
