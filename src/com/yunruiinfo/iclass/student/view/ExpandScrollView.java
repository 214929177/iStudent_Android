package com.yunruiinfo.iclass.student.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ExpandScrollView extends ScrollView {

	public ExpandScrollView(Context context) {
		super(context);
	}
	public ExpandScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public ExpandScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	//解决和ScrollView内的横向滑动View的冲突
	//如果横着滑动的距离大于竖着滑动的距离，就返回false也就是不拦截当前事件，传递给下一层执行
	private float xDistance;
	private float yDistance;
	private float xLast;
	private float yLast;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0.0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();
			
			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			
			if(xDistance > yDistance)
				return false;
			
			break;
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

}
