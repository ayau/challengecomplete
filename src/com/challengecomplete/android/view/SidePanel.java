package com.challengecomplete.android.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class SidePanel extends ViewGroup{
	private int width;
	private int height;
	
	public SidePanel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
//	@Override
//	public void onDraw(Canvas canvas){
//		Shader shader = new LinearGradient(width-5, 0, width, 0, Color.TRANSPARENT, Color.parseColor("#44000000"), TileMode.CLAMP); 
//		Paint paint = new Paint(); 
//		paint.setShader(shader); 
//		canvas.drawRect(new RectF(width - 5, 0, width, height), paint); 
//	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		height = MeasureSpec.getSize(heightMeasureSpec);
		width = (int) MeasureSpec.getSize(widthMeasureSpec) * 5/6;
		
		setMeasuredDimension(width, height);

		for(int i = 0; i < getChildCount(); i++){
			getChildAt(i).measure(
					MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childTop = 0;
		
		for(int i = 0; i < getChildCount(); i++){
			View child = getChildAt(i);
			
			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();
			
			if (i == getChildCount() - 1) child.layout(0, childTop, childWidth, b);
			else child.layout(0, childTop, childWidth, childTop + childHeight);
			
			childTop += childHeight;
		}

	}
}
