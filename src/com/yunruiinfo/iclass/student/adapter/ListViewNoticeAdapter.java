package com.yunruiinfo.iclass.student.adapter;

import java.util.Collections;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.bean.Notice;

public class ListViewNoticeAdapter extends BaseAdapter {
	private List<Notice> listItems = Collections.emptyList(); // 数据集合
	private LayoutInflater mContainer; // 视图容器

	static class ViewHolder { // 自定义视图
		public TextView title;
		public TextView user;
		public TextView date;
	}

	public ListViewNoticeAdapter(Context context) {
		mContainer = LayoutInflater.from(context);
	}

	public void setNotices(List<Notice> data) {
		if (data != null) listItems = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mContainer.inflate(R.layout.listitem_notice, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.user = (TextView) convertView.findViewById(R.id.user);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Notice notice = listItems.get(position);
		holder.title.setText(notice.getTitle());
		holder.user.setText(notice.getUser());
		holder.date.setText(notice.getDate());
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