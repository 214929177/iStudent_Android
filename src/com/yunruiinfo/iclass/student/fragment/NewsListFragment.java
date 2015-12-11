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

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.adapter.ListViewNewsAdapter;
import com.yunruiinfo.iclass.student.bean.News;
import com.yunruiinfo.iclass.student.util.UIHelper;

public class NewsListFragment  extends BaseFragment {
	@ViewInject(R.id.news_list)		private ListView mNewsView;
	@ViewInject(R.id.empty_text)	private TextView mEmptyView;
	private List<News> mNewsData = Collections.emptyList();;
	private ListViewNewsAdapter mNewsAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_list_news, container, false);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().setTitle("校园资讯");
		mNewsView.setEmptyView(mEmptyView);
		mNewsAdapter = new ListViewNewsAdapter(getActivity(), mNewsData);
		mNewsView.setAdapter(mNewsAdapter);		
		mNewsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UIHelper.ToastMessage(getActivity(), "功能暂未启用");
			}
		});
	}
}
