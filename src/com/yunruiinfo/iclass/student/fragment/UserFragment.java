package com.yunruiinfo.iclass.student.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.activity.LoginActivity;
import com.yunruiinfo.iclass.student.bean.User;
import com.yunruiinfo.iclass.student.util.UIHelper;

public class UserFragment  extends BaseFragment {
	
	@ViewInject(R.id.realname)		private TextView mRealName;
	@ViewInject(R.id.realname2)		private TextView mRealName2;
	@ViewInject(R.id.username)		private TextView mUserName;
	@ViewInject(R.id.gender)		private TextView mGender;		//性别
	@ViewInject(R.id.summary)		private TextView mSummary;
	@ViewInject(R.id.summary2)		private TextView mSummary2;
	@ViewInject(R.id.email)			private TextView mEmail;
	@ViewInject(R.id.phone)			private TextView mPhone;
	@ViewInject(R.id.mobile)		private TextView mMobile;
	@ViewInject(R.id.numb_student)	private TextView mNumbStudent;	//学号
	@ViewInject(R.id.dep_name)		private TextView mDepName;		//院系
	@ViewInject(R.id.major_name)	private TextView mMajorName;	//专业
	@ViewInject(R.id.grad_title)	private TextView mGradTitle;	//年级
	@ViewInject(R.id.class_name)	private TextView mClassName;	//班级
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_user, container, false);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().setTitle("个人资料");
		
		getUserInfo();//获取用户信息
	}
	
	@OnClick({R.id.edit_layout, R.id.logout})
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.edit_layout:
			UIHelper.ToastMessage(getActivity(), "功能升级中...");
			break;
		case R.id.logout:
			if (appContext.getUserId() > 0) {
				new AlertDialog.Builder(getActivity())
				.setTitle("确定退出账号？")
				.setMessage("将会退出当前账号，您可以在登录界面重新登录。")
				.setNegativeButton("取消", null)
				.setPositiveButton("退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						appContext.logout();
						UIHelper.ToastMessage(getActivity(), "成功退出");
						getFragmentManager().popBackStack();
						switchFragment(new HomeNewFragment());
					}
				})
				.create().show();
			} else {
				UIHelper.ToastMessage(getActivity(), "您尚未登录");
			}
			break;
		}
	}
	
	private void getUserInfo() {
		if (appContext.getUserId() > 0) {
			User user = appContext.getUserInfo();
			mRealName.setText(user.getRealname());
			mRealName2.setText(user.getRealname());
			mUserName.setText(user.getUsername());
			String gender = "";
			if ("1".equals(user.getGender())) {
				gender = "男";
			} else if ("2".equals(user.getGender())) {
				gender = "女";
			} else {
				gender = "未知";
			}
			mGender.setText(gender);
			mSummary.setText(user.getSummary());
			mSummary2.setText(user.getSummary());
			mEmail.setText(user.getEmail());
			mPhone.setText(user.getPhone());
			mMobile.setText(user.getMobile());
			mNumbStudent.setText(user.getNumbStudent());
			mDepName.setText(user.getDepName());
			mMajorName.setText(user.getMajorName());
			mGradTitle.setText(user.getGradTitle());
			mClassName.setText(user.getClassName());
		} else {
			UIHelper.ToastMessage(getActivity(), "请先登录...");
			UIHelper.showActivity(getActivity(), LoginActivity.class);
			getFragmentManager().popBackStack();
		}
	}
}