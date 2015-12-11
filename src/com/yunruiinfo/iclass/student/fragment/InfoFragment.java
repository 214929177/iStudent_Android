package com.yunruiinfo.iclass.student.fragment;

import java.util.ArrayList;
import java.util.List;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.adapter.GridViewIconAdapter;
import com.yunruiinfo.iclass.student.bean.Icon;
import com.yunruiinfo.iclass.student.util.UIHelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class InfoFragment extends BaseFragment {
	@ViewInject(R.id.icons)	private GridView mIconsView;
	private GridViewIconAdapter mIconsAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_info, container, false);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);		
		
		getChildFragmentManager().beginTransaction()
		.replace(R.id.info_banner, new BannerFragment())
		.commit();
		
		List<Icon> mIcons = new ArrayList<Icon>();
        mIcons.add(new Icon("通知公告", R.drawable.ic_icon_news_notice, NoticeListFragment.class, null));
        mIcons.add(new Icon("农大校历", R.drawable.ic_icon_course_table, null, null));
		mIconsAdapter = new GridViewIconAdapter(getActivity(), mIcons);
		mIconsView.setAdapter(mIconsAdapter);
		mIconsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Icon icon = (Icon) mIconsAdapter.getItem(position);
				if ("通知公告".equals(icon.name)) {
					switchFragment(new NoticeListFragment());
				} else if ("农大校历".equals(icon.name)) {
					UIHelper.showUrl(getActivity(), "file:///android_asset/xiaoli.htm");
				} else {
					UIHelper.ToastMessage(getActivity(), "功能升级中...");
				}
			}
		});
	}
	
	@Override
	public void onStart() {
		super.onStart();
		getActivity().setTitle("信息中心");
	}
}
