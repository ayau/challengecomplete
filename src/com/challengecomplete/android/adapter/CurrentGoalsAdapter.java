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
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.challengecomplete.android.R;
import com.challengecomplete.android.utils.BitmapCache;
import com.challengecomplete.android.utils.SvgLoader;

public class CurrentGoalsAdapter extends CursorAdapter {
	private BitmapCache mBitmapCache;
	private SvgLoader svgLoader;
	private int selected;
	
	public CurrentGoalsAdapter(Context context, Cursor c) {
		super(context, c, 0);
		mBitmapCache = new BitmapCache();
		svgLoader = new SvgLoader(context, this, mBitmapCache);
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
			if (!(Boolean) holder.container.getTag())
				holder.container.setVisibility(View.GONE);
			holder.container_active.setVisibility(View.VISIBLE);
		} else {
			holder.container.setVisibility(View.VISIBLE);
			holder.container_active.setVisibility(View.GONE);
		}
		
		holder.square.setActivated(selected == id);
		holder.square.setTag(id);
	}
	
	@Override
	public View newView(final Context context, Cursor c, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.list_item_goal, parent, false);
		ViewHolder holder = new ViewHolder();
		holder.name = (TextView) v.findViewById(R.id.goal_name);
		holder.badge = (ImageView) v.findViewById(R.id.goal_badge);
		holder.square = (ImageView) v.findViewById(R.id.square);
		holder.container = v.findViewById(R.id.goal_background);
		holder.container_active = v.findViewById(R.id.goal_background_active);
		v.setTag(holder);
		
		final View goalContainer = holder.container;
		final View goalContainerActive = holder.container_active;
		goalContainer.setTag(false);
		
		holder.square.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View square) {;
				if (selected == (Integer) square.getTag()){
					selected = 0;
					square.setActivated(false);
					CurrentGoalsAdapter.this.notifyDataSetChanged();
				} else {
					
					goalContainerActive.setAlpha(0f);
					goalContainerActive.setVisibility(View.VISIBLE);
					goalContainerActive.animate()
				            .alpha(1f)
				            .setDuration(150)
				            .setListener(null);
					
					goalContainer.setTag(true); // is animating
					goalContainer.animate()
		            	.alpha(0f)
		            	.setDuration(150)
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
		
		return v;
	}

	class ViewHolder {
		View container_active;
		View container;
		ImageView badge;
		TextView name;
		ImageView square;
	}
	
}
