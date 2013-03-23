package com.challengecomplete.android.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;

import com.challengecomplete.android.R;
import com.challengecomplete.android.adapter.CurrentGoalsAdapter;
import com.challengecomplete.android.models.goals.Goal;
import com.challengecomplete.android.models.goals.GoalContentProvider;
import com.challengecomplete.android.models.goals.GoalTable;
import com.challengecomplete.android.utils.Media;
import com.challengecomplete.android.utils.SmoothInterpolator;

public class GoalFragment extends ListFragment implements LoaderCallbacks<Cursor>{
	private Goal goal; // id of goal
	private final static int LOADER_ID = 0x02;
	private CurrentGoalsAdapter mAdapter;
	private View mHeader;
	private int mLastMotionY;
	private int mHeaderHeight;
	private ImageView mCoverImage;
	
	public GoalFragment(int id){
		goal = new Goal(id);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mHeader = inflater.inflate(R.layout.list_header_goal, null, true);
		getListView().addHeaderView(mHeader, null, false);
		mHeaderHeight = 300;
		
		mAdapter = new CurrentGoalsAdapter(getActivity(), null);  
		setListAdapter(mAdapter);
		getLoaderManager().initLoader(LOADER_ID, null, this);
		
		getListView().setOnTouchListener(new OnTouchListener(){
			 
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int y = (int) event.getY();

			    switch (event.getAction()) {
			    	case MotionEvent.ACTION_UP:
						resetHeaderPadding();
						break;
		            case MotionEvent.ACTION_DOWN:
		                mLastMotionY = y;
		                break;
		            case MotionEvent.ACTION_MOVE:
		                applyHeaderPadding(event);
		                break;
			    }
				return false;
			}
		
		});
		
		getListView().setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScroll(AbsListView lv, int firstVisibleItem, int arg2, int arg3) {
				if (firstVisibleItem > 0)
					mCoverImage.setVisibility(View.GONE);
				else
					mCoverImage.setVisibility(View.VISIBLE);
				
			}

			@Override
			public void onScrollStateChanged(AbsListView lv, int state) {
			}
			
		});
		
		// turn off fading edge
		getListView().setOverScrollMode(View.OVER_SCROLL_NEVER);
		
		// populate goal
		goal.populate(getActivity());
		
//		mCoverImage.setTranslationY(-1 * (mCoverImage.getMeasuredHeight() - mHeaderHeight)/2);
		
		ImageView mBadge = (ImageView) mHeader.findViewById(R.id.goal_badge);
		
		mBadge.setImageBitmap(Media.tempProcessSvg(getActivity(), goal.getSvg(), goal.getFgColor(), goal.getBgColor()));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_goal, container, false);
		mCoverImage = (ImageView) root.findViewById(R.id.image);
		return root;
	}
	

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle extras) {
		// TODO
		// Handle this in ContentProvider instead?
		return new CursorLoader(getActivity(), GoalContentProvider.CONTENT_URI_CURRENT_WITH_BADGES,
				GoalTable.allColumnsWithBadge, null, null, null); 
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
		mAdapter.swapCursor(c);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}
	
	
	private void applyHeaderPadding(MotionEvent ev) {
		// clever hack :D
		if (getListView().getFirstVisiblePosition() > 0 || getListView().getChildAt(0).getTop() != 0) return;
		
		int topPadding = (int) ((ev.getY() - mLastMotionY) / 5);
		
		if (topPadding < 0) topPadding = 0;
		
		mHeader.setPadding(
		        mHeader.getPaddingLeft(),
		        topPadding,
		        mHeader.getPaddingRight(),
		        mHeader.getPaddingBottom());
		
		mCoverImage.setTranslationY(-1 * (mCoverImage.getHeight() - (mHeaderHeight + topPadding))/2);

	}
	
	 private void resetHeaderPadding() {
		 if (mHeader.getPaddingTop() == 0) return;
		 
		 BounceAnimation a = new BounceAnimation();
		 a.setInterpolator(new SmoothInterpolator());
		 a.setDuration(500);
		 mHeader.startAnimation(a);
	 }
	 
	 private class BounceAnimation extends Animation {
		 private int startPadding;
		 
		 public BounceAnimation(){
			 startPadding = mHeader.getPaddingTop();
		 }
		 
		 @Override
		 protected void applyTransformation(float interpolatedTime, Transformation t) {
			 
			 int padding = (int) (startPadding*(1 - interpolatedTime));
			 
			 mHeader.setPadding(
		        		mHeader.getPaddingLeft(),
		                padding,
		                mHeader.getPaddingRight(),
		                mHeader.getPaddingBottom());
			 
			 mCoverImage.setTranslationY(-1 * (mCoverImage.getHeight() - (mHeaderHeight + padding))/2);	 
	     }
		 
		 @Override
		 public boolean willChangeBounds(){
			 return true;
		 }
	 }
	 
    
}
