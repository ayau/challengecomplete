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
//	    paint.setShadowLayer(5, 0, 0, Color.BLACK);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//	    paint.setShadowLayer(0, 0, 0, Color.BLACK);
	    
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);

	    // draw border
	    paint.setColor(Color.WHITE);
	    paint.setStyle(Paint.Style.STROKE);
	    paint.setStrokeWidth((float) borderPx);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);


	    // draw border
//	    paint.setColor(Color.BLACK);
//	    paint.setStrokeWidth((float) 2);
//	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    
	    return output;
	  }
	
	private static float dpFromPx(Context context, float px) {
	    return px / context.getResources().getDisplayMetrics().density;
	}


	private static float pxFromDp(Context context, float dp) {
	    return dp * context.getResources().getDisplayMetrics().density;
	}
}
