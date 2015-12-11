package com.yunruiinfo.iclass.student.fragment;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
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
import com.yunruiinfo.iclass.student.adapter.ListViewQauNewsAdapter;
import com.yunruiinfo.iclass.student.bean.QauNews;
import com.yunruiinfo.iclass.student.bean.Result;
import com.yunruiinfo.iclass.student.bean.URLs;
import com.yunruiinfo.iclass.student.util.JsonUtils;
import com.yunruiinfo.iclass.student.util.UIHelper;

public class QauNewsListFragment  extends BaseFragment {
	@ViewInject(R.id.news_list)	private ListView mNewsView;
	@ViewInject(R.id.empty_text)		private TextView mEmptyView;
	private List<QauNews> mNewsData = Collections.emptyList();;
	private ListViewQauNewsAdapter mNewsAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_list_qau_news, container, false);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//get args, 使用该Fragment必须提供type参数，否则报错，判断无意义
		String news_type = getArguments().getString("news_type");
		//init view
		mNewsView.setEmptyView(mEmptyView);
		mNewsAdapter = new ListViewQauNewsAdapter(getActivity());
		mNewsView.setAdapter(mNewsAdapter);
		//load data
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("act", "news_list");
		params.addQueryStringParameter("news_type", news_type);
		http.send(HttpMethod.GET, URLs.YR_API, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				try {
					Result result = JsonUtils.fromJson(responseInfo.result, Result.class);
					if (!result.OK()) {
						throw AppException.custom(result.Message());
					} else if (result.news_list.size() > 0) {
						mNewsData = result.news_list;
						mNewsAdapter.setNews(mNewsData);
						mNewsAdapter.notifyDataSetChanged();
					} else {
						mEmptyView.setText("暂无新闻信息");
					}
				} catch (JsonSyntaxException e) {
					AppException.json(e).makeToast(getActivity());
				} catch (AppException e) {
					e.makeToast(getActivity());
				}
			}
			@Override
			public void onStart() {
				mEmptyView.setText("正在获取新闻信息");
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				mEmptyView.setText("暂无新闻信息");
			}
		});
		mNewsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				QauNews news = (QauNews) mNewsAdapter.getItem(position);
				UIHelper.showUrl(getActivity(), news.getHref());
			}
		});
	}
	
	public static QauNewsListFragment newInstance(String type) {
		QauNewsListFragment fragment = new QauNewsListFragment();
		Bundle args = new Bundle();
		args.putString("news_type", type);
		fragment.setArguments(args);
		return fragment;
	}
}
