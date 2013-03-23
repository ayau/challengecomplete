package com.challengecomplete.android.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.challengecomplete.android.R;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.PictureDrawable;
import android.util.Log;

public class Media {

	public static Bitmap getRoundedCornerBitmap(Context context, Bitmap bitmap) {
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);
	    
	    final int borderPx = (int) pxFromDp(context, 10);

	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    final RectF rectF = new RectF(rect);
	    final float roundPx = pxFromDp(context, 80);

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	    
	    // draw border
	    paint.setColor(Color.WHITE);
	    paint.setStyle(Paint.Style.STROKE);
	    paint.setStrokeWidth((float) borderPx);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

	    return output;
	  }
	
	public static Bitmap getBadgeBitmap(Context context, PictureDrawable pictureDrawable, String colorString) {
	    Bitmap output = Bitmap.createBitmap((int) pxFromDp(context, 80f), (int) pxFromDp(context, 80f), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);
	    
	    int width = (int) pxFromDp(context, 80);
	    int margin = (int) pxFromDp(context, 5);
	    int padding = (int) pxFromDp(context, 3);
	    int badgeMargin = (int) pxFromDp(context, 18);
	    
	    final Paint paint = new Paint();
	    
	    final Rect rect = new Rect(margin, margin, width - margin, width - margin);
	    final Rect iRect = new Rect(margin + padding, margin + padding, width - margin - padding, width - margin - padding);
	    
	    final RectF iRectF = new RectF(iRect);
	    final RectF rectF = new RectF(rect);
	    
	    final float iRoundPx = width - margin*2 - padding*2;
	    final float roundPx = width - margin* 2;

	    paint.setAntiAlias(true);
	    
	    // svg dimensions
	    Rect svgRect = getSvgRect(pictureDrawable, width, badgeMargin);   
	    
	    // draw border
	    paint.setColor(Color.WHITE);
	    final int color = Color.argb(85, 0, 0, 0);
	    paint.setShadowLayer(3, 0, 0, color);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    paint.setShadowLayer(0, 0, 0, Color.BLACK);
	    
	    paint.setColor(Color.parseColor("#" + colorString));
	    canvas.drawRoundRect(iRectF, iRoundPx, iRoundPx, paint);
	    canvas.drawPicture(pictureDrawable.getPicture(), svgRect);
	    
	    return output;
	}
	
	// temporary solution. Should put in async task
	public static Bitmap tempProcessSvg(Context context, String svgString, String fgColor, String bgColor){
		svgString = svgString.replaceAll("fill=\"#000000\"", "");
    	svgString = svgString.replaceAll("<polygon", "<polygon fill=\"#" + fgColor + "\"");
    	svgString = svgString.replaceAll("<path", "<path fill=\"#" + fgColor + "\"");
    	svgString = svgString.replaceAll("<rect", "<rect fill=\"#" + fgColor + "\"");
    	
    	InputStream is = new ByteArrayInputStream(svgString.getBytes());
		SVG svg = SVGParser.getSVGFromInputStream(is);
		
		return getBadgeCoverBitmap(context, svg.createPictureDrawable(), bgColor);
	}
	
	public static Bitmap getBadgeCoverBitmap(Context context, PictureDrawable pictureDrawable, String colorString){
		Bitmap output = Bitmap.createBitmap((int) pxFromDp(context, 120f), (int) pxFromDp(context, 120f), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);
	    
	    // Another way? getResources?
	    int width = (int) pxFromDp(context, 120);
	    int margin = (int) pxFromDp(context, 8);
	    int outerWidth = (int) pxFromDp(context, 10);
	    int innerMargin = (int) pxFromDp(context, 6);
	    int badgeMargin = (int) pxFromDp(context, 18);
	    int badgeTotalMargin = outerWidth/2 + innerMargin + badgeMargin;
	    
	    final float roundPx = width - margin* 2;
	    final float iRoundPx = width - (margin + outerWidth + innerMargin)*2;
	    
	    final Rect iRect = new Rect(margin + outerWidth/2 + innerMargin, margin + outerWidth/2 + innerMargin,
	    		width - margin - outerWidth/2 - innerMargin, width - margin - outerWidth/2 - innerMargin);
	    
	    final RectF iRectF = new RectF(iRect);
	    
	    final Rect rect = new Rect(margin, margin, width - margin, width - margin);
	    final RectF rectF = new RectF(rect);
	    
	    
	    Paint paint = new Paint();
	    paint.setAntiAlias(true);
	    paint.setColor(Color.parseColor("#" + colorString));

	    final int color = Color.argb(200, 0, 0, 0);
	    paint.setShadowLayer(2, 0, 1, color);
	    
	    canvas.drawRoundRect(iRectF, iRoundPx, iRoundPx, paint);	    
	    
	    paint.setStyle(Paint.Style.STROKE);
	    paint.setStrokeWidth(outerWidth);
	    
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    
	    Rect svgRect = getSvgRect(pictureDrawable, width, badgeTotalMargin);

	    canvas.drawPicture(pictureDrawable.getPicture(), svgRect);
	    
	    return output;
	}
	
	private static Rect getSvgRect(PictureDrawable pictureDrawable, int width, int margin){
		int svgWidth = pictureDrawable.getPicture().getWidth();
	    int svgHeight = pictureDrawable.getPicture().getHeight();
	    int xOffset = 0;
	    int yOffset = 0;
	    
	    int svgMaxWidth = width - margin*2;
	    if (svgWidth > svgHeight){
	    	float ratio = ((float) svgHeight)/svgWidth;
	    	svgWidth = svgMaxWidth;
	    	svgHeight = (int) (svgMaxWidth*ratio);
	    	yOffset = (svgWidth - svgHeight)/2;
	    } else {
	    	float ratio = ((float) svgWidth)/svgHeight;
	    	svgHeight = svgMaxWidth;
	    	svgWidth = (int) (svgMaxWidth*ratio);
	    	xOffset = (svgHeight - svgWidth)/2;
	    }
	    
	    return new Rect(xOffset + margin,
	    		yOffset + margin,
	    		xOffset + svgWidth + margin,
	    		yOffset + svgHeight + margin);
	}

	
	private static float dpFromPx(Context context, float px) {
	    return px / context.getResources().getDisplayMetrics().density;
	}


	private static float pxFromDp(Context context, float dp) {
	    return dp * context.getResources().getDisplayMetrics().density;
	}
}
