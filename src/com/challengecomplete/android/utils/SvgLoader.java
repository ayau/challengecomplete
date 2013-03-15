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
	
	public void getSvg(int id, String badge, String fgColor, String bgColor){
		if (!workers.containsKey(id)){
			BitmapWorkerTask task = new BitmapWorkerTask(id, badge, fgColor, bgColor);
	        workers.put(id, task);
			task.execute(id);
		}
	}

	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
	    private String badge;
	    private String fgColor;
	    private String bgColor;
	    private int id;
	    
		public BitmapWorkerTask(int id, String badge, String fgColor, String bgColor) {
			this.id = id;
			this.badge = badge;
			this.fgColor = fgColor;
			this.bgColor = bgColor;
		}

		// Decode image in background.
	    @Override
	    protected Bitmap doInBackground(Integer... params) {

    		String svgString = HttpCaller.getRequest(context, "/svg/" + badge + ".svg");
	    	if (svgString == null)
	    		return null;
	    	
	    	// Save svg to database
	    	ContentValues contentValues = BadgeProcessor.createContentValues(badge, svgString);
	    	context.getContentResolver().insert(BadgeContentProvider.CONTENT_URI, contentValues);

	    	return processSvg(id, svgString, fgColor, bgColor);
	    }
	    
	    @Override
	    protected void onPostExecute(Bitmap result) {
	    	workers.remove(id);
	    	if (result != null)
	    		adapter.notifyDataSetChanged();
	     }
	}

	public Bitmap processSvg(int id, String svgString, String fgColor, String bgColor){
		svgString = svgString.replaceAll("fill=\"#000000\"", "");
    	svgString = svgString.replaceAll("<polygon", "<polygon fill=\"#" + fgColor + "\"");
    	svgString = svgString.replaceAll("<path", "<path fill=\"#" + fgColor + "\"");
    	svgString = svgString.replaceAll("<rect", "<rect fill=\"#" + fgColor + "\"");
    	
    	InputStream is = new ByteArrayInputStream(svgString.getBytes());
		SVG svg = SVGParser.getSVGFromInputStream(is);
		Bitmap bitmap = Media.getBadgeBitmap(context, svg.createPictureDrawable(), bgColor);
		mBitmapCache.addBitmapToMemoryCache(id + "", bitmap);
		
		return bitmap;
	}
    
}
