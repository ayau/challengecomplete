package com.challengecomplete.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class SidePanel extends ViewGroup{

	public SidePanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		
		setMeasuredDimension(width, height);

		for(int i = 0; i < getChildCount(); i++){
			getChildAt(i).measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), heightMeasureSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for(int i = 0; i < getChildCount(); i++){
			getChildAt(i).layout(l, t, r, b);
		}
		
	}

}
