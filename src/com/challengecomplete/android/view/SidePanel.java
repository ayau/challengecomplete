package com.challengecomplete.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.challengecomplete.android.R;
import com.challengecomplete.android.utils.ChallengeComplete;

public class SidePanel extends ViewGroup{
	
	public SidePanel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		
		width = (int) (width*5/6);
		
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
