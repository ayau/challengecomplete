package com.challengecomplete.android.models.goals;

import android.database.sqlite.SQLiteDatabase;

public class GoalTable {
	public final static String NAME = "Goals";
	public final static String COLUMN_ID = "_id";
	public final static String COLUMN_NAME = "name";
	public final static String COLUMN_DESCRIPTION = "description";
	public final static String COLUMN_POINTS = "points";
	public final static String COLUMN_CREATED_AT = "created_at";
	public final static String COLUMN_UPDATED_AT = "updated_at";
	public final static String COLUMN_DEADLINE = "deadline";
	public final static String COLUMN_HAS_DEADLINE = "has_deadline";
	public final static String COLUMN_BADGE = "badge";
	public final static String COLUMN_COLOR = "color";
	public final static String COLUMN_OWNER_ID = "owner_id";
	public final static String COLUMN_PARENT_ID = "parent_id";
	public final static String COLUMN_IS_CURRENT = "is_current";
	
	public final static String[] allColumns = {
		COLUMN_ID,				// 0
		COLUMN_NAME,			// 1
		COLUMN_DESCRIPTION,		// 2
		COLUMN_POINTS,			// 3
		COLUMN_CREATED_AT,		// 4
		COLUMN_UPDATED_AT,		// 5
		COLUMN_DEADLINE,		// 6
		COLUMN_HAS_DEADLINE,	// 7
		COLUMN_BADGE,			// 8
		COLUMN_COLOR,			// 9
		COLUMN_OWNER_ID,		// 10
		COLUMN_PARENT_ID,		// 11
		COLUMN_IS_CURRENT		// 12
	};
	
	// Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
		  + NAME + "(" + 
		  COLUMN_ID + " integer primary key, " 
		  + COLUMN_NAME + " text not null, "
		  + COLUMN_DESCRIPTION + " text not null, "
		  + COLUMN_POINTS + " integer not null, "
		  + COLUMN_CREATED_AT + " integer not null, "
		  + COLUMN_UPDATED_AT + " integer not null, "
		  + COLUMN_DEADLINE + " text not null, "
		  + COLUMN_HAS_DEADLINE + " integer not null, "
		  + COLUMN_BADGE + " text not null, "
		  + COLUMN_COLOR + " text not null, "
		  + COLUMN_OWNER_ID + " integer not null, "
		  + COLUMN_PARENT_ID + " integer not null, "
		  + COLUMN_IS_CURRENT + " integer not null);";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
  	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + NAME);
    	onCreate(database);
	}
}
