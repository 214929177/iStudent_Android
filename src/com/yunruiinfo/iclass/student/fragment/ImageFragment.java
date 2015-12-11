package com.yunruiinfo.iclass.student.fragment;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.yunruiinfo.iclass.student.R;
import com.yunruiinfo.iclass.student.bean.Icon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends BaseFragment {
	@ViewInject(R.id.imageview)
	ImageView mImageView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_image, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mImageView = (ImageView) findViewById(R.id.imageview);
		Icon icon = (Icon) getArguments().getSerializable("icon");
		mImageView.setImageResource((Integer) icon.object);
		
		getActivity().setTitle(icon.name);
	}
}
