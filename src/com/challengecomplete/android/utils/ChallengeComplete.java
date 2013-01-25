package com.challengecomplete.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ChallengeComplete {
	private static final String PREFERENCE_NAME = "ChallengeComplete";
	
	private static final String KEY_TOKEN = "token";
	private static final String KEY_LOGGEDIN = "loggedin";
	
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
	
	public static SharedPreferences getSharedPreferences (Context context) {
		return context.getSharedPreferences(PREFERENCE_NAME, 0);
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
}
