package com.challengecomplete.android.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.challengecomplete.android.R;

public class CurrentGoalsAdapter extends CursorAdapter {

	public CurrentGoalsAdapter(Context context, Cursor c) {
		super(context, c, 0);
	}

	@Override
	public void bindView(View view, Context context, Cursor c) {
//		 int id = c.getInt(0);
		 String name = c.getString(1);
		
		 ViewHolder holder = (ViewHolder) view.getTag();
		 holder.name.setText(name);

	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		 LayoutInflater inflater = LayoutInflater.from(context);
		 View v = inflater.inflate(R.layout.list_item_goal, parent, false);
		 ViewHolder holder = new ViewHolder();
		 holder.name = (TextView) v.findViewById(R.id.goal_name);
		 v.setTag(holder);
		 bindView(v, context, c);
		 return v;
	}

	class ViewHolder {
		TextView name;
	}

}
