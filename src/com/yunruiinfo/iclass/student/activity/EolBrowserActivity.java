package com.yunruiinfo.iclass.student.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.bean.Base;
import com.yunruiinfo.iclass.student.bean.URLs;
import com.yunruiinfo.iclass.student.bean.User;
import com.yunruiinfo.iclass.student.download.DownloadManager;
import com.yunruiinfo.iclass.student.download.DownloadService;
import com.yunruiinfo.iclass.student.util.CyptoUtils;
import com.yunruiinfo.iclass.student.util.FileUtils;
import com.yunruiinfo.iclass.student.util.UIHelper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

public class EolBrowserActivity extends BaseActivity {
	@ViewInject(R.id.webview)
	private WebView mWebView;
	private String mUrl;
	private User mUser;
	private DownloadManager downloadManager;
	private ProgressBar mProgress;
	private TextView mProgressText;
	private AlertDialog downloadDialog;
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
		
		setTitle("正在跳转...");

		mUrl = getIntent().getStringExtra("url");
		
		downloadManager = DownloadService.getDownloadManager(this);
		initView();
		initData();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		mWebView.getSettings().setJavaScriptEnabled(true);
		//开启Dom存储Api，解决Uncaught TypeError: Cannot call method 'getItem' of null at XXXX/build.js:6等错误
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setDefaultTextEncodingName(Base.UTF_8);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		if(Build.VERSION.SDK_INT >= 11){//用于判断是否为Android 3.0系统, 然后隐藏缩放控件
			mWebView.getSettings().setDisplayZoomControls(false);
		}else{
			setZoomControlGone(mWebView);  // Android 3.0(11) 以下使用以下方法
		}
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
		mWebView.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, 
					String mimetype, long contentLength) {
				Uri uri = Uri.parse(URLs.getLoginUrl(url, mUser));  
	            Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
	            startActivity(intent);  
	            
	            String fileName = FileUtils.getUrlFileName(url);
				String target = Environment.getExternalStorageDirectory().getAbsolutePath();
				target += "/iStudent/.file/" + fileName;
				final String filePath = target;
				boolean autoResume = true;
				boolean autoRename = false;
				
				try {
					downloadManager.addNewDownload(url, fileName, target, autoResume, autoRename,
						new RequestCallBack<File>() {
							@Override
							public void onSuccess(ResponseInfo<File> arg0) {
								downloadDialog.dismiss();
								UIHelper.openFile(EolBrowserActivity.this, arg0.result);
							}
							@Override
							public void onFailure(HttpException arg0, String arg1) {
								if (new File(filePath).exists()) {
									downloadDialog.dismiss();
									UIHelper.openFile(EolBrowserActivity.this, new File(filePath));
								} else {
									UIHelper.ToastMessage(EolBrowserActivity.this, "文件下载出错，请重新下载");
								}
							}
							public void onLoading(long total, long current, boolean isUploading) {
								//显示文件大小格式：2个小数点显示
						    	DecimalFormat df = new DecimalFormat("0.00");
						    	//进度条下面显示的总文件大小
						    	String fileSize = df.format((float) total / 1024 / 1024) + "MB";
						    	//进度条下面显示的当前下载文件大小
						    	String tmpFileSize = df.format((float) current / 1024 / 1024) + "MB";
					    		//当前进度值
					    	    int progress = (int)(((float)current / total) * 100);
					    	    mProgress.setProgress(progress);
								mProgressText.setText(tmpFileSize + "/" + fileSize);
							};
							public void onStart() {
								showDownloadDialog();
							};
						});
				} catch (DbException e) {
					e.printStackTrace();
				}
	            
			}
		});
	}
	
	private void initData() {
		mWebView.loadUrl(URLs.getLoginUrl(mUrl, mUser));
	}

	//Android 3.0(11) 以下使用以下方法:
	//利用java的反射机制
	public void setZoomControlGone(View view) {
		Class<WebView> classType;
		Field field;
		try {
			classType = WebView.class;
			field = classType.getDeclaredField("mZoomButtonsController");
			field.setAccessible(true);
			ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(view);
			mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
			field.set(view, mZoomButtonsController);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 显示下载对话框
	 */
	private void showDownloadDialog(){
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("正在下载新版本");
		
		final LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.update_progress);
		mProgressText = (TextView) v.findViewById(R.id.update_progress_text);
		
		builder.setView(v);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					downloadManager.stopAllDownload();
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dialog.dismiss();
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				try {
					downloadManager.stopAllDownload();
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dialog.dismiss();
			}
		});
		downloadDialog = builder.create();
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();
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
