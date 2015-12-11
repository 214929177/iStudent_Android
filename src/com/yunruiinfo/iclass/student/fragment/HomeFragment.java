package com.yunruiinfo.iclass.student.fragment;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.adapter.QauNewsPagerAdapter;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class HomeFragment extends BaseFragment {
	
	@ViewInject(R.id.title_layout)	private LinearLayout mTitleLayout;
	@ViewInject(R.id.pager)			private ViewPager mNewsPager;
	private QauNewsPagerAdapter mPagerAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		//include banner
		getChildFragmentManager().beginTransaction()
		.replace(R.id.home_banner, new BannerFragment())
		.commit();
		//include qau news
		mPagerAdapter = new QauNewsPagerAdapter(getChildFragmentManager());
		mNewsPager.setAdapter(mPagerAdapter);
		mNewsPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				for (int i = 0; i < mTitleLayout.getChildCount(); i++) {
					mTitleLayout.getChildAt(i).setSelected(false);
				}
				mTitleLayout.getChildAt(position).setSelected(true);
			}
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
			@Override
			public void onPageScrollStateChanged(int state) {}
		});
		View.OnClickListener cl = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = 0;
				for (int i = 0; i < mTitleLayout.getChildCount(); i++) {
					mTitleLayout.getChildAt(i).setSelected(false);
					if (v == mTitleLayout.getChildAt(i)) {
						position = i;
					}
				}
				v.setSelected(true);
				mNewsPager.setCurrentItem(position);
			}
		};
		for (int i = 0; i < mTitleLayout.getChildCount(); i++) {
			mTitleLayout.getChildAt(i).setOnClickListener(cl);
		}
		mTitleLayout.getChildAt(0).setSelected(true);
	}

	@Override
	public void onStart(){
		super.onStart();
		getActivity().setTitle("校园新闻");
	}
}
