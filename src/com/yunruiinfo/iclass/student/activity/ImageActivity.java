package com.yunruiinfo.iclass.student.activity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yunruiinfo.iclass.student.R;

import android.os.Bundle;
import android.widget.ImageView;

public class ImageActivity extends BaseActivity{
	@ViewInject(R.id.imageview)	private ImageView mImageView;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		ViewUtils.inject(this);	
		int resId = getIntent().getIntExtra("image", 0);
		mImageView.setImageResource(resId);		
		String title = getIntent().getStringExtra("title");
		setTitle(title);
	}
}
