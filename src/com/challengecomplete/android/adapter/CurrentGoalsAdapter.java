package com.challengecomplete.android.adapter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import com.challengecomplete.android.service.HttpCaller;
import com.challengecomplete.android.utils.BitmapCache;
import com.challengecomplete.android.utils.Media;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

public class CurrentGoalsAdapter extends CursorAdapter {
	private BitmapCache mBitmapCache;
	
	public CurrentGoalsAdapter(Context context, Cursor c) {
		super(context, c, 0);
		mBitmapCache = new BitmapCache();
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
		
		if (name.equals("guitar"))
		Log.i("BADGE", badge);
		
		// Check cache first
		Bitmap bitmap = mBitmapCache.getBitmapFromMemCache(id + "");
		
		if (bitmap == null){
			holder.badge.setImageResource(R.drawable.ic_launcher);
			BitmapWorkerTask task = new BitmapWorkerTask(id, holder.badge, badge, color);
	        task.execute(id);
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
	
	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
	    private ImageView imageView;
	    private String badge;
	    private String color;
	    private int id;
	    
		public BitmapWorkerTask(int id, ImageView imageView, String badge, String color) {
			this.id = id;
			this.imageView = imageView;
			this.badge = badge;
			this.color = color;
		}

		// Decode image in background.
	    @Override
	    protected Bitmap doInBackground(Integer... params) {
//	    	Log.i("BADGE IS", badge);
	    	String svgString = HttpCaller.getRequest(imageView.getContext(), badge);
	    	
	    	String svgColor = badge.substring(badge.length() - 6);
	    	
	    	svgString = svgString.replaceAll("fill=\"#000000\"", "");
	    	svgString = svgString.replaceAll("<polygon", "<polygon fill=\"#" + svgColor + "\"");
	    	svgString = svgString.replaceAll("<path", "<path fill=\"#" + svgColor + "\"");
	    	svgString = svgString.replaceAll("<rect", "<rect fill=\"#" + svgColor + "\"");
	    	
	    	InputStream is = new ByteArrayInputStream(svgString.getBytes());
			SVG svg = SVGParser.getSVGFromInputStream(is);
			Bitmap bitmap = Media.getBadgeBitmap(imageView.getContext(), svg.createPictureDrawable(), color);
			mBitmapCache.addBitmapToMemoryCache(params[0] + "", bitmap);
	    	
			return bitmap;
	    }
	    
	    @Override
	    protected void onPostExecute(Bitmap result) {
	    	if (((Integer) imageView.getTag()) == id)
	    		imageView.setImageBitmap(result);
	     }
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
		 bindView(v, context, c);
		 return v;
	}

	class ViewHolder {
		ImageView badge;
		TextView name;
		ImageView square;
	}
	
}
