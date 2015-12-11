package com.yunruiinfo.iclass.student.fragment;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.activity.LoginActivity;
import com.yunruiinfo.iclass.student.activity.MainActivity;
import com.yunruiinfo.iclass.student.activity.SettingActivity;
import com.yunruiinfo.iclass.student.adapter.ListViewMenuAdapter;
import com.yunruiinfo.iclass.student.adapter.ListViewMenuAdapter.SlideMenu;
import com.yunruiinfo.iclass.student.bean.URLs;
import com.yunruiinfo.iclass.student.bean.User;
import com.yunruiinfo.iclass.student.util.UIHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MenuFragment extends BaseFragment {
	
	@ViewInject(R.id.realname)			private TextView mRealName;
	@ViewInject(R.id.slidingmenu_body)	private ListView mMenusView;
	
	private ListViewMenuAdapter mMenusAdapter;
	private int mSelectedPosition = 0;	//当前选中菜单

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu, container, false);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setUserVisibleHint(getUserVisibleHint());
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && appContext.getUserId() > 0) {
			User user = appContext.getUserInfo();
			mRealName.setText(user.getRealname());
		} else {
			mRealName.setText("请登录...");
		}
	}
	
	@OnClick(R.id.account_layout)
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.account_layout:
			if (appContext.getUserId() > 0) {
				switchFragment(new UserFragment());
			} else {
				UIHelper.showActivity(getActivity(), LoginActivity.class);
			}
			break;
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mMenusAdapter = new ListViewMenuAdapter(getActivity());
		mMenusView.setAdapter(mMenusAdapter);
		mMenusView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id){
				//已选中该菜单
				if (mSelectedPosition == position) return;
				
				SlideMenu menu = (SlideMenu) mMenusAdapter.getItem(position);
				switch (menu.id){
				case R.id.menu_home:
					switchFragment(new HomeNewFragment());
					break;
				case R.id.menu_course:
					switchFragment(new CourseFragment());
					break;
				case R.id.menu_user:
					if (appContext.getUserId() == 0) {
						UIHelper.ToastMessage(getActivity(), "请先登录...");
						UIHelper.showActivity(getActivity(), LoginActivity.class);
						return;
					}
					switchFragment(new UserFragment());
					break;
				case R.id.menu_info:
					switchFragment(new InfoFragment());
					break;
				case R.id.menu_school:
					//暂时打开百度贴吧
					UIHelper.showUrl(getActivity(), "http://tieba.baidu.com/f?kw=%C7%E0%B5%BA%C5%A9%D2%B5%B4%F3%D1%A7&fr=ala0");
					return;
					//switchFragment(new SchoolFragment());
					//break;
				case R.id.menu_feedback:
					UIHelper.showUrl(getActivity(), URLs.FEED_BACK);
					return;
				case R.id.menu_setting:
					//switchFragment(new SettingFragment());
					//break;
					UIHelper.showActivity(getActivity(), SettingActivity.class);
					return;
				}
				setSelectedMenu(position);
			}
		});
	}
	
	/**
	 * 根据当前显示Fragment选中相应菜单
	 * @param fragment
	 */
	public void setSelectedMenu(Class<? extends Fragment> fragment) {
		int position = -1;
		if (fragment == HomeFragment.class) {
			position = 0;
		} else if (fragment == CourseFragment.class) {
			position = 1;
		} else if (fragment == UserFragment.class) {
			position = 2;
		} else if (fragment == InfoFragment.class) {
			position = 3;
		} else if (fragment == SchoolFragment.class) {
			position = 4;
		} else if (fragment == SettingNewFragment.class) {
			position = 5;
		}
		if (position >= 0) setSelectedMenu(position);
	}
	/**
	 * 选中相应菜单
	 * @param fragment
	 */
	private void setSelectedMenu(int position) {
		mSelectedPosition = position;
		mMenusAdapter.setSelectedPosition(position);
		mMenusAdapter.notifyDataSetInvalidated();
	}

	@Override
	protected void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof MainActivity) {
			//获取主Activity
			MainActivity fca = (MainActivity) getActivity();
			//切换MenuFragment中的Fragment时先清除以前的Fragment
			FragmentManager fm = fca.getSupportFragmentManager();
			int count = fm.getBackStackEntryCount();
			for (int i = 0; i < count; i++) {
				fm.popBackStack();
			}
			//切换Fragment
			fca.switchContent(fragment);
		}
	}
}
