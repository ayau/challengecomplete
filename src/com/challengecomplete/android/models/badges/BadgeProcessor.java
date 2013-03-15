package com.challengecomplete.android.models.badges;

import android.content.ContentValues;


public class BadgeProcessor {

	
	public static ContentValues createContentValues(String name, String svg){ 
		
		ContentValues values = new ContentValues();
	    values.put(BadgeTable.COLUMN_NAME, name);
	    values.put(BadgeTable.COLUMN_SVG, svg);
	    	    
	    return values;
	}
	
	
	
}
