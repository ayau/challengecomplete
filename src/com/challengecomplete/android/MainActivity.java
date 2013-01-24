package com.challengecomplete.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.widget.FrameLayout;

import com.challengecomplete.android.fragment.MainFragment;
import com.challengecomplete.android.fragment.SideFragment;

public class MainActivity extends FragmentActivity {
	private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        FrameLayout fl = (FrameLayout) findViewById(R.id.fragment_container);
        MainFragment mFragment = new MainFragment();
//      TaskListFragment mFragment = new TaskListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(fl.getId(), mFragment);
      
        FrameLayout sp = (FrameLayout) findViewById(R.id.side_panel);
        SideFragment sFragment = new SideFragment();
        ft.add(sp.getId(), sFragment);
  
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
