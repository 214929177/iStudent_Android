package com.yunruiinfo.iclass.student.fragment;

import java.util.ArrayList;
import java.util.List;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.activity.MainActivity;
import com.yunruiinfo.iclass.student.adapter.GridViewIconAdapter;
import com.yunruiinfo.iclass.student.bean.Icon;
import com.yunruiinfo.iclass.student.util.UIHelper;
import com.yunruiinfo.iclass.student.view.ExpandGridView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class CourseFragment extends BaseFragment {
	@ViewInject(R.id.icons)
	private ExpandGridView mIconsView;
	private GridViewIconAdapter mIconsAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_course, container, false);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		getChildFragmentManager().beginTransaction()
		.replace(R.id.course_banner, new BannerFragment())
		.commit();
		
		List<Icon> mIcons = new ArrayList<Icon>();
		mIcons.add(new Icon("在线课程", R.drawable.ic_icon_course_table, CourseListFragment.class, null));
		mIcons.add(new Icon("课程通知", R.drawable.ic_icon_course_notice, CourseNoticesFragment.class, null));
		mIconsAdapter = new GridViewIconAdapter(getActivity(), mIcons);
		mIconsView.setAdapter(mIconsAdapter);
		mIconsView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id){
				Icon icon = (Icon) mIconsAdapter.getItem(position);
				
				if (icon.fragment == null) {
					UIHelper.ToastMessage(getActivity(), "功能升级中...");
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
	}
	
	@Override
	public void onStart() {
		super.onStart();
		getActivity().setTitle("课程学习");
	}
}
