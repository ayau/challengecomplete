package com.challengecomplete.android.adapter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.challengecomplete.android.R;
import com.challengecomplete.android.utils.Media;
import com.google.android.imageloader.ImageLoader;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

public class CurrentGoalsAdapter extends CursorAdapter {

	public CurrentGoalsAdapter(Context context, Cursor c) {
		super(context, c, 0);
	}

	@Override
	public void bindView(View view, Context context, Cursor c) {
//		 int id = c.getInt(0);
		String name = c.getString(1);
		String badge = c.getString(8);
		String color = c.getString(9);
		 
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.name.setText(name);
		String p = "<svg version=\"1.0\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" width=\"100px\" height=\"95.105px\" viewBox=\"0 0 100 95.105\" enable-background=\"new 0 0 100 95.105\" xml:space=\"preserve\"><polygon points=\"50,0 61.803,36.327 100,36.327 69.098,58.778 80.902,95.105 50,72.654 19.098,95.105 30.902,58.778 0,36.327 38.197,36.327 \"></polygon></svg>";
		InputStream is = new ByteArrayInputStream(p.getBytes());
		SVG svg = SVGParser.getSVGFromInputStream(is);
//		svg.createPictureDrawable()
//		holder.badge.setBackgroundColor(Color.WHITE);
//		holder.badge.setImageDrawable(svg.createPictureDrawable());
		 
//		Bitmap bitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();
		Bitmap newBitmap = Media.getBadgeBitmap(context, svg.createPictureDrawable(), color);
		holder.badge.setImageBitmap(newBitmap);
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		 LayoutInflater inflater = LayoutInflater.from(context);
		 View v = inflater.inflate(R.layout.list_item_goal, parent, false);
		 ViewHolder holder = new ViewHolder();
		 holder.name = (TextView) v.findViewById(R.id.goal_name);
		 holder.badge = (ImageView) v.findViewById(R.id.goal_badge);
		 v.setTag(holder);
		 bindView(v, context, c);
		 return v;
	}

	class ViewHolder {
		ImageView badge;
		TextView name;
	}

}
