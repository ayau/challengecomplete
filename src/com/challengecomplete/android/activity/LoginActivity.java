package com.challengecomplete.android.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.challengecomplete.android.R;
import com.challengecomplete.android.service.APIService;
import com.challengecomplete.android.service.ServiceHelper;
import com.challengecomplete.android.service.ServiceReceiver;
import com.challengecomplete.android.utils.ChallengeComplete;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class LoginActivity extends Activity implements ServiceReceiver.Receiver {
	private static final String TAG = "LoginActivity";
	
	public ServiceReceiver mReceiver;
	private ProgressDialog mProgressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mReceiver = new ServiceReceiver(new Handler());
        mReceiver.setReceiver(this);
	}

	public void login(View v) {
		mProgressDialog = ChallengeComplete.showDialog(LoginActivity.this);
		
		// start Facebook Login
		Session.openActiveSession(this, true, new Session.StatusCallback() {

			// callback when session changes state
			@Override
			public void call(final Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {

					// make request to the /me API
					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

						// callback after Graph API response with user
						// object
						@Override
						public void onCompleted(GraphUser user, Response response) {
							if (user != null) {
								Log.i("ID IS", user.getId());
								Log.i("ACCESS TOKEN IS", session.getAccessToken());

								Bundle extras = new Bundle();
								extras.putString("ftoken", session.getAccessToken());
								extras.putString("fid", user.getId());
								extras.putParcelable(ServiceReceiver.NAME, (Parcelable) mReceiver);

								ServiceHelper mServiceHelper = ServiceHelper.getInstance();
								int taskId = mServiceHelper.startService(LoginActivity.this,ServiceHelper.LOGIN, extras);
							}
						}
					});
				}
			}
		});
	}
	
    // For facebook login
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		String results = resultData.getString(APIService.RESULTS);
		
		ChallengeComplete.dismissDialog(mProgressDialog);
			
		if (results != null){
			try {
				JSONObject jObject = new JSONObject(results);
				String mToken = jObject.getString("token");
				Log.i(TAG, mToken);
				ChallengeComplete.setLoggedIn(this, true);
				ChallengeComplete.setToken(this, mToken);
				Log.i(TAG, "Logged in");
				finish();
				return;
			} catch (JSONException e){
				
			}
		}
	}
}
