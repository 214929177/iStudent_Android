package com.yunruiinfo.iclass.student.adapter;

import java.util.Collections;
import java.util.List;

import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.bean.QauNews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewQauNewsAdapter extends BaseAdapter {
	private List<QauNews> listItems = Collections.emptyList(); // 数据集合
	private LayoutInflater mContainer; // 视图容器

	static class ViewHolder { // 自定义视图
		public TextView title;
		public TextView time;
	}

	public ListViewQauNewsAdapter(Context context) {
		mContainer = LayoutInflater.from(context);
	}
	
	public void setNews(List<QauNews> data) {
		if (data != null) listItems = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mContainer.inflate(R.layout.listitem_qau_news, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		QauNews news = listItems.get(position);
		holder.title.setText(news.getTitle());
		holder.time.setText(news.getTime());
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
