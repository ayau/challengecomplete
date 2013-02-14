package com.challengecomplete.android.models.goals;

import java.util.ArrayList;

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
	public final static String IS_CURRENT = "is_current";
	public final static String STATUS = "status";
	
	public final static String STATUS_CREATED = "created";
	public final static String STATUS_UPDATED = "updated";
	
	public static class SyncContentValues{
		public ContentValues[] created = null;
		public ContentValues[] updated = null;
	}
	
	public static SyncContentValues bulkCreateContentValues(String results){
		SyncContentValues syncContentValues = new SyncContentValues();
		
		JSONArray json;
		try {
			json = new JSONArray(results);
			ArrayList<ContentValues> created = new ArrayList<ContentValues>();
//			ContentValues[] contentValues = new ContentValues[json.length()];
			ArrayList<ContentValues> updated = new ArrayList<ContentValues>();
			
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
				int is_current = o.optInt(IS_CURRENT, 0);
				String status = o.getString(STATUS);
				
				ContentValues cv = createContentValues(id, name, description, points, created_at, updated_at, deadline, 
						has_deadline, badge, color, owner_id, parent_id, is_current);
				
				if (status.equals(STATUS_CREATED)){
					created.add(cv);
				} else if (status.equals(STATUS_UPDATED)){
					updated.add(cv);
				}
//				contentValues[i] = cv;
			}
			
			if (created.size() > 0)
				syncContentValues.created = created.toArray(new ContentValues[created.size()]);
			if (updated.size() > 0)
				syncContentValues.updated = updated.toArray(new ContentValues[created.size()]);
			
			return syncContentValues;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ContentValues createContentValues(int id, String name, String description, int points,
			long created_at, long updated_at, String deadline, 
			int has_deadline, String badge, String color, int owner_id, int parent_id, int is_current){ // state
		
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
	    values.put(GoalTable.COLUMN_IS_CURRENT, is_current);
	    	    
	    return values;
	}
	
	
	public static void delete(Context context, long id) {
		Uri deleteUri = Uri.parse(GoalContentProvider.CONTENT_URI + "/" + id);
		context.getContentResolver().delete(deleteUri, null, null);
	}
	
}
