package com.challengecomplete.android.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Static class that stores and retrieves preferences
 * @author alexyau
 *
 */
public class ChallengeComplete {
	private static final String PREFERENCE_NAME = "ChallengeComplete";
	
	private static final String KEY_TOKEN = "token";
	private static final String KEY_LOGGEDIN = "loggedin";
	
	// Boolean indicating whether a route has been fetched before
	private static final String KEY_FETCHED_ME = "fetched_me";
	
	// User information
	private static final String KEY_USER_ID = "user_id";
	private static final String KEY_USER_AVATAR = "user_avatar";
	private static final String KEY_USER_NAME = "user_name";
	private static final String KEY_USER_POINTS_TOTAL = "user_points_total";
	private static final String KEY_USER_POINTS_MONTH = "user_points_month";
	
	// Sync
	private static final String KEY_LAST_SYNCED = "last_synced";
	
	
	public static boolean isLoggedIn(Context context){
		SharedPreferences pref = getSharedPreferences(context);
		return pref.getBoolean(KEY_LOGGEDIN, false);
	}
	
	public static void setLoggedIn(Context context, boolean isLoggedIn){
		writeBooleanPreferences(context, KEY_LOGGEDIN, isLoggedIn);
	}
	
	public static String getToken(Context context){
		SharedPreferences pref = getSharedPreferences(context);
		return pref.getString(KEY_TOKEN, null);
	}
	
	public static void setToken(Context context, String token){
		writeStringPreferences(context, KEY_TOKEN, token);
	}
	
	public static boolean hasFetchedMe(Context context){
		SharedPreferences pref = getSharedPreferences(context);
		return pref.getBoolean(KEY_FETCHED_ME, false);
	}
	
	public static void setFetchedMe(Context context){
		writeBooleanPreferences(context, KEY_FETCHED_ME, true);
	}
	
	public static int getUserId(Context context){
		SharedPreferences pref = getSharedPreferences(context);
		return pref.getInt(KEY_USER_ID, 0);
	}
	
	public static void setUserId(Context context, int id){
		writeIntPreferences(context, KEY_USER_ID, id);
	}
	
	public static String getUserAvatar(Context context){
		SharedPreferences pref = getSharedPreferences(context);
		return pref.getString(KEY_USER_AVATAR, null);
	}
	
	public static void setUserAvatar(Context context, String avatar){
		writeStringPreferences(context, KEY_USER_AVATAR, avatar);
	}
	
	public static String getUserName(Context context){
		SharedPreferences pref = getSharedPreferences(context);
		return pref.getString(KEY_USER_NAME, null);
	}
	
	public static void setUserName(Context context, String name){
		writeStringPreferences(context, KEY_USER_NAME, name);
	}
	
	public static int getUserPointsTotal(Context context){
		SharedPreferences pref = getSharedPreferences(context);
		return pref.getInt(KEY_USER_POINTS_TOTAL, 0);
	}
	
	public static void setUserPointsTotal(Context context, int points){
		writeIntPreferences(context, KEY_USER_POINTS_TOTAL, points);
	}
	
	public static int getUserPointsMonth(Context context){
		SharedPreferences pref = getSharedPreferences(context);
		return pref.getInt(KEY_USER_POINTS_MONTH, 0);
	}
	
	public static void setUserPointsMonth(Context context, int points){
		writeIntPreferences(context, KEY_USER_POINTS_MONTH, points);
	}
	
	public static long getLastSynced(Context context){
		SharedPreferences pref = getSharedPreferences(context);
		return pref.getLong(KEY_LAST_SYNCED, 0);
	}
	
	public static void setLastSynced(Context context, long time){
		writeLongPreferences(context, KEY_LAST_SYNCED, time);
	}
	
	
	// Helper methods
	public static SharedPreferences getSharedPreferences (Context context) {
		return context.getSharedPreferences(PREFERENCE_NAME, 0);
	}
	
	
	private static void writeIntPreferences(Context context, String name, int value){
		SharedPreferences pref = getSharedPreferences(context);
		Editor editor = pref.edit();
		
		editor.putInt(name, value);
		
		editor.commit();
	}
	
	private static void writeBooleanPreferences(Context context, String name, boolean value){
		SharedPreferences pref = getSharedPreferences(context);
		Editor editor = pref.edit();
		
		editor.putBoolean(name, value);
		
		editor.commit();
	}
	
	private static void writeStringPreferences(Context context, String name, String value){
		SharedPreferences pref = getSharedPreferences(context);
		Editor editor = pref.edit();
		
		editor.putString(name, value);
		
		editor.commit();
	}
	
	private static void writeLongPreferences(Context context, String name, long value){
		SharedPreferences pref = getSharedPreferences(context);
		Editor editor = pref.edit();
		
		editor.putLong(name, value);
		
		editor.commit();
	}
	
	// Other utils
	public static ProgressDialog showDialog(Context context) {
		return ProgressDialog.show(context, "", "Loading. Please wait...", true);
	}
	
	public static void dismissDialog(ProgressDialog pd) {
		if (pd != null) 
			pd.dismiss();
	}
}
