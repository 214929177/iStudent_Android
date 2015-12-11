package com.yunruiinfo.iclass.student.adapter;

import java.util.ArrayList;
import java.util.List;

import com.yunruiinfo.iclass.student.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewMenuAdapter extends BaseAdapter {
    private List<SlideMenu> mMenus = new ArrayList<SlideMenu>();//数据集合
    private LayoutInflater mContainer;  //视图容器
    public class SlideMenu{
    	public int icon;
    	public String name;
    	public int id;
    	public SlideMenu(String name, int icon, int id){
    		this.icon = icon;
    		this.name = name;
    		this.id = id;
    	}
    }    
    static class ViewHolder {//自定义视图
        public ImageView icon;
        public TextView name;
    }
	public ListViewMenuAdapter(Context context){
		mMenus.add(new SlideMenu("首页",   R.drawable.ic_menu_home,R.id.menu_home));
		mMenus.add(new SlideMenu("课程学习",  R.drawable.ic_menu_course, R.id.menu_course));
		mMenus.add(new SlideMenu("个人资料",  R.drawable.ic_menu_user, R.id.menu_user));
		mMenus.add(new SlideMenu("信息中心",  R.drawable.ic_menu_info, R.id.menu_info));
		mMenus.add(new SlideMenu("意见反馈",R.drawable.ic_menu_school, R.id.menu_feedback));
		mMenus.add(new SlideMenu("设置" ,    R.drawable.ic_menu_setting, R.id.menu_setting));
		mContainer = LayoutInflater.from(context);
	}
	private int selectedPosition = 0;// 选中位置，默认第一个
	public void setSelectedPosition(int selectedPosition){
		this.selectedPosition = selectedPosition;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder;
		if (convertView == null){
			convertView = mContainer.inflate(R.layout.listitem_menu, null);  
			holder = new ViewHolder();
			holder.icon = (ImageView)convertView.findViewById(R.id.icon);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SlideMenu menu = mMenus.get(position);
		holder.name.setText(menu.name);
		holder.icon.setImageResource(menu.icon);
		if (selectedPosition == position){
			holder.icon.setSelected(true);
			holder.name.setSelected(true);
			holder.icon.setPressed(true);
			holder.name.setPressed(true);
			convertView.setBackgroundResource(R.drawable.bg_slidingmenu_selected);
		}else{
			holder.icon.setSelected(false);
			holder.name.setSelected(false);
			holder.icon.setPressed(false);
			holder.name.setPressed(false);
			convertView.setBackgroundResource(R.drawable.bg_slidingmenu_selector);
		}
		return convertView;
	}
	@Override
	public int getCount(){
		return mMenus.size();
	}
	@Override
	public Object getItem(int position){
		return mMenus.get(position);
	}
	@Override
	public long getItemId(int position){
		return 0;
	}
}
