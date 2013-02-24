package com.challengecomplete.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.challengecomplete.android.models.badges.BadgeTable;
import com.challengecomplete.android.models.goals.GoalTable;

// TODO
// Convert to singleton class
public class DBHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "challengecomplete.db";
	public static final int DB_VERSION = 1;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		BadgeTable.onCreate(database);
		GoalTable.onCreate(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVer, int newVer) {
		BadgeTable.onUpgrade(database, oldVer, newVer);
		GoalTable.onUpgrade(database, oldVer, newVer);
	}
}
