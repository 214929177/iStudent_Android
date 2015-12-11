package com.yunruiinfo.iclass.student.view;

import com.yunruiinfo.iclass.student.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

/**
 * Android自带GridView扩展
 * 目前实现功能：
 * 1.子视图之间的分割线（配置属性：divider：分割线颜色，dividerWidth：分割线宽度）
 * 2.根据内容设定高度，适合在嵌套在ScrollView中时使用（配置属性：mostHeight:是否启用，默认不启用）（另外需要设置layout_height="wrap_content"）
 * @author SYZ
 */
public class ExpandGridView extends GridView {
	private Paint mPaint = new Paint();
	private int mDividerColor;
	private float mDividerWidth;
	private int mNumColumn;
	private int mColumnWidth;
	private boolean isMostHeight;
	public ExpandGridView(Context context) {
		super(context);
	}
	public ExpandGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttrs(context, attrs);
	}
	public ExpandGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAttrs(context, attrs);
	}
	/**
	 * 获取自定义属性
	 * @param context
	 * @param attrs
	 */
	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpandGridView);
		mDividerColor = a.getColor(R.styleable.ExpandGridView_divider, 0);
		mDividerWidth = a.getDimension(R.styleable.ExpandGridView_dividerWidth, 1);
		isMostHeight = a.getBoolean(R.styleable.ExpandGridView_mostHeight, false);
		a.recycle();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int heightSpec;
        if (isMostHeight && getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            // The great Android "hackatlon", the love, the magic.
            // The two leftmost bits in the height measure spec have
            // a special meaning, hence we can't use them to describe height.
            heightSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        } else {
            // Any other height should be respected as is.
            heightSpec = heightMeasureSpec;
        }
        super.onMeasure(widthMeasureSpec, heightSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//绘制分割线
		this.drawDivider(canvas);
	}
	
	/**
	 * 绘制分割线
	 * @param canvas
	 */
	private void drawDivider(Canvas canvas) {
		if (mDividerColor == 0) {
			return;//未指定分割线颜色
		}
		//setVerticalSpacing((int) mDividerWidth);		//设置间距后子View按下效果有间距？
		//setHorizontalSpacing((int) mDividerWidth);
		mPaint.setColor(mDividerColor);
		mPaint.setStrokeWidth(mDividerWidth);
		mPaint.setAntiAlias(false);	//禁用抗锯齿
		
		final int width = getMeasuredWidth();
        final int first = getFirstVisiblePosition();
        final int last = getLastVisiblePosition();
        final int count = last - first + 1;
		
		if (mNumColumn <= 0 && mColumnWidth > 0) {//指定列数
			mNumColumn = width / mColumnWidth;
		}
		final int rowCount = rowsOf(count, mNumColumn); 
		for (int i = 0; i < count; i++) {
			View view = getChildAt(first + i);
			if (view == null)
				return;
			int w = width / mNumColumn;//view.getWidth(); //取平均宽度而不是子View宽度
			int h = view.getHeight();		//子View高度
			int top = view.getTop();		//距离父容器顶部间距
			int rowIndex = i / mNumColumn;	//当前行索引，从0开始
			int colIndex = i % mNumColumn;	//当前列索引，从0开始
			if (rowIndex < rowCount) {		//绘制水平分割线
				canvas.drawLine(w * colIndex, top + h, w * colIndex + w, top + h, mPaint);
			}
			if (colIndex + 1 < mNumColumn) {//绘制垂直分割线，最后一列不绘制
				canvas.drawLine(w * colIndex + w, top, w * colIndex + w, top + h, mPaint);
			}
		}
	}
	
	/**
	 * 已知总数量和列数求行数
	 * @param size 总数量
	 * @param columns 列数
	 * @return 计算得到的行数
	 */
	public static int rowsOf(int size, int columns) {
		if (size < 1 || columns < 1)
			return 0;
		// 整除
		boolean isDivisible = (size % columns) == 0;
		if (isDivisible) {
			return size / columns;
		} else {
			return size / columns + 1;
		}
	}
	
	@Override
	public void setNumColumns(int numColumns) {
		super.setNumColumns(numColumns);
		this.mNumColumn = numColumns;
	}
	
	@Override
	public int getNumColumns() {
		return this.mNumColumn;
	}
	
	@Override
	public void setColumnWidth(int columnWidth) {
		super.setColumnWidth(columnWidth);
		this.mColumnWidth = columnWidth;
	}
	
	@Override
	public int getColumnWidth() {
		return this.mColumnWidth;
	}
}
