package com.yunruiinfo.iclass.student.adapter;

import java.util.ArrayList;
import java.util.List;

import com.yunruiinfo.iclass.student.fragment.QauNewsListFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class QauNewsPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> mFragments;
	public QauNewsPagerAdapter(FragmentManager fm) {
		super(fm);
		mFragments = new ArrayList<Fragment>();
		mFragments.add(QauNewsListFragment.newInstance("recent"));	//最新动态
		mFragments.add(QauNewsListFragment.newInstance("notice"));	//通知公告
		mFragments.add(QauNewsListFragment.newInstance("academic"));//学术信息
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

}
