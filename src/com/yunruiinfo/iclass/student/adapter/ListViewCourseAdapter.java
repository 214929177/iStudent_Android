package com.yunruiinfo.iclass.student.adapter;

import java.util.Collections;
import java.util.List;

import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.bean.Course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewCourseAdapter extends BaseAdapter {
    private List<Course> listItems = Collections.emptyList();    //数据集合
    private LayoutInflater mContainer;  //视图容器
    static class ViewHolder { //自定义视图
        public ImageView icon;
        public TextView name;
        public TextView teacher;
        public Button resourse;
    }
    /**
     * listitem内view点击事件监听器
     * @author SYZ
     */
    public interface OnItemClickListener {
    	public void onClick(int position);
    }
    private OnItemClickListener mItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
		mItemClickListener = listener;
	}
    
	public ListViewCourseAdapter(Context context){
		mContainer = LayoutInflater.from(context);
	}
	
	public void setCourses(List<Course> data) {
		if (data != null) listItems = data;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null){
			convertView = mContainer.inflate(R.layout.listitem_course, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.teacher = (TextView) convertView.findViewById(R.id.teacher);
			holder.resourse = (Button) convertView.findViewById(R.id.resourse);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Course course = listItems.get(position);
		holder.name.setText(course.getName());
		holder.teacher.setText(course.getTutorname());
		holder.resourse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mItemClickListener != null) {
					mItemClickListener.onClick(position);
				}
			}
		});
		return convertView;
	}
	@Override
	public int getCount() {
		return listItems.size();
	}
	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}
	@Override
	public long getItemId(int position){
		return 0;
	}
}
