package com.yunruiinfo.iclass.student.adapter;

import java.util.Collections;
import java.util.List;

import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.bean.News;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewNewsAdapter extends BaseAdapter {
	private List<News> listItems = Collections.emptyList(); // 数据集合
	private LayoutInflater mContainer; // 视图容器

	static class ViewHolder { // 自定义视图
		public ImageView icon;
		public TextView title;
		public TextView branch;
		public TextView time;
	}

	public ListViewNewsAdapter(Context context, List<News> mnewsData) {
		listItems = mnewsData;
		mContainer = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mContainer.inflate(R.layout.listitem_news, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.branch = (TextView) convertView.findViewById(R.id.branch);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		News news = listItems.get(position);
		holder.title.setText(news.getName());
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
	public long getItemId(int position) {
		return 0;
	}
}
