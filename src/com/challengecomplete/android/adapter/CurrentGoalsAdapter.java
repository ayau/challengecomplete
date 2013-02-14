package com.challengecomplete.android.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import com.challengecomplete.android.R;
import com.challengecomplete.android.models.goals.GoalProcessor;
import com.challengecomplete.android.utils.BitmapCache;
import com.challengecomplete.android.utils.SvgLoader;

public class CurrentGoalsAdapter extends CursorAdapter {
	private BitmapCache mBitmapCache;
	private SvgLoader svgLoader;
	private int selected;
	private View selectedView;
	private Cursor cursor;
	
	public CurrentGoalsAdapter(Context context, Cursor c) {
		super(context, c, 0);
		cursor = c;
		mBitmapCache = new BitmapCache();
		svgLoader = new SvgLoader(context, this, mBitmapCache);
	}
	
	@Override
	public Cursor swapCursor(Cursor newCursor){
		super.swapCursor(newCursor);
		cursor = newCursor;
		return cursor;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor c) {
		int id = c.getInt(0);
		String name = c.getString(1);
		String badge = c.getString(8);
		String color = c.getString(9);
		 
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.name.setText(name);
		holder.badge.setTag(id);
		
		// Check cache first
		Bitmap bitmap = mBitmapCache.getBitmapFromMemCache(id + "");
//		Log.i("HERE", id + "");
		if (bitmap == null) {
			holder.badge.setImageResource(R.drawable.ic_launcher);
			svgLoader.getSvg(id, badge, color);
		} else {
			holder.badge.setImageBitmap(bitmap);
		}
		
		if (selected == id){
			if (!(Boolean) holder.container.getTag()){
				holder.container.setVisibility(View.GONE);
				holder.container_active.setVisibility(View.VISIBLE);
			}
		} else {
			if (!(Boolean) holder.container.getTag()){
				holder.container.setVisibility(View.VISIBLE);
				holder.container_active.setVisibility(View.GONE);
			}
		}
		
		holder.square.setActivated(selected == id);
		holder.square.setTag(id);
	}
	
	@Override
	public View newView(final Context context, Cursor c, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		final View v = inflater.inflate(R.layout.list_item_goal, parent, false);
		ViewHolder holder = new ViewHolder();
		holder.name = (TextView) v.findViewById(R.id.goal_name);
		holder.badge = (ImageView) v.findViewById(R.id.goal_badge);
		holder.square = (ImageView) v.findViewById(R.id.square);
		holder.container = v.findViewById(R.id.goal_background);
		holder.container_active = v.findViewById(R.id.goal_background_active);
		holder.giveup = v.findViewById(R.id.goal_giveup);
		v.setTag(holder);
		
		final View goalContainer = holder.container;
		final View goalContainerActive = holder.container_active;
		goalContainer.setTag(false);
		final View badge = holder.badge;
		
		// TODO
		// refactor onClickListeners
		holder.square.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View square) {
				Log.i("HEIGHT", v.getHeight()+"");
				if (selected == (Integer) square.getTag()){
					selected = 0;
					square.setActivated(false);
					CurrentGoalsAdapter.this.notifyDataSetChanged();
				} else {
					selectedView = v;
					goalContainerActive.setAlpha(0f);
					goalContainerActive.setVisibility(View.VISIBLE);
					goalContainerActive.animate()
				            .alpha(1f)
				            .setDuration(200)
				            .setListener(null);
					
					goalContainer.setTag(true); // is animating
					goalContainer.animate()
		            	.alpha(0f)
		            	.setDuration(200)
		            	.setListener(new AnimatorListenerAdapter() {
			                @Override
			                public void onAnimationEnd(Animator animation) {
			                	goalContainer.setVisibility(View.GONE);
			                	goalContainer.setAlpha(1f);
			                	goalContainer.setTag(false);
			                }
			            });
					
					selected = (Integer) square.getTag();
					CurrentGoalsAdapter.this.notifyDataSetChanged();
				}
			}
		});
		
		holder.giveup.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View giveup) {
				Animation fold = new FoldUpAnimation(v);
				fold.setAnimationListener(new AnimationListener(){

					@Override
					public void onAnimationEnd(Animation animation) {
						int id = (Integer) badge.getTag();
						GoalProcessor.delete(context, id);
			            cursor.requery();
			            notifyDataSetChanged();
			            goalContainerActive.getLayoutParams().height = goalContainer.getHeight();
			            v.getLayoutParams().height = goalContainer.getHeight();
			            v.requestLayout();
			            selectedView = null;
						selected = 0;
					}

					@Override
					public void onAnimationRepeat(Animation animation) {}

					@Override
					public void onAnimationStart(Animation animation) {}
					
				});
				fold.setDuration(500);
				v.startAnimation(fold); 
			}
			
		});
		
		return v;
	}
	
	class ViewHolder {
		View container_active;
		View container;
		ImageView badge;
		TextView name;
		ImageView square;
		View giveup;
	}
	
	public void deselect(){
		// TODO
		// Refactor selectedView
		if (selectedView == null) return;
		
		ViewHolder holder = (ViewHolder) selectedView.getTag();
		View goalContainer = holder.container;
		final View goalContainerActive = holder.container_active;
		holder.square.setActivated(false);
		
		goalContainer.setAlpha(0f);
		goalContainer.setVisibility(View.VISIBLE);
		goalContainer.animate()
	            .alpha(1f)
	            .setDuration(400)
	            .setListener(null);
		
		goalContainerActive.setTag(true); // is animating
		goalContainerActive.animate()
        	.alpha(0f)
        	.setDuration(400)
        	.setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                	goalContainerActive.setVisibility(View.GONE);
                	goalContainerActive.setAlpha(1f);
                	goalContainerActive.setTag(false);
                }
            });
		selectedView = null;
		selected = 0;
	}

	// TODO
	// refactor animation
	public class FoldUpAnimation extends Animation {

		int mFromHeight;
		View mView;

		public FoldUpAnimation(View view) {
		    this.mView = view;
		    this.mFromHeight = view.getHeight();
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
		    int newHeight;
		    newHeight = (int) (mFromHeight * (1 - interpolatedTime));
		    if (newHeight < 0) newHeight = 0;
		    mView.getLayoutParams().height = newHeight;
		    mView.requestLayout();
		    if (newHeight == 0) this.cancel();
		}

		@Override
		public void initialize(int width, int height, int parentWidth, int parentHeight) {
		    super.initialize(width, height, parentWidth, parentHeight);
		}

		@Override
		public boolean willChangeBounds() {
		    return true;
		}
	}
	
}
