package com.challengecomplete.android.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.challengecomplete.android.utils.ChallengeComplete;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * All the intents are put in a queue, so no need for locking
 */
public class APIService extends IntentService {
	public static final String TAG = "APIService";
	public static final String NAME = "APIService";

	public APIService() {
		super(NAME);
	}

	public interface Receiver{
		public void onReceive(int resultCode, int action, String result);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (!intent.hasExtra(ServiceHelper.ACTION))
			return;

		int action = intent.getIntExtra(ServiceHelper.ACTION, 0);
		int taskId = intent.getIntExtra(ServiceHelper.TASK_ID, 0);
		
		String results;
		
		switch (action) {
		
		case ServiceHelper.LOGIN:
			Log.i(TAG, "LOGIN");
			
			if(!intent.hasExtra("ftoken") || !intent.hasExtra("fid"))
				return;
			
			String token = intent.getStringExtra("ftoken");
			String fid = intent.getStringExtra("fid");
			
			results = HttpCaller.getRequest(this, "/login?ftoken=" + token + "&fid=" + fid);
			
			// This part only temporary. Should be done in receiver
			if (results != null){
				try {
					JSONObject jObject = new JSONObject(results);
					String mToken = jObject.getString("token");
					Log.i(TAG, mToken);
					ChallengeComplete.setLoggedIn(this);
					ChallengeComplete.setToken(this, mToken);
					Log.i(TAG, "Logged in");
					return;
				} catch (JSONException e){
					
				}
			}
			
//			ServiceHelper mServiceHelper = ServiceHelper.getInstance();
//			mServiceHelper.onReceive(ServiceHelper.SUCCESS, taskId);
			
			break;
		case ServiceHelper.GET_ME:
			Log.i(TAG, "GET ME");
			results = HttpCaller.getRequest(this, "/me");
			
			if (results != null)
				Log.i(TAG, results);
			
			ServiceHelper mServiceHelper = ServiceHelper.getInstance();
			mServiceHelper.onReceive(ServiceHelper.SUCCESS, taskId);
			
			break;
			
//		case ServiceHelper.GET_TASKS:
//			Log.i(TAG, "Get Tasks");
//			String results = HttpCaller.getRequest("/tasks");
//		
//			ContentValues[] contentValues = TaskProcessor.bulkCreateContentValues(results);
//			if (contentValues != null && contentValues.length > 0)
//				getContentResolver().bulkInsert(TaskContentProvider.CONTENT_URI, contentValues);
//			// TODO
//			// Notify Processor.
//			// getProcessor by Id -> runTask (GET_TASKS) to update database
////			getContentResolver()
//			ServiceHelper sHelper = ServiceHelper.getInstance();
//			sHelper.onReceive(ServiceHelper.SUCCESS, taskId);
//			break;
		
		default:
			// No intent specified
			break;
		}

	}

}
