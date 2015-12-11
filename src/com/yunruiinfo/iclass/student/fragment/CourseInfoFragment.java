package com.yunruiinfo.iclass.student.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.AppException;
import com.yunruiinfo.iclass.student.activity.MainActivity;
import com.yunruiinfo.iclass.student.adapter.GridViewCourseAdapter;
import com.yunruiinfo.iclass.student.bean.Course;
import com.yunruiinfo.iclass.student.bean.Icon;
import com.yunruiinfo.iclass.student.bean.Result;
import com.yunruiinfo.iclass.student.bean.Teacher;
import com.yunruiinfo.iclass.student.bean.URLs;
import com.yunruiinfo.iclass.student.util.JsonUtils;
import com.yunruiinfo.iclass.student.util.UIHelper;

public class CourseInfoFragment extends BaseFragment {
	@ViewInject(R.id.course_name)		private TextView mCourseName;
	@ViewInject(R.id.course_lessoncat)	private TextView mLessonCat;
	@ViewInject(R.id.course_tutorname)	private TextView mTutorName;
	@ViewInject(R.id.icons)				private GridView mIconsView;
	private GridViewCourseAdapter mIconsAdapter;
	private int mCourseId;
	private Course mCourse;
	private ProgressDialog mLoadingDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_course_info, container, false);
		ViewUtils.inject(this, view);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().setTitle("课程信息");
		
		try {
			mCourseId = getArguments().getInt("id");
			if (mCourseId == 0) 
				throw new IllegalArgumentException("课程ID不能为0！");
		} catch (Exception e) {
			UIHelper.ToastMessage(getActivity(), "非法访问");
			getFragmentManager().popBackStack();
		}
		
		initView(); //初始化视图
		initData(); //初始化数据
	}
	
	private void initView() {
		mLoadingDialog = new ProgressDialog(getActivity());
		mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mLoadingDialog.setTitle("提示");
		mLoadingDialog.setMessage("正在加载……");
		mLoadingDialog.setCancelable(false);		
		List<Icon> mIcons = new ArrayList<Icon>();
		mIcons.add(new Icon("授课教师", R.drawable.ic_course_teacher, null));
        mIcons.add(new Icon("课程介绍", R.drawable.ic_course_brief, null));
        mIcons.add(new Icon("教学大纲", R.drawable.ic_course_plan, null));
        mIcons.add(new Icon("教学日历", R.drawable.ic_course_calendar, null));
        mIcons.add(new Icon("课程通知", R.drawable.ic_course_duihua, null));
        mIcons.add(new Icon("教学材料", R.drawable.ic_course_files, null));
		mIconsAdapter = new GridViewCourseAdapter(getActivity(), mIcons);
		mIconsView.setAdapter(mIconsAdapter);
		mIconsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mCourse == null) {
					UIHelper.ToastMessage(getActivity(), "正在获取课程信息...");
					return;
				}
				Icon icon = (Icon) mIconsAdapter.getItem(position);
				if ("授课教师".equals(icon.name)) {
					showTeachers(mCourse);
				} else if ("课程介绍".equals(icon.name)) {
					UIHelper.showHtml(getActivity(), icon.name, mCourse.getIntro(), URLs.BASE_URL);
				} else if ("教学大纲".equals(icon.name)) {
					UIHelper.showHtml(getActivity(), icon.name, mCourse.getOutline(), URLs.BASE_URL);
				} else if ("教学日历".equals(icon.name)) {
					UIHelper.showHtml(getActivity(), icon.name, mCourse.getCalendar(), URLs.BASE_URL);
				} else if ("教学材料".equals(icon.name)) {
					//UIHelper.showEolUrl(getActivity(), URLs.EOL_COURSE_FILES + mCourseId);
					Bundle bundle = new Bundle();
					bundle.putSerializable("id", mCourseId);
					MainActivity fca = (MainActivity) getActivity();
					Fragment fragment = Fragment.instantiate(getActivity(), ResourseFragment.class.getName(), bundle);
					fca.switchContent(fragment);
				} else if ("课程通知".equals(icon.name)) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("id", mCourseId);
					MainActivity fca = (MainActivity) getActivity();
					Fragment fragment = Fragment.instantiate(getActivity(), CourseNoticesFragment.class.getName(), bundle);
					fca.switchContent(fragment);
				} else if ("模拟试题".equals(icon.name)) {
					UIHelper.ToastMessage(getActivity(), "功能升级中....");
				} else if ("在线测试".equals(icon.name)){
					UIHelper.ToastMessage(getActivity(), "功能升级中....");
				} else if ("对话教师".equals(icon.name)){
					UIHelper.ToastMessage(getActivity(), "功能升级中....");
				}else {
					if (icon.activity == null){
						UIHelper.ToastMessage(getActivity(), "功能升级中....");
					} else {
						UIHelper.showActivity(getActivity(), icon.activity);
					}
				}
			}
		});
	}	
	private void initData() {
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("type", "course");
		params.addQueryStringParameter("id", Integer.toString(mCourseId));
		http.send(HttpMethod.GET, URLs.API_URL, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				try {
					Result result = JsonUtils.fromJson(responseInfo.result, Result.class);
					if (!result.OK()) {
						throw AppException.custom(result.Message());
					} else {
						mCourse = result.course;
						showCourse(mCourse);
					}
				} catch (JsonSyntaxException e) {
					AppException.json(e).makeToast(getActivity());
				} catch (AppException e) {
					e.makeToast(getActivity());
				} 
			}
			@Override
			public void onStart() {}
			@Override
			public void onFailure(HttpException error, String msg) {
				UIHelper.ToastMessage(getActivity(), "课程信息获取失败，请重试。");
				getFragmentManager().popBackStack();
			}
		});
	}
	
	/**
	 * 显示课程信息
	 * @param course
	 */
	private void showCourse(Course course) {
		if (course == null) return;
		mCourseName.setText(course.getName());
		mLessonCat.setText(course.getLessoncat());
		mTutorName.setText(course.getTutorname());
	}
	
	/**
	 * 选择并显示教师简介
	 * @param course
	 */
	private void showTeachers(Course course) {
		if (course == null) return;
		final List<Teacher> teachers = course.getTeachers();
		if (teachers == null) {
			UIHelper.ToastMessage(getActivity(), "暂无教师信息");
			return;
		}
		if (teachers.size() == 1) {
			showTeacher(teachers.get(0));
			return;
		}
		String[] names = new String[teachers.size()];
		for (int i = 0; i < names.length; i++) {
			names[i] = teachers.get(i).getRealname();
		}
		new AlertDialog.Builder(getActivity())
		.setTitle("请选择教师：")
		.setItems(names, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				showTeacher(teachers.get(which));
			}
		})
		.show();
	}
	/**
	 * 显示教师信息
	 * @param teacher
	 */
	private void showTeacher(Teacher teacher) {
		String title = teacher.getRealname();
		String data = teacher.getSummary();
		UIHelper.showHtml(getActivity(), title, data, URLs.BASE_URL);
	}
}