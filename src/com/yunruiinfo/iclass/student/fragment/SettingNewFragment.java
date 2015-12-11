package com.yunruiinfo.iclass.student.fragment;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.bean.URLs;
import com.yunruiinfo.iclass.student.util.UIHelper;
import com.yunruiinfo.iclass.student.util.UpdateManager;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingNewFragment extends BaseFragment {
	@ViewInject(R.id.version) TextView mVersion;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting_new, container, false);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//显示版本号
		mVersion.setText("当前版本为" + appContext.getVersionName());
	}
	
	@Override
	public void onStart() {
		super.onStart();
		getActivity().setTitle("设置");
	}
	
	@OnClick({
		R.id.feedback_layout,
		R.id.update_layout,
		R.id.about_layout
	})
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.feedback_layout:
			UIHelper.showUrl(getActivity(), URLs.FEED_BACK);
			break;
		case R.id.update_layout:
			UpdateManager.getUpdateManager().checkAppUpdate(getActivity(), true);
			break;
		case R.id.about_layout:
			getFragmentManager()
			.beginTransaction()
			.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
			.replace(R.id.container, new AboutFragment())
			.addToBackStack(null)
			.commit();
			break;
		}
	}
}
