package com.yunruiinfo.iclass.student.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.bean.Advert;
import com.yunruiinfo.iclass.student.util.UIHelper;

public class ViewPagerAdvertAdapter   extends PagerAdapter {
	private Context mContext;
	private List<Advert> listItems = Collections.emptyList(); // 数据集合
	private List<View> listViews; // 视图集合
	private LayoutInflater listContainer; // 视图容器
	static class ViewHolder {  //自定义控件集合  
		ImageView image;
		TextView title;
	}
	public ViewPagerAdvertAdapter(Context context, List<Advert> data) {
		this.mContext = context;
		this.listItems = data;
		listContainer = LayoutInflater.from(context);
		listViews = new ArrayList<View>();
		for (int i = 0; i < listItems.size(); i++) {
			View view = listContainer.inflate(R.layout.pageritem_advert, null);
			ViewHolder holder = new ViewHolder();
			holder.image = (ImageView) view.findViewById(R.id.image);
			holder.title = (TextView) view.findViewById(R.id.title);
			Advert advert = listItems.get(i);
			listViews.add(view);
		}   
	}	    
	@Override
	public int getCount()  {
		return Integer.MAX_VALUE;
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1){
		return arg0 == (arg1);
	}
	@Override
	public Object instantiateItem(ViewGroup container, final int position){
		//container.addView(listViews.get(position % listViews.size()), 0);	
		View view=listViews.get(position % listViews.size());//切记超出数组界限问题，取余来解决	View view=listViews.get(position)之前是这样出错的
		view.setOnClickListener(new View.OnClickListener(){			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//String url=listItems.get(position).getUrl();
				String url="http://www.baidu.com";
				UIHelper.openBrowser(mContext, url);
			}
		});
		 // return listViews.get(position % listViews.size());
		    container.addView(view, 0);
			return view;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(listViews.get(position % listViews.size()));
	}
}
