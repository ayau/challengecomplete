package com.challengecomplete.android.utils;

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
//	    final Rect pRect = new Rect(badgeMargin, badgeMargin, width - badgeMargin, width - badgeMargin);
	    
	    final RectF iRectF = new RectF(iRect);
	    final RectF rectF = new RectF(rect);
	    
	    final float iRoundPx = width - margin*2 - padding*2;
	    final float roundPx = width - margin* 2;

	    paint.setAntiAlias(true);
	    
	    // svg dimensions
	    int svgWidth = pictureDrawable.getPicture().getWidth();
	    int svgHeight = pictureDrawable.getPicture().getHeight();
	    int xOffset = 0;
	    int yOffset = 0;
	    
	    int svgMaxWidth = width - badgeMargin*2;
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
	    
	    Rect svgRect = new Rect(xOffset + badgeMargin,
	    		yOffset + badgeMargin,
	    		xOffset + svgWidth + badgeMargin,
	    		yOffset + svgHeight + badgeMargin);   
	    
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

	
	private static float dpFromPx(Context context, float px) {
	    return px / context.getResources().getDisplayMetrics().density;
	}


	private static float pxFromDp(Context context, float dp) {
	    return dp * context.getResources().getDisplayMetrics().density;
	}
}
