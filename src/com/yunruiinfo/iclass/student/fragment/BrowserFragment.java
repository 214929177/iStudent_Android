package com.yunruiinfo.iclass.student.fragment;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yunruiinfo.iclass.student.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class BrowserFragment extends BaseFragment {
	@ViewInject(R.id.webview)
	WebView mWebView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_browser, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setInitialScale(1);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setSupportZoom(false);
		mWebView.getSettings().setBuiltInZoomControls(false);
		String url = getArguments().getString("url");
		mWebView.loadUrl(url);
	}
}
