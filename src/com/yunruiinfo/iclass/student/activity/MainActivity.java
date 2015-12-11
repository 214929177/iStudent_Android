package com.yunruiinfo.iclass.student.activity;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.networkbench.agent.impl.NBSAppAgent;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.AppContext;
import com.yunruiinfo.iclass.student.AppManager;
import com.yunruiinfo.iclass.student.bean.URLs;
import com.yunruiinfo.iclass.student.fragment.HomeNewFragment;
import com.yunruiinfo.iclass.student.fragment.MenuFragment;
import com.yunruiinfo.iclass.student.util.UIHelper;
import com.yunruiinfo.iclass.student.util.UpdateManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends SlidingFragmentActivity implements OnClickListener{
	@ViewInject(R.id.left_button)	private ImageView mSlide;
	@ViewInject(R.id.main_title)	private TextView mTitle;
	private SlidingMenu mSlidingMenu;
	private MenuFragment mMenuFragment;
	private AppContext appContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
		appContext = (AppContext) getApplication();
		// set the Above View
		setContentView(R.layout.activity_main);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.main_frame, new HomeNewFragment())
		.addToBackStack(null)
		.commit();
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		mMenuFragment = new MenuFragment();
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, mMenuFragment)
		.commit();
		// customize the SlidingMenu
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		mSlidingMenu.setShadowDrawable(R.drawable.shadow);
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mSlidingMenu.setFadeDegree(0.35f);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mSlidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
			@Override
			public void onOpen() {
				mMenuFragment.setUserVisibleHint(true);
			}
		});
		ViewUtils.inject(this);
		mSlide.setImageResource(R.drawable.ic_home_slide);
		mSlide.setOnClickListener(this);
		
		//初始化用户信息
		appContext.initUserInfo();
		//检测网络是否可用
		appContext.checkNetworkConnected();
		
		//登录用户打开应用时通知运营平台
		final int userId = appContext.getUserId();
		if (userId > 0) {
			RequestParams params = new RequestParams(){{
				addQueryStringParameter("act", "user_online");
				addBodyParameter("user_id", Integer.toString(userId));
			}};
			new HttpUtils().send(HttpMethod.GET, URLs.YR_API, params, null);
		}
		//检查新版本
		UpdateManager.getUpdateManager().checkAppUpdate(this, false);
	}
	/**
	 * 根据当前显示Fragment选中相应菜单
	 * @param fragment
	 */
	public void setSelectedMenu(Class<? extends Fragment> fragment) {
		mMenuFragment.setSelectedMenu(fragment);
	}
	public void switchContent(Fragment fragment) {
		getSupportFragmentManager()
		.beginTransaction()
		.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
		.replace(R.id.main_frame, fragment)
		.addToBackStack(null)
		.commit();
		mSlidingMenu.showContent();
	}
	@Override
	protected void onTitleChanged(CharSequence title, int color) {
		super.onTitleChanged(title, color);
		if (mTitle != null) {
			mTitle.setText(title);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean flag = true;
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			mSlidingMenu.toggle();
			break;
		case KeyEvent.KEYCODE_BACK:
			int count = getSupportFragmentManager().getBackStackEntryCount();
			if (count > 1) {
				getSupportFragmentManager().popBackStack();
			} else {
				UIHelper.appExit(this);
			}
			break;
		default:
			flag = super.onKeyDown(keyCode, event);
			break;
		}
		return flag;              
	}   
	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.left_button:
			mSlidingMenu.toggle();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}
}