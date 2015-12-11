package com.yunruiinfo.iclass.student.adapter;

import java.util.Collections;
import java.util.List;

import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.bean.Resourse;
import com.yunruiinfo.iclass.student.util.FileUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewResourseAdapter extends BaseAdapter {
    private List<Resourse> listItems = Collections.emptyList();    //数据集合
    private LayoutInflater mContainer;  //视图容器
    static class ViewHolder { //自定义视图
        public ImageView icon;
        public TextView name;
        public TextView size;
    }

	public ListViewResourseAdapter(Context context){
		mContainer = LayoutInflater.from(context);
	}
	
	private boolean isLoaded = false;	// 加载完成
	public void loaded() {
		isLoaded = true;
	}
	public boolean isLoaded() {
		return isLoaded;
	}
	
	public void setResourses(List<Resourse> data) {
		if (data != null) listItems = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null){
			convertView = mContainer.inflate(R.layout.listitem_resourse, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.size = (TextView) convertView.findViewById(R.id.size);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Resourse resourse = listItems.get(position);
		holder.name.setText(resourse.getName());
		holder.size.setText(FileUtils.formatFileSize(resourse.getSize()));
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
