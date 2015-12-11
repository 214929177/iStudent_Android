package com.yunruiinfo.iclass.student.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.bean.Advert;
import com.yunruiinfo.iclass.student.util.UIHelper;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 顶部轮播适配器
 * @author SYZ
 */
public class ViewPagerBannerAdapter extends PagerAdapter {
	private Context mContext;
	private List<Advert> listItems = Collections.emptyList(); // 数据集合
	private List<View> listViews; // 视图集合
	private LayoutInflater listContainer; // 视图容器
	private BitmapUtils bmpUtils;
	static class ViewHolder { //自定义控件集合  
		ImageView image;
		//TextView title;
	}
	
	public ViewPagerBannerAdapter(Context context) {
		this.mContext = context;
		this.bmpUtils = new BitmapUtils(context);
		this.listContainer = LayoutInflater.from(context);
	}
	
	public void setBannerAds(List<Advert> data) {
		this.listItems = data;
		this.listViews = new ArrayList<View>();
		for (int i = 0; i < listItems.size(); i++) {
			View view = listContainer.inflate(R.layout.pageritem_banner, null);
			
			ViewHolder holder = new ViewHolder();
			holder.image = (ImageView) view.findViewById(R.id.image);
			//holder.title = (TextView) view.findViewById(R.id.title);
			
			view.setTag(holder);
			
			listViews.add(view);
		}
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return listItems.get(position).getName();
	}
	
	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View view = listViews.get(position);
		Advert ad = listItems.get(position);
		ImageView image = ((ViewHolder) view.getTag()).image;
		bmpUtils.display(image, ad.getPic());
		//holder.title.setText(ad.getName());
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = listItems.get(position).getLink();
				UIHelper.showUrl(mContext, url);
			}
		});
		container.addView(view, 0);
		return view;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(listViews.get(position));
	}

}
