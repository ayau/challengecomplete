package com.challengecomplete.android.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.challengecomplete.android.R;
import com.challengecomplete.android.utils.ChallengeComplete;
import com.challengecomplete.android.utils.Media;
import com.google.android.imageloader.ImageLoader;


public class SideFragment extends ListFragment{
	private View mView;
	Context context;
	LayoutInflater inflater;
	ImageLoader mImageLoader;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View root = inflater.inflate(R.layout.fragment_side, container, false);
		return root;
	}
	
	@Override
	public void onViewCreated(View v, Bundle savedInstanceState){
		mView = v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		context = getActivity();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// Intantiate imageLoader
		mImageLoader = new ImageLoader();
		
		displayUser();
		
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
	
	public void displayUser(){
		ImageView userAvatar = (ImageView) mView.findViewById(R.id.user_avatar);
		TextView userName = (TextView) mView.findViewById(R.id.user_name);
		TextView userPointsTotal = (TextView) mView.findViewById(R.id.user_points_total);
		TextView userPointsMonth = (TextView) mView.findViewById(R.id.user_points_month);
		
		userName.setText(ChallengeComplete.getUserName(context));
		userPointsTotal.setText(ChallengeComplete.getUserPointsTotal(context) + "");
		userPointsMonth.setText(ChallengeComplete.getUserPointsMonth(context) + "");
		
		String url = ChallengeComplete.getUserAvatar(context);
		
		if (url != null)
			mImageLoader.bind(userAvatar, url, callback);
	}
	
	// Just a temporary way to make the avatar circular
	public final ImageLoader.Callback callback = new ImageLoader.Callback(){
		@Override
		public void onImageLoaded(ImageView view, String url) {
			Bitmap bitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();
			Bitmap newBitmap = Media.getRoundedCornerBitmap(context, bitmap);
			view.setImageBitmap(newBitmap);
		}

		@Override
		public void onImageError(ImageView view, String url, Throwable error) {
			// TODO Auto-generated method stub
			
		}
	};
	
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
