package com.challengecomplete.android.models.goals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;


public class GoalProcessor {

	public final static String ID = "id";
	public final static String NAME = "name";
	public final static String DESCRIPTION = "description";
	public final static String POINTS = "points";
	public final static String CREATED_AT = "created_at";
	public final static String UPDATED_AT = "updated_at";
	public final static String DEADLINE = "deadline";
	public final static String HAS_DEADLINE = "has_deadline";
	public final static String BADGE = "badge";
	public final static String COLOR = "color";
	public final static String OWNER_ID = "owner_id";
	public final static String PARENT_ID = "parent_id";
	
	public static ContentValues[] bulkCreateContentValues(String results){
		JSONArray json;
		try {
			json = new JSONArray(results);
			ContentValues[] contentValues = new ContentValues[json.length()];
			
			for (int i = 0; i < json.length(); i++){
				JSONObject o = json.getJSONObject(i);
				int id = o.getInt(ID);
				String name = o.getString(NAME);
				String description = o.getString(DESCRIPTION);
				int points = o.getInt(POINTS);
				long created_at = o.getLong(CREATED_AT);
				long updated_at = o.getLong(UPDATED_AT);
				String deadline = o.getString(DEADLINE);
				int has_deadline = o.getInt(HAS_DEADLINE);
				String badge = o.getString(BADGE);
				String color = o.getString(COLOR);
				int owner_id = o.getInt(OWNER_ID);
				int parent_id = o.getInt(PARENT_ID);
				
//				String status = ServiceHelper.STATUS_OK;
				
				ContentValues cv = createContentValues(id, name, description, points, created_at, updated_at, deadline, 
						has_deadline, badge, color, owner_id, parent_id);
				
				contentValues[i] = cv;
			}
		
			return contentValues;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ContentValues createContentValues(int id, String name, String description, int points,
			long created_at, long updated_at, String deadline, 
			int has_deadline, String badge, String color, int owner_id, int parent_id){ // state
		
		ContentValues values = new ContentValues();
		values.put(GoalTable.COLUMN_ID, id);
	    values.put(GoalTable.COLUMN_NAME, name);
	    values.put(GoalTable.COLUMN_DESCRIPTION, description);
	    values.put(GoalTable.COLUMN_POINTS, points);
	    values.put(GoalTable.COLUMN_CREATED_AT, created_at);
	    values.put(GoalTable.COLUMN_UPDATED_AT, updated_at);
	    values.put(GoalTable.COLUMN_DEADLINE, deadline);
	    values.put(GoalTable.COLUMN_HAS_DEADLINE, has_deadline);
	    values.put(GoalTable.COLUMN_BADGE, badge);
	    values.put(GoalTable.COLUMN_COLOR, color);
	    values.put(GoalTable.COLUMN_OWNER_ID, owner_id);
	    values.put(GoalTable.COLUMN_PARENT_ID, owner_id);
	    	    
	    return values;
	}
	
	
	public static void delete(Context context, long id) {
		Uri deleteUri = Uri.parse(GoalContentProvider.CONTENT_URI + "/" + id);
		context.getContentResolver().delete(deleteUri, null, null);
	}
	
}
