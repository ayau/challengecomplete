package com.challengecomplete.android.activity;

import org.json.JSONException;
import org.json.JSONObject;

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
	private ScrollView mScrollView;

	public ServiceReceiver mReceiver;
	
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
        SideFragment sFragment = new SideFragment();
        ft.add(sp.getId(), sFragment);
  
        ft.commit();
        
        // Initializing home button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        // If user is not logged in, start LoginActivity
        if (!ChallengeComplete.isLoggedIn(this)){
        	Intent intent = new Intent(this, LoginActivity.class);
        	startActivity(intent);
        }
        
        // Setup receivers
		mReceiver = new ServiceReceiver(new Handler());
        mReceiver.setReceiver(this);
        
        // Otherwise, fetch /api/me
        fetch();
    }
    
    // temporary
    public void fetch(){
		// Update tasks

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
//				String mToken = jObject.getString("token");
				
			} catch (JSONException e){}
		}
	}
}
