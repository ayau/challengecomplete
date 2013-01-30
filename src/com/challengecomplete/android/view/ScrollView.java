package com.challengecomplete.android.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.challengecomplete.android.R;


public class ScrollView extends HorizontalScrollView{
	final static private int DIRECTION_RIGHT = 1;
	final static private int DIRECTION_LEFT = -1;
	final static private int DIRECTION_NONE = 0;
	private int width;
	private int height;
	private int direction;
	private int openedX;
	private int closedX;
	private boolean scrolled; // detect single tap event
	
	private GestureDetector mGestureDetector;
	private TranslateAnimation bounceAnimation;
	
	
//	for measuring angle of gesture
	private int oldX;
	private int oldY;
	
	
	public ScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		setOverScrollMode(View.OVER_SCROLL_NEVER);
		setHorizontalFadingEdgeEnabled(false);
		
		ViewTreeObserver viewTreeObserver = getViewTreeObserver();
		viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener(){
			@Override
			public void onGlobalLayout() {
				ScrollView.this.scrollTo(width*5/6, 0);
			}
		});
		
		mGestureDetector = new GestureDetector(context, new XScrollDetector());
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		this.width = width;
		this.height = height;
		openedX = 0;
		closedX = width*5/6;
		
		setMeasuredDimension(width, height);		
		
		FrameLayout fragment_container = (FrameLayout) this.findViewById(R.id.fragment_container);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
		lp.setMargins(closedX, 0, 0, 0);
		fragment_container.setLayoutParams(lp);
		fragment_container.measure(MeasureSpec.makeMeasureSpec(width + closedX, MeasureSpec.EXACTLY), heightMeasureSpec);
		
		for(int i = 0; i < fragment_container.getChildCount(); i++){
			fragment_container.getChildAt(i).measure(MeasureSpec.makeMeasureSpec(width,  MeasureSpec.EXACTLY), heightMeasureSpec);
		}
	}
	
	@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
		// If scrollView is opened and the touch position is inside mainView
        if (isOpened() && ev.getX() + getScrollX() > closedX) return true;
        
        // If scrollView is closed and the touch position is on the right half of the screen
        if (isClosed() && ev.getX() > width/2) return false;
        
		return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent ev){
		if (bounceAnimation != null)
			bounceAnimation.cancel();
		
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			int x = (int) ev.getX();
			if(x + getScrollX() < closedX){
				return false;
			}
			scrolled = false;
			break;
		case MotionEvent.ACTION_UP:
			if(scrolled){
				if(direction == DIRECTION_LEFT){
					close();
				}else if (direction == DIRECTION_RIGHT){
					open();
				}
			}else if(isOpened()){
				close();
			}
			return false;
		}
		return super.onTouchEvent(ev);
	}
	
	@Override
	public void onScrollChanged(int l, int t, int oldl, int oldt){
		if(l > oldl)
			direction = DIRECTION_LEFT;
		else if(l < oldl)
			direction = DIRECTION_RIGHT;
		else if (!scrolled)
			direction = DIRECTION_NONE;
		if(direction != DIRECTION_NONE)
			scrolled = true;
	}
	
	// When the bounce animation ends
	@Override
	protected void onAnimationEnd(){
		super.onAnimationEnd();
		ScrollView.this.scrollTo(openedX, 0);
	}
	
	public void bounce(){
		// Don't bounce if it's not fully opened
		if (getScrollX() != openedX)
			return;
		
		bounceAnimation = new TranslateAnimation(0, 40, 0, 0);
		bounceAnimation.setInterpolator(new BounceInterpolator());
		bounceAnimation.setDuration(800);
	    ScrollView.this.scrollTo(openedX + 40, 0);
	    startAnimation(bounceAnimation);
	    invalidate();
	}
	
	// Return true if we're scrolling in the x direction  
    class XScrollDetector extends SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(Math.abs(distanceY) < Math.abs(distanceX)) {
                return true;
            }
            return false;
        }
    }
	
	public boolean isClosed(){
		return getScrollX() == closedX;
	}
	
	public boolean isOpened(){
		return getScrollX() < closedX;
	}
	
	public void close(){
		smoothScrollTo(closedX, 0);
		((Activity) this.getContext()).getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public void open(){
		smoothScrollTo(openedX, 0);
		((Activity) this.getContext()).getActionBar().setDisplayHomeAsUpEnabled(false);
	}
}
