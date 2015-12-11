package com.yunruiinfo.iclass.student.fragment;

import com.lidroid.xutils.ViewUtils;
import com.yunruiinfo.iclass.student.AppContext;
import com.yunruiinfo.iclass.student.activity.MainActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

public class BaseFragment extends Fragment {
	protected AppContext appContext;	//全局Context
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		appContext = (AppContext) getActivity().getApplication();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ViewUtils.inject(getActivity());
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (getActivity() instanceof MainActivity) {
			//获取主Activity
			MainActivity fca = (MainActivity) getActivity();
			//根据当前显示Fragment选中相应菜单
			fca.setSelectedMenu(this.getClass());
		}
	}
	
	// the meat of switching the above fragment
	protected void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof MainActivity) {
			//获取主Activity
			MainActivity fca = (MainActivity) getActivity();
			//切换Fragment
			fca.switchContent(fragment);
		}
	}
	
	protected View findViewById(int id) {
		return getActivity().findViewById(id);
	}
}
