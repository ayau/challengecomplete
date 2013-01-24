package com.challengecomplete.android.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.challengecomplete.android.R;


public class SideFragment extends ListFragment{
	Context context;
	LayoutInflater inflater;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View root = inflater.inflate(R.layout.fragment_side, container, false);
		return root;
	}
	
	@Override
	public void onViewCreated(View v, Bundle savedInstanceState){

	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		context = getActivity();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		ListView list = this.getListView();
		
		ArrayList<MenuItem> menu = new ArrayList<MenuItem>();

		MenuItem new_goal = new MenuItem("New Goal");
		MenuItem current = new MenuItem("Current Goals");
		MenuItem bucket = new MenuItem("In my Bucket");
		MenuItem achievements = new MenuItem("Achievements");
		MenuItem notifications = new MenuItem("Notifications");
		
		menu.add(new_goal);
		menu.add(current);
		menu.add(bucket);
		menu.add(achievements);
		menu.add(notifications);

		MenuListAdapter adapter = new MenuListAdapter(context, R.layout.list_item_menu, menu);
		list.setAdapter(adapter);
	}
	
	public class MenuListAdapter extends ArrayAdapter<MenuItem>{
		ArrayList<MenuItem> menu;
		int resId;
		
		public MenuListAdapter(Context context, int resId, ArrayList<MenuItem> menu) {
			super(context, resId, menu);
			this.menu = menu;
			this.resId = resId;
		}
		
		@Override
		public int getCount(){
			return menu.size();
		}
		
		@Override
		public MenuItem getItem(int pos){
			return menu.get(pos);
		}
		
		@Override
		public long getItemId(int pos){
			return pos;
		}
		
		@Override
		public View getView(int pos, View v, ViewGroup parent){
			TextView title;
			
			if(v == null){
				v = inflater.inflate(resId, parent, false);
				title = (TextView) v.findViewById(R.id.menu_title);
				v.setTag(title);
			}else{
				title = (TextView) v.getTag();
			}
			
			MenuItem item = getItem(pos);
			title.setText(item.title);
			return v;
		}
		
	}
	
	private class MenuItem {
		String title;
		public MenuItem(String title){
			this.title = title;
		}
	}
}
