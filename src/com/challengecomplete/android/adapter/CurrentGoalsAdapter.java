package com.challengecomplete.android.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.challengecomplete.android.R;
import com.challengecomplete.android.fragment.CurrentGoalsFragment;
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
		if (bitmap == null){
			holder.badge.setImageResource(R.drawable.ic_launcher);
			svgLoader.getSvg(id, badge, color);
		} else {
			holder.badge.setImageBitmap(bitmap);
		}
		
		if (selected == id){
			if (view.getBackground() == null){
				int bottom = view.getPaddingBottom();
			    int top = view.getPaddingTop();
			    int right = view.getPaddingRight();
			    int left = view.getPaddingLeft();
				view.setBackgroundResource(R.drawable.goalselected);
				view.setPadding(left, top, right, bottom);
				view.layout(0, 0, view.getRight(), view.getHeight());
			}
		} else {
			view.setBackgroundResource(0);
		}
		
		holder.square.setActivated(selected == id);
		holder.square.setTag(id);
		
	}
	
	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.list_item_goal, parent, false);
		ViewHolder holder = new ViewHolder();
		holder.name = (TextView) v.findViewById(R.id.goal_name);
		holder.badge = (ImageView) v.findViewById(R.id.goal_badge);
		holder.square = (ImageView) v.findViewById(R.id.square);
		v.setTag(holder);
		
		holder.square.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {;
				if (selected == (Integer) v.getTag()){
					selected = 0;
					v.setActivated(false);
					CurrentGoalsAdapter.this.notifyDataSetChanged();
				} else{
					selected = (Integer) v.getTag();
					CurrentGoalsAdapter.this.notifyDataSetChanged();
				}
			}
		});
		
		return v;
	}

	class ViewHolder {
		ImageView badge;
		TextView name;
		ImageView square;
	}
	
}
