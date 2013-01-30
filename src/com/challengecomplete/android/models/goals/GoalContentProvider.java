package com.challengecomplete.android.models.goals;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.challengecomplete.android.database.DBHelper;

// TODO
// Refactor switch statements + isEmpty(Selection) part
public class GoalContentProvider extends ContentProvider {

	private DBHelper dbHelper;

	// code to return when Uri is matched
	private static final int GOALS = 0x01;
	private static final int GOAL = 0x02;

	private static final String AUTHORITY = "com.challengecomplete.android.models.goals";

	private static final String BASE_PATH = "goals";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/goals";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/goals";

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, GOALS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", GOAL);
	}
	
	@Override
	public boolean onCreate() {
		dbHelper = new DBHelper(getContext());
		return false;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		// Uisng SQLiteQueryBuilder instead of query() method
	    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

	    // Check if the caller has requested a column which does not exists
	    checkColumns(projection);

	    // Set the table
	    queryBuilder.setTables(GoalTable.NAME);

	    int uriType = sURIMatcher.match(uri);
	    switch (uriType) {
	    case GOALS:
//	    	ServiceHelper sHelper = ServiceHelper.getInstance();
//	    	sHelper.startService(getContext(), ServiceHelper.GET_GOALS);
	    	break;
	    case GOAL:
	    	// Adding the ID to the original query
	    	queryBuilder.appendWhere(GoalTable.COLUMN_ID + "="
	    			+ uri.getLastPathSegment());
	    	break;
	    default:
	    	throw new IllegalArgumentException("Unknown URI: " + uri);
	    }

	    SQLiteDatabase db = dbHelper.getWritableDatabase();
	    Cursor cursor = queryBuilder.query(db, projection, selection,
	        selectionArgs, null, null, sortOrder);
	    
	    // Make sure that potential listeners are getting notified
	    cursor.setNotificationUri(getContext().getContentResolver(), uri);

	    return cursor;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase db = dbHelper.getWritableDatabase();
	    int rowsDeleted = 0;
	    switch (uriType) {
	    case GOALS:
	    	rowsDeleted = db.delete(GoalTable.NAME, selection, selectionArgs);
	    	break;
	    case GOAL:
	    	String id = uri.getLastPathSegment();
		    if (TextUtils.isEmpty(selection)) {
		    	rowsDeleted = db.delete(GoalTable.NAME,
		    			GoalTable.COLUMN_ID + "=" + id, null);
		    } else {
		        rowsDeleted = db.delete(GoalTable.NAME,
		        		GoalTable.COLUMN_ID + "=" + id 
		        		+ " and " + selection, selectionArgs);
		    }
		    break;
	    default:
	    	throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase db = dbHelper.getWritableDatabase();
	    
	    long id = 0;
	    
	    switch (uriType) {
	    case GOALS:
	      id = db.insert(GoalTable.NAME, null, values);
	      break;
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    
	    getContext().getContentResolver().notifyChange(uri, null);
	    return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase db = dbHelper.getWritableDatabase();
	    int rowsUpdated = 0;
	    
	    switch (uriType) {
	    case GOALS:
	    	rowsUpdated = db.update(
	    			GoalTable.NAME, 
	    			values, 
	    			selection,
	    			selectionArgs);
	    	break;
	    case GOAL:
	    	String id = uri.getLastPathSegment();
	    	if (TextUtils.isEmpty(selection)) {
	    		rowsUpdated = db.update(
	    				GoalTable.NAME, 
	    				values,
	    				GoalTable.COLUMN_ID + "=" + id, 
	    				null);
	    	} else {
	    		rowsUpdated = db.update(
	    				GoalTable.NAME, 
	    				values,
	    				GoalTable.COLUMN_ID + "=" + id 
	    				+ " and " 
	    				+ selection,
	    				selectionArgs);
	    	}
	    	break;
	    default:
	    	throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsUpdated;
	}
	
	// Check if all columns which are requested are available
	private void checkColumns(String[] projection) {
		String[] available = GoalTable.allColumns;
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
		    HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
		    if (!availableColumns.containsAll(requestedColumns)) {
		    	throw new IllegalArgumentException("Unknown columns in projection");
		    }
		}
	}

}
