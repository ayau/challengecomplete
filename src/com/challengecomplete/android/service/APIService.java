package com.challengecomplete.android.service;

import com.challengecomplete.android.utils.ChallengeComplete;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * All the intents are put in a queue, so no need for locking
 */
public class APIService extends IntentService {
	public static final String TAG = "APIService";
	public static final String NAME = "APIService";
	
	public static final String RESULTS = "results";
	public static final String TASK_ID = "task_id";

	public APIService() {
		super(NAME);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (!intent.hasExtra(ServiceHelper.ACTION))
			return;

		int action = intent.getIntExtra(ServiceHelper.ACTION, 0);
		int taskId = intent.getIntExtra(ServiceHelper.TASK_ID, 0);
		
		ServiceHelper mServiceHelper;
		String results;
		Bundle bundle = new Bundle();
		
		switch (action) {
		case ServiceHelper.LOGIN:
			Log.i(TAG, "LOGIN");
			
			if(!intent.hasExtra("ftoken") || !intent.hasExtra("fid"))
				return;
			
			String token = intent.getStringExtra("ftoken");
			String fid = intent.getStringExtra("fid");
			
			results = HttpCaller.getRequest(this, "/login?ftoken=" + token + "&fid=" + fid);
			
			bundle.putString(RESULTS, results);
			
			mServiceHelper = ServiceHelper.getInstance();
			mServiceHelper.onReceive(ServiceHelper.SUCCESS, taskId, bundle);
			
			break;
		case ServiceHelper.GET_ME:
			Log.i(TAG, "GET ME");
			results = HttpCaller.getRequest(this, "/me");
			
			bundle.putString(RESULTS, results);
			
			mServiceHelper = ServiceHelper.getInstance();
			mServiceHelper.onReceive(ServiceHelper.SUCCESS, taskId, bundle);

			break;

		case ServiceHelper.SYNC:
			Log.i(TAG, "sync");
			
			int id = ChallengeComplete.getUserId(this);
			if (id == 0) return;
			
			results = HttpCaller.getRequest(this, "/users/" + id + "/goals/sync");
			
			bundle.putString(RESULTS, results);
			
			mServiceHelper = ServiceHelper.getInstance();
			mServiceHelper.onReceive(ServiceHelper.SUCCESS, taskId, bundle);
			
			break;		
		default:
			// No intent specified
			break;
		}

	}

}
