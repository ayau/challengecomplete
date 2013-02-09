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
import com.challengecomplete.android.utils.BitmapCache;
import com.challengecomplete.android.utils.SvgLoader;

public class CurrentGoalsAdapter extends CursorAdapter {
	private BitmapCache mBitmapCache;
	private SvgLoader svgLoader;
	
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
		
		holder.square.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Toast toast = Toast.makeText(v.getContext(), "square selected", Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}
	
	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		Log.i("NEW VIEW", "NEW");
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.list_item_goal, parent, false);
		ViewHolder holder = new ViewHolder();
		holder.name = (TextView) v.findViewById(R.id.goal_name);
		holder.badge = (ImageView) v.findViewById(R.id.goal_badge);
		holder.square = (ImageView) v.findViewById(R.id.square);
		v.setTag(holder);
//		bindView(v, context, c);
		return v;
	}

	class ViewHolder {
		ImageView badge;
		TextView name;
		ImageView square;
	}
	
}
