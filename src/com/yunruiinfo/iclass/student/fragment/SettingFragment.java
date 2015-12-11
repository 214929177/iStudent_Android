package com.yunruiinfo.iclass.student.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.adapter.GridViewIconAdapter;
import com.yunruiinfo.iclass.student.bean.Icon;
import com.yunruiinfo.iclass.student.bean.URLs;
import com.yunruiinfo.iclass.student.util.UIHelper;
import com.yunruiinfo.iclass.student.util.UpdateManager;


public class SettingFragment extends BaseFragment {
	@ViewInject(R.id.icons)			private GridView mIconsView;
	private GridViewIconAdapter mIconsAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		getChildFragmentManager().beginTransaction()
		.replace(R.id.setting_banner, new BannerFragment())
		.commit();
		
		List<Icon> mIcons = new ArrayList<Icon>();
		mIcons.add(new Icon("检查新版本", R.drawable.ic_home_elecive, null));
        mIcons.add(new Icon("退出当前账号", R.drawable.ic_set_tuichu, null));
        mIcons.add(new Icon("意见反馈", R.drawable.ic_home_course, null));        
		mIconsAdapter = new GridViewIconAdapter(getActivity(), mIcons);
		mIconsView.setAdapter(mIconsAdapter);
		mIconsView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id){
				Icon icon = (Icon) mIconsAdapter.getItem(position);				
				if ("意见反馈".equals(icon.name)) {
					UIHelper.showUrl(getActivity(), URLs.FEED_BACK);
				} else if ("检查新版本".equals(icon.name)) {
					UpdateManager.getUpdateManager().checkAppUpdate(getActivity(), true);
				} else if ("退出当前账号".equals(icon.name)) {
					if (appContext.getUserId() > 0) {
						new AlertDialog.Builder(getActivity())
						.setTitle("退出账号？")
						.setMessage("确定注销当前账号吗？")
						.setNegativeButton("取消", null)
						.setPositiveButton("退出", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								appContext.logout();
								UIHelper.ToastMessage(getActivity(), "已退出");
							}
						})
						.create().show();
					} else {
						UIHelper.ToastMessage(getActivity(), "您尚未登录");
					}
				} else {
					UIHelper.ToastMessage(getActivity(), "功能升级中...");
				}
			}
		});
	}
	
	@Override
	public void onStart() {
		super.onStart();
		getActivity().setTitle("设置");
	}
}
