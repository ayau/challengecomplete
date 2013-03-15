package com.challengecomplete.android.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.BaseAdapter;

import com.challengecomplete.android.models.badges.BadgeContentProvider;
import com.challengecomplete.android.models.badges.BadgeProcessor;
import com.challengecomplete.android.models.badges.BadgeTable;
import com.challengecomplete.android.service.HttpCaller;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

public class SvgLoader {
	
//    public static final int DEFAULT_TASK_LIMIT = 3;
	private BitmapCache mBitmapCache;  
	private HashMap<Integer, BitmapWorkerTask> workers;
	private BaseAdapter adapter;
	private Context context;
	
	public SvgLoader(Context context, BaseAdapter adapter, BitmapCache mBitmapCache) {
		this.mBitmapCache = mBitmapCache;
		workers = new HashMap<Integer, BitmapWorkerTask>();
		this.adapter = adapter;
		this.context = context;
	}
	
	public void getSvg(int id, String badge, String color){
		if (!workers.containsKey(id)){
			BitmapWorkerTask task = new BitmapWorkerTask(id, badge, color);
	        workers.put(id, task);
			task.execute(id);
		}
	}

	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
	    private String badge;
	    private String color;
	    private int id;
	    
		public BitmapWorkerTask(int id, String badge, String color) {
			this.id = id;
			this.badge = badge;
			this.color = color;
		}

		// Decode image in background.
	    @Override
	    protected Bitmap doInBackground(Integer... params) {

	    	// Switch a better way to get the id - /svg/guitar.svg?color=#something
	    	String badgeName = badge.substring(5, badge.length()-17);
	    	
	    	String svgString;
	    	
	    	// Temporary solution - long term should link id of badge to goal so they can be fetched together in
	    	// GoalContentProvider
	    	String[] projection = {BadgeTable.COLUMN_NAME, BadgeTable.COLUMN_SVG};
	    	String selection = BadgeTable.COLUMN_NAME + " = ?"; // LIMIT 1
	    	String[] selectionArgs = {badgeName};
	    	Cursor c = context.getContentResolver().query(BadgeContentProvider.CONTENT_URI, projection, selection, selectionArgs, null);
	    	
	    	if (c.getCount() > 0){
		    	c.moveToFirst();
		    	svgString = c.getString(1);
	    	} else {
	    		svgString = HttpCaller.getRequest(context, badge);
		    	if (svgString == null)
		    		return null;
		    	// Save svg to database
		    	ContentValues contentValues = BadgeProcessor.createContentValues(badgeName, svgString);
		    	context.getContentResolver().insert(BadgeContentProvider.CONTENT_URI, contentValues);
	    	}
	    	
	    	c.close();
	    	
	    	String svgColor = badge.substring(badge.length() - 6);
	    	
	    	svgString = svgString.replaceAll("fill=\"#000000\"", "");
	    	svgString = svgString.replaceAll("<polygon", "<polygon fill=\"#" + svgColor + "\"");
	    	svgString = svgString.replaceAll("<path", "<path fill=\"#" + svgColor + "\"");
	    	svgString = svgString.replaceAll("<rect", "<rect fill=\"#" + svgColor + "\"");
	    	
	    	InputStream is = new ByteArrayInputStream(svgString.getBytes());
			SVG svg = SVGParser.getSVGFromInputStream(is);
			Bitmap bitmap = Media.getBadgeBitmap(context, svg.createPictureDrawable(), color);
			mBitmapCache.addBitmapToMemoryCache(params[0] + "", bitmap);
	    	
			return bitmap;
	    }
	    
	    @Override
	    protected void onPostExecute(Bitmap result) {
	    	workers.remove(id);
	    	if (result != null)
	    		adapter.notifyDataSetChanged();
	     }
	}

    
}
