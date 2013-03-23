package com.challengecomplete.android.models.goals;

import com.challengecomplete.android.models.badges.BadgeTable;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


public class Goal {
	private long id;
	private String name;
	private String description;
	private int points;
	private long created_at;
	private long updated_at;
	private String deadline;
	private boolean has_deadline;
	private String badge;
	private String fg_color;
	private String bg_color;
	private int owner_id;
	private int parent_id;
	private String svg;
	
	// constructor not complete
	// TODO
	public Goal(int id){
		this.id = id;
	}
	
	// need default values
	public Goal(int id, String name, String description, int points, long created_at, long updated_at,
			String deadline, boolean has_deadline, String badge, String fg_color, String bg_color, int owner_id, int parent_id){
		this.id = id;
		this.name = name;
		this.description = description;
		this.points = points;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deadline = deadline;
		this.has_deadline = has_deadline;
		this.badge = badge;
		this.fg_color = fg_color;
		this.bg_color = bg_color;
		this.owner_id = owner_id;
		this.parent_id = parent_id;
	}
	
	public void saveToDB(Context context) {
		ContentValues values = new ContentValues();
		values.put(GoalTable.COLUMN_ID, id);
	    values.put(GoalTable.COLUMN_NAME, name);
	    values.put(GoalTable.COLUMN_DESCRIPTION, description);
	    values.put(GoalTable.COLUMN_POINTS, points);
	    values.put(GoalTable.COLUMN_CREATED_AT, created_at);
	    values.put(GoalTable.COLUMN_UPDATED_AT, updated_at);
	    values.put(GoalTable.COLUMN_DEADLINE, deadline);
	    int has_deadline_int = 0;
	    if (has_deadline) has_deadline_int = 1;
	    values.put(GoalTable.COLUMN_HAS_DEADLINE, has_deadline_int);
	    values.put(GoalTable.COLUMN_BADGE, badge);
	    values.put(GoalTable.COLUMN_FG_COLOR, fg_color);
	    values.put(GoalTable.COLUMN_BG_COLOR, bg_color);
	    values.put(GoalTable.COLUMN_OWNER_ID, owner_id);
	    values.put(GoalTable.COLUMN_PARENT_ID, parent_id);
	    
	    if (id == 0){
	    	Uri newGoalUri = context.getContentResolver().insert(GoalContentProvider.CONTENT_URI, values);
	    }else{
	    	Uri updateUri = Uri.parse(GoalContentProvider.CONTENT_URI + "/" + id);
		    context.getContentResolver().update(updateUri, values, null, null);
	    }
//	    
	    // TODO
	    // update goal when done
	}
	
	public void delete(Context context, Goal goal) {
		long id = goal.getId();
		Uri deleteUri = Uri.parse(GoalContentProvider.CONTENT_URI + "/" + id);
		context.getContentResolver().delete(deleteUri, null, null);
	}
	
	public void populate(Context context){
		Uri uri = ContentUris.withAppendedId(GoalContentProvider.CONTENT_URI_WITH_EXTRA, this.id);
		String[] projection = GoalTable.allColumnsWithExtra;
		Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
		
		c.moveToFirst();
		
		this.name = c.getString(1);
		this.description = c.getString(2);
		this.points = c.getInt(3);
		this.created_at = c.getLong(4);
		this.updated_at = c.getLong(5);
		this.deadline = c.getString(6);
		this.has_deadline = c.getInt(7) == 1;
		this.badge = c.getString(8);
		this.fg_color = c.getString(9);
		this.bg_color = c.getString(10);
		this.owner_id = c.getInt(11);
		this.parent_id = c.getInt(12);
		
		this.svg = c.getString(14);
	}
	
//	public Task populateFromCursor(Cursor c){
//		Task task = new Task();
//		task.setId(c.getLong(0));
//		task.setName(c.getString(1));
//		task.setDescription(c.getString(2));
//		task.setCategory(c.getString(3));
//		task.setCreated_at(c.getLong(4));
//		task.setDeadline(c.getLong(5));
//		task.setUpdated_at(c.getLong(6));
//		task.setFinished_at(c.getLong(7));
//		task.setPriority(c.getInt(8));
//		task.setDifficulty(c.getInt(9));
//		task.setTime(c.getLong(10));
//		task.setFinished(c.getInt(11) == 1);
//	    return task;
//	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}

	public long getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(long updated_at) {
		this.updated_at = updated_at;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public boolean isHas_deadline() {
		return has_deadline;
	}

	public void setHas_deadline(boolean has_deadline) {
		this.has_deadline = has_deadline;
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public String getFgColor() {
		return fg_color;
	}

	public void setFgColor(String color) {
		this.fg_color = color;
	}
	
	public String getBgColor() {
		return bg_color;
	}

	public void setBgColor(String color) {
		this.bg_color = color;
	}

	public int getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(int owner_id) {
		this.owner_id = owner_id;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	public String getSvg() {
		return svg;
	}

	public void setSvg(String svg) {
		this.svg = svg;
	}	
}
