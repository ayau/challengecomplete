package com.challengecomplete.android.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.Scroller;

import com.challengecomplete.android.R;
import com.challengecomplete.android.utils.SmoothInterpolator;


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
	private Scroller mScroller; // scroller for smooth scrolling
	
	private GestureDetector mGestureDetector;
	private TranslateAnimation bounceAnimation;
	
	public ScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setOverScrollMode(View.OVER_SCROLL_NEVER);
		setHorizontalFadingEdgeEnabled(false);
		
		mScroller = new Scroller(context, new SmoothInterpolator());
		
//		ViewTreeObserver viewTreeObserver = getViewTreeObserver();
//		viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener(){
//			@Override
//			public void onGlobalLayout() {
//				ScrollView.this.scrollTo(width*5/6, 0);
//			}
//		});
		
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
		
		int shadowWidth = this.getResources().getDimensionPixelSize(R.dimen.sidepanel_shadow_width);
		
		// originally fragment_container
		FrameLayout container = (FrameLayout) this.findViewById(R.id.scrollview_container);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
		lp.setMargins(closedX - shadowWidth, 0, 0, 0);
		container.setLayoutParams(lp);
		container.measure(MeasureSpec.makeMeasureSpec(width + closedX, MeasureSpec.EXACTLY), heightMeasureSpec);
		
//		for(int i = 0; i < fragment_container.getChildCount(); i++){
//			fragment_container.getChildAt(i).measure(MeasureSpec.makeMeasureSpec(width,  MeasureSpec.EXACTLY), heightMeasureSpec);
//		}
		this.findViewById(R.id.fragment_container).measure(MeasureSpec.makeMeasureSpec(width,  MeasureSpec.EXACTLY), heightMeasureSpec);
	}
	
	@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
		// If scrollView is opened and the touch position is inside mainView
        if (isOpened() && ev.getX() + getScrollX() > closedX) return true;
        
        // If can scroll
		if (super.onInterceptTouchEvent(ev)){
			// If scrollView is closed and the touch position is on the right half of the screen
	        if (isClosed() && ev.getX() > width/3) return false;
	        
	        // if gesture is valid (less than 45 degree)
			return mGestureDetector.onTouchEvent(ev);
		}
		
		return false;
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent ev){
		
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			int x = (int) ev.getX();
			if(x + getScrollX() < closedX){
				return false;
			}

			// intercept bounce animation
			if (bounceAnimation != null){
				bounceAnimation.cancel();
				bounceAnimation = null;
			}
			
			if (!mScroller.isFinished()) mScroller.abortAnimation();

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
		mScroller.startScroll(getScrollX(), 0, closedX - getScrollX(), 0, 500);
		post(scrollerTask);
//		smoothScrollTo(closedX, 0);
		((Activity) this.getContext()).getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public void open(){
		mScroller.startScroll(getScrollX(), 0, openedX - getScrollX(), 0, 500);
		post(scrollerTask);
//		smoothScrollTo(openedX, 0);
		((Activity) this.getContext()).getActionBar().setDisplayHomeAsUpEnabled(false);
	}
	
	private Runnable scrollerTask = new Runnable() {
        @Override
        public void run() {
            mScroller.computeScrollOffset();
            scrollTo(mScroller.getCurrX(), 0);

            if (!mScroller.isFinished()) ScrollView.this.post(this);
        }
    };
	
	// Scrolls the fragment container such that container is complete hidden
	// runnable to run after animation is completed
	public void scrollOut(final Runnable runnable){
		Animation animation = new TranslateAnimation(0, width*1/6, 0, 0);
		animation.setDuration(200);
		animation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				if (runnable != null) runnable.run();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationStart(Animation animation) {}
			
		});
		startAnimation(animation);
	}

}
