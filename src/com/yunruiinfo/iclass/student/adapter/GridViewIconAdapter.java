package com.yunruiinfo.iclass.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.bean.Icon;
 

/**
 * User: SYZ
 * 
 */
public class GridViewIconAdapter extends BaseAdapter {
    private List<Icon> mIcons = new ArrayList<Icon>();    //数据集合
    private LayoutInflater mContainer;  //视图容器
    
    static class GridItemView { //自定义视图
        public ImageView icon;
        public TextView name;
    }

    public GridViewIconAdapter(Context context, List<Icon> icons){
        mIcons = icons;
        mContainer = LayoutInflater.from(context);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridItemView gridItemView;
        if(convertView == null){
            convertView = mContainer.inflate(R.layout.griditem_icon, null);
            gridItemView = new GridItemView();
            gridItemView.icon = (ImageView) convertView.findViewById(R.id.icon);
            gridItemView.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(gridItemView);
        } else {
            gridItemView = (GridItemView) convertView.getTag();
        }       
        Icon icon = mIcons.get(position);
        gridItemView.name.setText(icon.name);
        gridItemView.icon.setImageResource(icon.icon);
        return convertView;
    }
    @Override
    public int getCount() {
        return mIcons.size();
    }
    @Override
    public Object getItem(int position) {
        return mIcons.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
}
