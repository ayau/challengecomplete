package com.challengecomplete.android.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.BaseAdapter;

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
	    	String svgString = HttpCaller.getRequest(context, badge);
	    	
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
	    	adapter.notifyDataSetChanged();
	     }
	}

    
}
