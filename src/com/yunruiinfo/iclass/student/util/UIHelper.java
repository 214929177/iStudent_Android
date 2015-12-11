package com.yunruiinfo.iclass.student.util;


import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.AppContext;
import com.yunruiinfo.iclass.student.AppManager;
import com.yunruiinfo.iclass.student.activity.BrowserActivity;
import com.yunruiinfo.iclass.student.activity.EolBrowserActivity;
import com.yunruiinfo.iclass.student.activity.MainActivity;
import com.yunruiinfo.iclass.student.bean.URLs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;


/**
 * 应用程序UI工具包：封装UI相关的一些操作
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class UIHelper {

	public final static int LISTVIEW_ACTION_INIT = 0x01;
	public final static int LISTVIEW_ACTION_REFRESH = 0x02;
	public final static int LISTVIEW_ACTION_SCROLL = 0x03;
	public final static int LISTVIEW_ACTION_CHANGE_CATALOG = 0x04;
	
	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;
	
	public final static int LISTVIEW_DATATYPE_NEWS = 0x01;
	public final static int LISTVIEW_DATATYPE_BLOG = 0x02;
	public final static int LISTVIEW_DATATYPE_POST = 0x03;
	public final static int LISTVIEW_DATATYPE_TWEET = 0x04;
	public final static int LISTVIEW_DATATYPE_ACTIVE = 0x05;
	public final static int LISTVIEW_DATATYPE_MESSAGE = 0x06;
	public final static int LISTVIEW_DATATYPE_COMMENT = 0x07;
	
	public final static int REQUEST_CODE_FOR_RESULT = 0x01;
	public final static int REQUEST_CODE_FOR_REPLY = 0x02;
	
	/** 全局web样式 */
	public final static String WEB_STYLE = "<style>* {font-size:16px;line-height:20px;} p {color:#333;} a {color:#3E62A6;} img {max-width:310px;} " +
			"img.alignleft {float:left;max-width:120px;margin:0 10px 5px 0;border:1px solid #ccc;background:#fff;padding:2px;} " +
			"pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;} " +
			"a.tag {font-size:15px;text-decoration:none;background-color:#bbd6f3;border-bottom:2px solid #3E6D8E;border-right:2px solid #7F9FB6;color:#284a7b;margin:2px 2px 2px 0;padding:2px 4px;white-space:nowrap;}</style>";
	
	/**
	 * 打开文件
	 * @param file
	 */
	public static void openFile(Activity activity, File file){
	    //Uri uri = Uri.parse("file://"+file.getAbsolutePath());
	    Intent intent = new Intent();
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    //设置intent的Action属性
	    intent.setAction(Intent.ACTION_VIEW);
	    //获取文件file的MIME类型
	    String type = FileUtils.getMIMEType(file);
	    //设置intent的data和Type属性。
	    intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
	    //跳转
	    activity.startActivity(intent);    
	}
	
	/**
	 * 判断是否已安装packageName应用程序
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAvilible(Context context, String packageName) {
		PackageInfo packageInfo;
		try {
		    packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
		    packageInfo = null;
		    e.printStackTrace();
		}
		return packageInfo != null;
	}	
	/**
	 * 单文件分享
	 * @param activity
	 * @param file
	 */
	public static void shareFile(Activity activity, File file) {
		Intent share = new Intent(Intent.ACTION_SEND);
		share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		share.setType("*/*");
		activity.startActivity(Intent.createChooser(share, "分享"));
	}	
	/**
	 * 打开浏览器
	 * @param context
	 * @param url
	 */
	public static void openBrowser(Context context, String url){
		try {
//			Uri uri = Uri.parse(url);  
//			Intent it = new Intent(Intent.ACTION_VIEW, uri);  			
//			context.startActivity(it);
			
			
			Intent intent = new Intent();
			intent.setData(Uri.parse(url));
			intent.setAction(Intent.ACTION_VIEW);
			context.startActivity(intent); //启动浏览器			
			
		} catch (Exception e) {
			e.printStackTrace();
			ToastMessage(context, "无法浏览此网页", 500);
		} 
	}	
	/**
	 * 弹出Toast消息
	 * @param msg
	 */
	public static void ToastMessage(Context cont,String msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}
	public static void ToastMessage(Context cont,int msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}
	public static void ToastMessage(Context cont,String msg,int time) {
		Toast.makeText(cont, msg, time).show();
	}	
	/**
	 * 点击返回监听事件
	 * @param activity
	 * @return
	 */
	public static View.OnClickListener finish(final Activity activity) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				activity.finish();
			}
		};
	}		
	/**
	 * 发送App异常崩溃报告
	 * @param cont
	 * @param crashReport
	 */
	public static void sendAppCrashReport(final Context cont, final String crashReport) {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_error);
		builder.setMessage(R.string.app_error_message);
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.submit_report, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//发送异常报告
				Intent i = new Intent(Intent.ACTION_SEND);
				//i.setType("text/plain"); //模拟器
				i.setType("message/rfc822") ; //真机
				i.putExtra(Intent.EXTRA_EMAIL, new String[]{"qddagu@126.com"});
				i.putExtra(Intent.EXTRA_SUBJECT,"爱课堂客户端 - 错误报告");
				i.putExtra(Intent.EXTRA_TEXT,crashReport);
				cont.startActivity(Intent.createChooser(i, "发送错误报告"));
				//退出
				AppManager.getAppManager().AppExit(cont, true);
			}
		});
		builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//退出
				AppManager.getAppManager().AppExit(cont, true);
			}
		});
		builder.show();
	}
	//双击退出程序标记
	private static Boolean isExit = false; 
	/**
	 * 双击退出程序
	 * @param context
	 */
	public static void appExit(Context context) {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(context, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
		} else {
			//登录用户退出时通知运营平台
			final int userId = ((AppContext) context.getApplicationContext()).getUserId();
			if (userId > 0) {
				RequestParams params = new RequestParams(){{
					addQueryStringParameter("act", "user_offline");
					addBodyParameter("user_id", Integer.toString(userId));
				}};
				new HttpUtils().send(HttpMethod.GET, URLs.YR_API, params, null);
			}
			//退出
			AppManager.getAppManager().AppExit(context);
		}
	}
	/**
	 * 退出程序
	 * @param cont
	 */
	public static void Exit(final Context cont) {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		//builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.prompt);
		builder.setMessage(R.string.app_exit_message);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				//登录用户退出时通知运营平台
				final int userId = ((AppContext) cont.getApplicationContext()).getUserId();
				if (userId > 0) {
					RequestParams params = new RequestParams(){{
						addQueryStringParameter("act", "user_offline");
						addBodyParameter("user_id", Integer.toString(userId));
					}};
					new HttpUtils().send(HttpMethod.GET, URLs.YR_API, params, null);
				}
				
				//退出
				AppManager.getAppManager().AppExit(cont);
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
	/**
	 * 显示首页
	 * @param activity
	 */
	public static void showHome(Activity activity) {
		Intent intent = new Intent(activity, MainActivity.class);
		activity.startActivity(intent);
	}
	/**
	 * 显示界面activity
	 * @param context
	 * @param activity
	 */
	public static void showActivity(Activity context, Class<? extends Activity> activity) {
		Intent intent = new Intent(context, activity);
		context.startActivity(intent);
	}
	/**
	 * 内部浏览器显示HTML内容
	 * @param activity
	 * @param title
	 * @param data
	 * @param baseUrl
	 */
	public static void showHtml(Activity activity, String title, String data, String baseUrl) {
		String fileName = "temp.html";
		FileUtils.write(activity, fileName, data);
		showHtmlFile(activity, title, fileName, baseUrl);
	}
	/**
	 * 内部浏览器访问本地HTML
	 * @param context
	 * @param title
	 * @param fileName	files文件夹下的文件名
	 * @param baseUrl
	 */
	public static void showHtmlFile(Context context, String title, String fileName, String baseUrl) {
		Intent intent = new Intent(context, BrowserActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("file", fileName);
		intent.putExtra("baseUrl", baseUrl);
		context.startActivity(intent);
	}
	/**
	 * 内部浏览器访问URL
	 * @param context
	 * @param url
	 */
	public static void showUrl(Context context, String url) {
		Intent intent = new Intent(context, BrowserActivity.class);
		intent.putExtra("url", url);
		context.startActivity(intent);
	}
	/**
	 * Eol内部浏览器访问URL
	 * @param context
	 * @param url
	 */
	public static void showEolUrl(Context context, String url) {
		Intent intent = new Intent(context, EolBrowserActivity.class);
		intent.putExtra("url", url);
		context.startActivity(intent);
	}
}
