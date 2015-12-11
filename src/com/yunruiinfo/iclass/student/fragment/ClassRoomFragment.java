package com.yunruiinfo.iclass.student.fragment;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yunruiinfo.iclass.student.R;


public class ClassRoomFragment  extends BaseFragment{

	Timer mTimer;
	TimerTask mTask;
	Handler mHandler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_classroom, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);		

	}
	
	@Override
	public void onStart() {
		super.onStart();
		getActivity().setTitle("查空教室");
		
//		if (mHandler == null || mTimer == null) {
//			mHandler = new Handler() {
//				public void handleMessage(Message msg) {
//					mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
//				};
//			};
//			mTask = new TimerTask() {
//				@Override
//				public void run() {
//					mHandler.obtainMessage().sendToTarget();
//				}
//			};
//			mTimer = new Timer();
//		}
//		mTimer.schedule(mTask, 2000, 2000);
	}
}
