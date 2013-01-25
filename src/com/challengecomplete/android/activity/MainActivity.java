package com.challengecomplete.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.challengecomplete.android.R;
import com.challengecomplete.android.fragment.MainFragment;
import com.challengecomplete.android.fragment.SideFragment;
import com.challengecomplete.android.service.ServiceHelper;
import com.challengecomplete.android.utils.ChallengeComplete;
import com.challengecomplete.android.view.ScrollView;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class MainActivity extends FragmentActivity {
	private static final String TAG = "MainActivity";
	private ScrollView mScrollView;
	
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
        
        // If user is not logged in
        if (!ChallengeComplete.isLoggedIn(this)){
        	Intent intent = new Intent(this, LoginActivity.class);
        	startActivity(intent);
        }
        
        fetch();
    }
    
    // temporary
    public void fetch(){
		// Update tasks
        ServiceHelper mServiceHelper = ServiceHelper.getInstance();
    	int taskId = mServiceHelper.startService(this, ServiceHelper.GET_ME);
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
//             Intent intent = new Intent(this, ActOnThisActivity.class);            
//             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
//             startActivity(intent);            
        	  return true;        
          default:            
        	  return super.onOptionsItemSelected(item);    
    	}
    }
}
