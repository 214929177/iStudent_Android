package com.yunruiinfo.iclass.student.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonParseException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.AppException;
import com.yunruiinfo.iclass.student.adapter.ListViewNoticeAdapter;
import com.yunruiinfo.iclass.student.bean.Notice;
import com.yunruiinfo.iclass.student.bean.Result;
import com.yunruiinfo.iclass.student.bean.URLs;
import com.yunruiinfo.iclass.student.util.JsonUtils;
import com.yunruiinfo.iclass.student.util.UIHelper;

public class CourseNoticesFragment  extends BaseFragment{
	@ViewInject(R.id.notice_list)	private ListView mNoticesView;
	@ViewInject(R.id.empty_text)	private TextView mEmptyView;
	private List<Notice> mNoticesData;
	private ListViewNoticeAdapter mNoticesAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list_notice, container, false);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().setTitle("课程通知");
		
		mNoticesView.setEmptyView(mEmptyView);	
		mNoticesAdapter = new ListViewNoticeAdapter(getActivity());
		mNoticesView.setAdapter(mNoticesAdapter);
		
		Bundle bundle = getArguments();
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("type", "course_notices");
		params.addQueryStringParameter("userId", Integer.toString(appContext.getUserId()));
		if (bundle != null) {
			params.addQueryStringParameter("course_id", Integer.toString(bundle.getInt("id", 0)));
		}
		http.send(HttpMethod.GET, URLs.API_URL, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				try {
					Result result = JsonUtils.fromJson(responseInfo.result, Result.class);
					if (!result.OK()) {
						throw AppException.custom(result.Message());
					} else if (result.notices.size() > 0) {
						mNoticesData = result.notices;
						mNoticesAdapter.setNotices(mNoticesData);
						mNoticesAdapter.notifyDataSetChanged();
					} else {
						mEmptyView.setText("暂无课程通知");
					}
				} catch (JsonParseException e) {
					AppException.json(e).makeToast(getActivity());
				} catch (AppException e) {
					e.makeToast(getActivity());
				}
			}
			@Override
			public void onStart() {
				mEmptyView.setText("正在获取课程通知");
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				mEmptyView.setText("暂无课程通知");
			}
		});
		mNoticesView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id){
				Notice notice = (Notice) mNoticesAdapter.getItem(position);
				UIHelper.showHtml(getActivity(), notice.getTitle(), notice.getContent(), URLs.BASE_URL);
			}
		});
	}
}
