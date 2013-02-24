package com.challengecomplete.android.models.badges;

import android.database.sqlite.SQLiteDatabase;

public class BadgeTable {

	public final static String NAME = "Badges";
	public final static String COLUMN_ID = "_id";
	public final static String COLUMN_NAME = "name";
	public final static String COLUMN_SVG = "svg";

	public final static String[] allColumns = { COLUMN_ID, // 0
			COLUMN_NAME, // 1
			COLUMN_SVG // 2
	};

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + NAME + "("
			+ COLUMN_ID + " integer primary key, " + COLUMN_NAME
			+ " text not null, " + COLUMN_SVG + " text not null);";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + NAME);
		onCreate(database);
	}

}
