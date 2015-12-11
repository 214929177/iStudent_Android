package com.yunruiinfo.iclass.student.fragment;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonParseException;
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
import com.yunruiinfo.iclass.student.adapter.ListViewCourseAdapter;
import com.yunruiinfo.iclass.student.bean.Course;
import com.yunruiinfo.iclass.student.bean.Result;
import com.yunruiinfo.iclass.student.bean.URLs;
import com.yunruiinfo.iclass.student.util.JsonUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class CourseListFragment extends BaseFragment{
	@ViewInject(R.id.empty_text)	private TextView mEmptyView;
	@ViewInject(R.id.course_list)	private ListView mCoursesView;
	private List<Course> mCoursesData = new ArrayList<Course>();
	private ListViewCourseAdapter mCourseAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list_course, container, false);
		ViewUtils.inject(this, view);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().setTitle("在线课程");
		
		mCoursesView.setEmptyView(mEmptyView);
		mCourseAdapter = new ListViewCourseAdapter(getActivity());
		mCourseAdapter.setOnItemClickListener(new ListViewCourseAdapter.OnItemClickListener() {
			@Override
			public void onClick(int position) {
				Course course = (Course) mCourseAdapter.getItem(position);
				showResourses(course.getId());
			}
		});
		mCoursesView.setAdapter(mCourseAdapter);
		mCoursesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Course course = (Course) mCourseAdapter.getItem(position);
				showCourse(course.getId());
			}
		});
		
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("type", "courses");
		params.addQueryStringParameter("userId", Integer.toString(appContext.getUserId()));
		http.send(HttpMethod.GET, URLs.API_URL, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				try {
					Result result = JsonUtils.fromJson(responseInfo.result, Result.class);
					if (!result.OK()) {
						throw AppException.custom(result.Message());
					} else if (result.courses.size() > 0) {
						mCoursesData = result.courses;
						mCourseAdapter.setCourses(mCoursesData);
						mCourseAdapter.notifyDataSetChanged();
					} else {
						mEmptyView.setText("暂无课程数据");
					}
				} catch (JsonParseException e) {
					AppException.json(e).makeToast(getActivity());
				} catch (AppException e) {
					e.makeToast(getActivity());
				} 
			}
			@Override
			public void onStart() {
				mEmptyView.setText("正在获取课程列表");
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				mEmptyView.setText("暂无课程数据");
			}
		});
	}
	
	private void showCourse(int id) {
		Bundle bundle = new Bundle();
		bundle.putInt("id", id);
		MainActivity fca = (MainActivity) getActivity();
		Fragment fragment = Fragment.instantiate(getActivity(), CourseInfoFragment.class.getName(), bundle);
		fca.switchContent(fragment);
	}
	
	/**
	 * 显示教学材料
	 * @param courseId
	 */
	private void showResourses(int courseId) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("id", courseId);
		MainActivity fca = (MainActivity) getActivity();
		Fragment fragment = Fragment.instantiate(getActivity(), ResourseFragment.class.getName(), bundle);
		fca.switchContent(fragment);
	}
}
