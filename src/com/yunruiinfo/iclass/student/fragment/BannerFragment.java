package com.yunruiinfo.iclass.student.fragment;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.JsonParseException;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.AppException;
import com.yunruiinfo.iclass.student.adapter.ViewPagerBannerAdapter;
import com.yunruiinfo.iclass.student.bean.Advert;
import com.yunruiinfo.iclass.student.bean.Result;
import com.yunruiinfo.iclass.student.bean.URLs;
import com.yunruiinfo.iclass.student.util.JsonUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BannerFragment extends BaseFragment {
	@ViewInject(R.id.pager_title)	private TextView mPagerTitle;
	@ViewInject(R.id.mark_layout)	private LinearLayout mMarkLayout;
	@ViewInject(R.id.viewpager)		private ViewPager mViewPager;
	private ViewPagerBannerAdapter mAdvertAdapter;
	private List<Advert> mAdvertData;
	
	Timer mTimer;
	TimerTask mTask;
	Handler mHandler;
	
	Activity mActivity;//保存Activity，避免getActivity() == null
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_banner, container, false);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (mHandler == null || mTimer == null) {
			mHandler = new Handler() {
				public void handleMessage(Message msg) {
					int current = mViewPager.getCurrentItem();
					int count = mAdvertAdapter.getCount();
					if (count > 0) {
						mViewPager.setCurrentItem((current + 1) % count);
					}
				};
			};
			mTask = new TimerTask() {
				@Override
				public void run() {
					mHandler.obtainMessage().sendToTarget();
				}
			};
			mTimer = new Timer();
			mTimer.schedule(mTask, 5000, 5000);
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		loadAdverts();	//加载轮播广告
	}

	private void setMarks(int count) {
		if (count == mMarkLayout.getChildCount()) {
			return;
		}
		if (count > 0) {
			mMarkLayout.removeAllViews();
			int width = mActivity.getResources().getDimensionPixelOffset(R.dimen.pager_mark_width);
			int height = mActivity.getResources().getDimensionPixelOffset(R.dimen.pager_mark_height);
			int margin = mActivity.getResources().getDimensionPixelOffset(R.dimen.pager_mark_margin);
			for (int i = 0; i < count; i++) {
				ImageView mark = new ImageView(mActivity);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
				lp.setMargins(margin, margin, margin, margin);
				mark.setLayoutParams(lp);
				mark.setImageResource(R.drawable.pager_mark_selector);
				mMarkLayout.addView(mark);
			}
			mPagerTitle.setText(mAdvertAdapter.getPageTitle(0));
			mMarkLayout.getChildAt(0).setSelected(true);
		}
	}
	
	private void loadAdverts() {
		mAdvertAdapter = new ViewPagerBannerAdapter(getActivity());
		mViewPager.setAdapter(mAdvertAdapter);
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				mPagerTitle.setText(mAdvertAdapter.getPageTitle(position));
				for (int i = 0; i < mMarkLayout.getChildCount(); i++) {
					mMarkLayout.getChildAt(i).setSelected(false);
				}
				mMarkLayout.getChildAt(position).setSelected(true);
			}
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
			@Override
			public void onPageScrollStateChanged(int state) {}
		});
		
		final DbUtils db = DbUtils.create(getActivity());
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("act", "advert_list");
		params.addBodyParameter("position", "banner");
		http.send(HttpMethod.GET, URLs.YR_API, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				try {
					Result result = JsonUtils.fromJson(responseInfo.result, Result.class);
					if (!result.OK()) {
						throw AppException.custom(result.Message());
					} else if (result.ads.size() > 0) {
						db.dropTable(Advert.class);
						mAdvertData = result.ads;
						mAdvertAdapter.setBannerAds(mAdvertData);
						mAdvertAdapter.notifyDataSetChanged();
						setMarks(mAdvertAdapter.getCount());
						db.saveAll(mAdvertData);
					}
				} catch (JsonParseException e) {
					e.printStackTrace();
					AppException.json(e).makeToast(getActivity());
				} catch (DbException e) {
					e.printStackTrace();
					AppException.sqlite(e).makeToast(getActivity());
				} catch (AppException e) {
					e.printStackTrace();
					e.makeToast(getActivity());
				}
			}
			@Override
			public void onStart() {
				try {
					mAdvertData = db.findAll(Advert.class);
					if (mAdvertData != null) {
						mAdvertAdapter.setBannerAds(mAdvertData);
						mAdvertAdapter.notifyDataSetChanged();
						setMarks(mAdvertAdapter.getCount());
					}
				} catch (DbException e) {
					e.printStackTrace();
					AppException.sqlite(e).makeToast(getActivity());
				}
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				appContext.checkNetworkConnected();
			}
		});
	}
}
