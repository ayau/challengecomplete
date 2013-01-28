package com.challengecomplete.android.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.challengecomplete.android.R;
import com.challengecomplete.android.fragment.MainFragment;
import com.challengecomplete.android.fragment.SideFragment;
import com.challengecomplete.android.service.APIService;
import com.challengecomplete.android.service.ServiceHelper;
import com.challengecomplete.android.service.ServiceReceiver;
import com.challengecomplete.android.utils.ChallengeComplete;
import com.challengecomplete.android.view.ScrollView;

public class MainActivity extends FragmentActivity implements ServiceReceiver.Receiver{
	private static final String TAG = "MainActivity";
	
	private static final int INTENT_LOGIN = 1;
	
	private ScrollView mScrollView;
	private SideFragment mSideFragment;
	public ServiceReceiver mReceiver;
	private ProgressDialog mProgressDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mScrollView = (ScrollView) findViewById(R.id.scrollview);
        
        FrameLayout fl = (FrameLayout) findViewById(R.id.fragment_container);
        MainFragment mFragment = new MainFragment();
//      TaskListFragment mFragment = new TaskListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(fl.getId(), mFragment);
      
        FrameLayout sp = (FrameLayout) findViewById(R.id.side_panel);
        mSideFragment = new SideFragment();
        ft.add(sp.getId(), mSideFragment);
  
        ft.commit();
        
        // Initializing home button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        // Setup receivers
		mReceiver = new ServiceReceiver(new Handler());
        mReceiver.setReceiver(this);
        
        // If user is not logged in, start LoginActivity
        if (!ChallengeComplete.isLoggedIn(this)){
        	Intent intent = new Intent(this, LoginActivity.class);
        	startActivityForResult(intent, INTENT_LOGIN);
        	return;
        }
        
        // Otherwise, fetch /api/me
        fetchMe();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode){
    		case INTENT_LOGIN:
    			fetchMe();
    			break;
    	}
    }
    
    // temporary
    public void fetchMe(){
    	
    	// Only show dialog if first time fetching
    	if (!ChallengeComplete.hasFetchedMe(this))
    		mProgressDialog = ChallengeComplete.showDialog(this);
		
		Bundle extras = new Bundle();
		extras.putParcelable(ServiceReceiver.NAME, (Parcelable) mReceiver);
		
        ServiceHelper mServiceHelper = ServiceHelper.getInstance();
    	int taskId = mServiceHelper.startService(this, ServiceHelper.GET_ME, extras);
    	Log.i(TAG, "TaskId: " + taskId);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    
    	switch (item.getItemId()) {        
          case android.R.id.home:
        	  mScrollView.open();         
        	  return true;        
          default:            
        	  return super.onOptionsItemSelected(item);    
    	}
    }

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		String results = resultData.getString(APIService.RESULTS);
		
		if (results != null){
			try {
				JSONObject jObject = new JSONObject(results);
				ChallengeComplete.setUserId(this, jObject.getInt("id"));
				ChallengeComplete.setUserAvatar(this, jObject.getString("avatar"));
				ChallengeComplete.setUserName(this, jObject.getString("name"));
				ChallengeComplete.setUserPointsTotal(this, jObject.getInt("points"));
				ChallengeComplete.setUserPointsMonth(this, jObject.getInt("points_this_month"));
				mSideFragment.displayUser();
				ChallengeComplete.setFetchedMe(this);
			} catch (JSONException e){}
		}
		
		ChallengeComplete.dismissDialog(mProgressDialog);
	}
}
