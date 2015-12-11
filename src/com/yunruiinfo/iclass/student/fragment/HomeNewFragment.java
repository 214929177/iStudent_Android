package com.yunruiinfo.iclass.student.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.activity.CoursesActivity;
import com.yunruiinfo.iclass.student.activity.MainActivity;
import com.yunruiinfo.iclass.student.activity.TestOnlineActivity;
import com.yunruiinfo.iclass.student.adapter.GridViewIconAdapter;
import com.yunruiinfo.iclass.student.bean.Base;
import com.yunruiinfo.iclass.student.bean.Icon;
import com.yunruiinfo.iclass.student.bean.URLs;
import com.yunruiinfo.iclass.student.util.UIHelper;
import com.yunruiinfo.iclass.student.view.ExpandGridView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;

@SuppressLint("SetJavaScriptEnabled")
public class HomeNewFragment extends BaseFragment {
	@ViewInject(R.id.webview)	private WebView mWebView;
	@ViewInject(R.id.icons)		private ExpandGridView mIconsView;
	private GridViewIconAdapter mIconsAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_home_new, container, false);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		//include banner
		getChildFragmentManager().beginTransaction()
		.replace(R.id.home_new_banner, new BannerFragment())
		.commit();
		//init icons
		List<Icon> mIcons = new ArrayList<Icon>();
		mIcons.add(new Icon("我的课表", R.drawable.ic_icon_course_table, CourseListFragment.class, null));
		mIcons.add(new Icon("在线测试", R.drawable.ic_icon_test_online, HomeFragment.class, null));
		mIcons.add(new Icon("课程学习", R.drawable.ic_icon_course_notice, CourseListFragment.class, null));
		mIcons.add(new Icon("课程通知", R.drawable.ic_icon_info_courier, CourseNoticesFragment.class, null));
		mIcons.add(new Icon("问道老师", R.drawable.ic_icon_ask_teacher, CourseListFragment.class, null));
		mIcons.add(new Icon("课程讨论", R.drawable.ic_icon_course_discuss, CourseNoticesFragment.class, null));
		mIcons.add(new Icon("常用工具", R.drawable.ic_icon_used_tools, UsedToolsFragment.class, null));
		mIcons.add(new Icon("校园新闻", R.drawable.ic_icon_news_notice, HomeFragment.class, null));
		//mIcons.add(new Icon("学习推荐", R.drawable.ic_icon_tools, CourseNoticesFragment.class, null));
		mIconsAdapter = new GridViewIconAdapter(getActivity(), mIcons);
		mIconsView.setAdapter(mIconsAdapter);
		mIconsView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id){
				Icon icon = (Icon) mIconsAdapter.getItem(position);
				
				if (icon.fragment == null) {
					UIHelper.ToastMessage(getActivity(), "功能升级中...");
				} else if ("在线测试".equals(icon.name)) {
					UIHelper.showActivity(getActivity(), TestOnlineActivity.class);
				} else if ("课程讨论".equals(icon.name)) {
					UIHelper.showEolUrl(getActivity(), URLs.EOL_COURSE_DISCUSS);
				} else if ("我的课表".equals(icon.name)) {
					UIHelper.showActivity(getActivity(), CoursesActivity.class);
					//switchFragment(new CourseListFragment());
				} else if ("问道老师".equals(icon.name)) {
					UIHelper.ToastMessage(getActivity(), "此功能正在升级中...");
					//UIHelper.showActivity(getActivity(), AskTeacherActivity.class);
				} else if (icon.fragment == CourseNoticesFragment.class) {
					switchFragment(new CourseNoticesFragment());
				} else if (icon.fragment == CourseListFragment.class) {
					switchFragment(new CourseListFragment());
				} else {
					Bundle bundle = new Bundle();
					bundle.putSerializable("icon", icon);
					MainActivity fca = (MainActivity) getActivity();
					Fragment fragment = Fragment.instantiate(getActivity(), icon.fragment.getName(), bundle);
					fca.switchContent(fragment);
				}
			}
		});
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setDefaultTextEncodingName(Base.UTF_8);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setSupportZoom(false);
		//mWebView.loadUrl("file:///android_asset/recommend/index.html");
		mWebView.loadUrl(URLs.RECOMMEND);
	}

	@Override
	public void onStart(){
		super.onStart();
		getActivity().setTitle(getString(R.string.app_name));
	}
}
