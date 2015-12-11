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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class SchoolFragment extends BaseFragment {
	@ViewInject(R.id.icons)
	private GridView mIconsView;
	private GridViewIconAdapter mIconsAdapter;	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		List<Icon> mIcons = new ArrayList<Icon>();
        mIcons.add(new Icon("查空教室", R.drawable.ic_home_viewroom,ClassRoomFragment.class, null));
        mIcons.add(new Icon("校园资讯", R.drawable.ic_home_news, NewsListFragment.class, null));
		mIconsView = (GridView) findViewById(R.id.icons);
		mIconsAdapter = new GridViewIconAdapter(getActivity(), mIcons);
		mIconsView.setAdapter(mIconsAdapter);
		mIconsView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//UIHelper.showActivity(getActivity(), CourseActivity.class);
				Icon icon = (Icon) mIconsAdapter.getItem(position);	
				if (icon.fragment==null){	
					UIHelper.ToastMessage(getActivity(), "功能暂未启用");									
				}								
				else{							
					Bundle bundle = new Bundle();
					bundle.putSerializable("icon", icon);					
					MainActivity fca = (MainActivity) getActivity();
					Fragment fragment = Fragment.instantiate(getActivity(),icon.fragment.getName(),bundle);
					fca.switchContent(fragment);
				}
			}
		});
	}
	@Override
	public void onStart() {
		super.onStart();		
		getActivity().setTitle("掌上农大");
	}
}
