package com.challengecomplete.android.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.challengecomplete.android.models.badges.BadgeContentProvider;
import com.challengecomplete.android.models.badges.BadgeProcessor;
import com.challengecomplete.android.service.HttpCaller;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

public class SvgLoader {
	private final static int FADE_DURATION = 500;
	
//    public static final int DEFAULT_TASK_LIMIT = 3;
	private BitmapCache mBitmapCache;  
	private HashMap<Integer, BitmapWorkerTask> workers;
	private HashMap<Integer, ImageView> imageViews;
	private BaseAdapter adapter;
	private Context context;
	
	public SvgLoader(Context context, BaseAdapter adapter, BitmapCache mBitmapCache) {
		this.mBitmapCache = mBitmapCache;
		workers = new HashMap<Integer, BitmapWorkerTask>();
		imageViews = new HashMap<Integer, ImageView>();
		this.adapter = adapter;
		this.context = context;
	}
	
	public void getSvg(ImageView imageView, String svgString, int id, String badge, String fgColor, String bgColor){
		imageViews.put(id, imageView);
		if (!workers.containsKey(id)){
			BitmapWorkerTask task = new BitmapWorkerTask(svgString, id, badge, fgColor, bgColor);
	        workers.put(id, task);
			task.execute(id);
		}
	}

	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		private String badge;
	    private String fgColor;
	    private String bgColor;
	    private int id;
	    private String svgString;
	    
		public BitmapWorkerTask(String svgString, int id, String badge,
				String fgColor, String bgColor) {
			this.id = id;
			this.badge = badge;
			this.fgColor = fgColor;
			this.bgColor = bgColor;
			this.svgString = svgString;
		}

		// Decode image in background.
	    @Override
	    protected Bitmap doInBackground(Integer... params) {
	    	
	    	if (svgString == null){
	    		svgString = HttpCaller.getRequest(context, "/svg/" + badge + ".svg");
	    	
	    		if (svgString == null) return null;
	    	
		    	// Save svg to database
		    	ContentValues contentValues = BadgeProcessor.createContentValues(badge, svgString);
		    	context.getContentResolver().insert(BadgeContentProvider.CONTENT_URI, contentValues);
	    	}
	    	
	    	return processSvg(id, svgString, fgColor, bgColor);
	    }
	    
	    @Override
	    protected void onPostExecute(Bitmap result) {
	    	workers.remove(id);
	    	if (result != null)
	    		onImageLoaded(id, imageViews.get(id), result);
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
	
	public static void fadeIn(ImageView imageView){
		AlphaAnimation fadeImage = new AlphaAnimation(0, 1);
		fadeImage.setDuration(FADE_DURATION);
		fadeImage.setInterpolator(new DecelerateInterpolator());
		imageView.startAnimation(fadeImage);
	}
	
	public void onImageLoaded(int id, ImageView imageView, Bitmap bitmap){
		if ((Integer) imageView.getTag() == id){
			imageView.setImageBitmap(bitmap);
	    	fadeIn(imageView);
	    	adapter.notifyDataSetChanged();
		}
	}
	
    
}
