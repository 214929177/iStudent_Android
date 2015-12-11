package com.yunruiinfo.iclass.student.fragment;

import java.util.ArrayList;
import java.util.List;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.adapter.GridViewIconAdapter;
import com.yunruiinfo.iclass.student.bean.Icon;
import com.yunruiinfo.iclass.student.util.UIHelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class UsedToolsFragment extends BaseFragment {
	@ViewInject(R.id.icons)	private GridView mIconsView;
	private GridViewIconAdapter mIconsAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_used_tools, container, false);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);		
		
		getChildFragmentManager().beginTransaction()
		.replace(R.id.used_tools_banner, new BannerFragment())
		.commit();
		
		List<Icon> mIcons = new ArrayList<Icon>();
        mIcons.add(new Icon("农大校历", R.drawable.ic_icon_course_table, null));
        mIcons.add(new Icon("百度翻译", R.drawable.ic_icon_translate, null));
        mIcons.add(new Icon("百度百科", R.drawable.ic_icon_baike, null));
        mIcons.add(new Icon("百度地图", R.drawable.ic_icon_baidumap, null));
        mIcons.add(new Icon("驾校一点通", R.drawable.ic_icon_jiaxia, null));
        mIcons.add(new Icon("新浪星座", R.drawable.ic_icon_xingzuo, null));
        mIcons.add(new Icon("快递100", R.drawable.ic_icon_kuaidi100, null));
        mIcons.add(new Icon("天气查询", R.drawable.ic_icon_weather, null));
        mIcons.add(new Icon("读书", R.drawable.ic_icon_read, null));
        mIcons.add(new Icon("火车查询", R.drawable.ic_icon_train, null));
        mIcons.add(new Icon("旅游", R.drawable.ic_icon_travel, null));
		mIconsAdapter = new GridViewIconAdapter(getActivity(), mIcons);
		mIconsView.setAdapter(mIconsAdapter);
		mIconsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Icon icon = (Icon) mIconsAdapter.getItem(position);
				if ("农大校历".equals(icon.name)) {
					UIHelper.showUrl(getActivity(), "file:///android_asset/xiaoli.htm");
				} else if ("百度翻译".equals(icon.name)) {
					UIHelper.showUrl(getActivity(), "http://fanyi.baidu.com/");
				} else if ("百度百科".equals(icon.name)) {
                    UIHelper.showUrl(getActivity(), "http://baike.baidu.com/");
                } else if ("驾校一点通".equals(icon.name)) {
                    UIHelper.showUrl(getActivity(), "http://mnks.jxedt.com/");
                } else if ("快递100".equals(icon.name)) {
                    UIHelper.showUrl(getActivity(), "http://www.kuaidi100.com/");
                } else if ("百度地图".equals(icon.name)) {
                    UIHelper.showUrl(getActivity(), "http://map.baidu.com/");
                } else if ("新浪星座".equals(icon.name)) {
                    UIHelper.showUrl(getActivity(), "http://ast.sina.cn/?vt=4");
                } else if ("火车查询".equals(icon.name)) {
                    UIHelper.showUrl(getActivity(), "http://train.qunar.com/?ex_track=bd_aladding_train_tb_title");
                } else if ("天气查询".equals(icon.name)) {
                    UIHelper.showUrl(getActivity(), "http://weather.html5.qq.com");//w.sina.cn
                } else if ("读书".equals(icon.name)) {
                    UIHelper.showUrl(getActivity(), "http://book.sina.cn/");
                } else if ("旅游".equals(icon.name)) {
                    UIHelper.showUrl(getActivity(), "http://travel.sina.cn/");
				} else {
					UIHelper.ToastMessage(getActivity(), "功能升级中...");
				}
			}
		});
	}
	
	@Override
	public void onStart() {
		super.onStart();
		getActivity().setTitle("常用工具");
	}
}
