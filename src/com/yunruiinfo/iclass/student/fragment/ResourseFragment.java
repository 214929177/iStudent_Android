package com.yunruiinfo.iclass.student.fragment;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonParseException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.AppException;
import com.yunruiinfo.iclass.student.activity.LoginActivity;
import com.yunruiinfo.iclass.student.adapter.ListViewResourseAdapter;
import com.yunruiinfo.iclass.student.bean.Resourse;
import com.yunruiinfo.iclass.student.bean.Result;
import com.yunruiinfo.iclass.student.bean.URLs;
import com.yunruiinfo.iclass.student.bean.User;
import com.yunruiinfo.iclass.student.download.DownloadManager;
import com.yunruiinfo.iclass.student.download.DownloadService;
import com.yunruiinfo.iclass.student.util.JsonUtils;
import com.yunruiinfo.iclass.student.util.UIHelper;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ResourseFragment extends BaseFragment{
	@ViewInject(R.id.empty_text)	private TextView mEmptyView;
	@ViewInject(R.id.course_list)	private ListView mResoursesView;
	private List<Resourse> mResoursesData = new ArrayList<Resourse>();
	private ListViewResourseAdapter mResourseAdapter;
	private User mUser;
	private int mCourseId;
	private DownloadManager downloadManager;
	private ProgressBar mProgress;
	private TextView mProgressText;
	private AlertDialog downloadDialog;
	private int mPageNo = 1;		// 当前页码
	private int mPageSize = 10;	// 每页显示数量
	private LinearLayout mFooterLayout;
	private TextView mFooterMessage;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list_resourse, container, false);
		ViewUtils.inject(this, view);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().setTitle("教学材料");
		
		if (appContext.getUserId() == 0) {
			UIHelper.ToastMessage(getActivity(), "请先登录...");
			UIHelper.showActivity(getActivity(), LoginActivity.class);
			getFragmentManager().popBackStack();
			return;
		}
		mUser = appContext.getUserInfo();
		
		try {
			mCourseId = getArguments().getInt("id");
			if (mCourseId == 0) 
				throw new IllegalArgumentException("课程ID不能为0！");
		} catch (Exception e) {
			UIHelper.ToastMessage(getActivity(), "非法访问");
			getFragmentManager().popBackStack();
		}
		
		downloadManager = DownloadService.getDownloadManager(getActivity());
		
		
		initView();
		loadResourseData(mPageNo, mPageSize);
	}
	
	/**
	 * 获取教学材料
	 */
	private void loadResourseData(int page, int size) {
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("type", "resourses");
		params.addQueryStringParameter("id", Integer.toString(mCourseId));
		params.addQueryStringParameter("page", Integer.toString(page));
		params.addQueryStringParameter("size", Integer.toString(size));
		http.send(HttpMethod.GET, URLs.API_URL, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				try {
					Result result = JsonUtils.fromJson(responseInfo.result, Result.class);
					if (!result.OK()) {
						throw AppException.custom(result.Message());
					} else if (result.resourses.size() >= 0) {
						mResoursesData.addAll(result.resourses);
						mResourseAdapter.setResourses(mResoursesData);
						mResourseAdapter.notifyDataSetChanged();
						if (mResoursesData.size() == 0) {
							mEmptyView.setText("暂无教学材料");
							return;
						}
						if (result.resourses.size() < mPageSize) {
							mResourseAdapter.loaded();	// 加载完毕
							mFooterMessage.setText("加载完毕");
						}
					}
				} catch (JsonParseException e) {
					AppException.json(e).makeToast(getActivity());
				} catch (AppException e) {
					e.makeToast(getActivity());
				}
			}
			@Override
			public void onStart() {
				mEmptyView.setText("正在获取教学材料");
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				mEmptyView.setText("暂无教学材料");
			}
		});
	}
	
	/**
	 * 初始化视图
	 */
	private void initView() {
		mResourseAdapter = new ListViewResourseAdapter(getActivity());
		mResoursesView.setEmptyView(mEmptyView);
		mFooterLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.listview_footer, null);
		mFooterMessage = (TextView) mFooterLayout.findViewById(R.id.message);
		mResoursesView.addFooterView(mFooterLayout);
		mResoursesView.setAdapter(mResourseAdapter);
		mResoursesView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 数据为空--不用继续下面代码了
				if (mResoursesData.isEmpty() || mResourseAdapter.isLoaded())
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(mFooterLayout) == view.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}
				if (scrollEnd) {
					loadResourseData(++mPageNo, mPageSize);
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) { }
		});
		mResoursesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//点击底部栏无效
        		if(view == mFooterLayout) return;
        		
				Resourse res = (Resourse) mResourseAdapter.getItem(position);
				String url = URLs.getLoginUrl(res.getUrl(), mUser);
	            String fileName = res.getName();
				String target = Environment.getExternalStorageDirectory().getAbsolutePath();
				target += "/iStudent/.file/" + mCourseId + "/" + fileName;
				final String filePath = target;
				boolean autoResume = true;
				boolean autoRename = false;
				File file = new File(filePath);
				if (file.exists() && file.length() == res.getSize()) {
					UIHelper.openFile(getActivity(), new File(filePath));
					return;
				}
				try {
					downloadManager.addNewDownload(url, fileName, target, autoResume, autoRename,
						new RequestCallBack<File>() {
							@Override
							public void onSuccess(ResponseInfo<File> arg0) {
								downloadDialog.dismiss();
								UIHelper.openFile(getActivity(), arg0.result);
							}
							@Override
							public void onFailure(HttpException arg0, String arg1) {
								downloadDialog.dismiss();
								if (new File(filePath).exists()) {
									UIHelper.openFile(getActivity(), new File(filePath));
								} else {
									UIHelper.ToastMessage(getActivity(), "文件下载出错，请重新下载");
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
	
	/**
	 * 显示下载对话框
	 */
	private void showDownloadDialog(){
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle("正在下载文件");
		
		final LayoutInflater inflater = LayoutInflater.from(getActivity());
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
					e.printStackTrace();
				}
				dialog.dismiss();
			}
		});
		downloadDialog = builder.create();
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();
	}
}
