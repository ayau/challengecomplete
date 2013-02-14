package com.challengecomplete.android.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;

import com.challengecomplete.android.R;
import com.challengecomplete.android.adapter.CurrentGoalsAdapter;
import com.challengecomplete.android.models.goals.GoalContentProvider;
import com.challengecomplete.android.models.goals.GoalTable;

public class CurrentGoalsFragment extends ListFragment implements LoaderCallbacks<Cursor> {

	private final static int LOADER_ID = 0x01;
	private CurrentGoalsAdapter mAdapter;
	//private boolean actionBarShowing = false;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		LayoutInflater inflater = getActivity().getLayoutInflater();
		View header = inflater.inflate(R.layout.list_header_goal, null, false);
		TextView dateText = (TextView) header.findViewById(R.id.current_day);;
		dateText.setText(getDayOfMonthString());
		getListView().addHeaderView(header);
		
		mAdapter = new CurrentGoalsAdapter(getActivity(), null);  
		setListAdapter(mAdapter);
		getLoaderManager().initLoader(LOADER_ID, null, this);
		
		getListView().setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				mAdapter.deselect();
			}
			
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_currentgoals, container, false);
		return root;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle extras) {
		// TODO
		// Handle this in ContentProvider instead?
		return new CursorLoader(getActivity(), GoalContentProvider.CONTENT_URI,
				GoalTable.allColumns, GoalTable.NAME + "." + GoalTable.COLUMN_IS_CURRENT + "=1", null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
		mAdapter.swapCursor(c);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}
	
	// Returns a string in the format of "Feburary 2013 (Day 7 of 28)" based on current date
	public String getDayOfMonthString(){
		Calendar calendar = Calendar.getInstance();
		int numDays = calendar.getActualMaximum(Calendar.DATE);
		SimpleDateFormat month_date = new SimpleDateFormat("MMMMMMMMM");
		String month_name = month_date.format(calendar.getTime());
		
		return month_name + " " + calendar.get(Calendar.YEAR) 
				+ " (Day " + calendar.get(Calendar.DAY_OF_MONTH) 
				+ " of " + numDays + ")";
	}
	
	
//	@Override
//	public void onListItemClick(ListView l, View v, int position, long id) {
//        Toast.makeText(v.getContext(), id + " selected", Toast.LENGTH_SHORT).show();
//    }
	
	// Currently not in use
	/**
	public void showActionbarBottom(){
		View v = getView().findViewById(R.id.actionbar_bottom);
		v.setVisibility(View.VISIBLE);
		TranslateAnimation slide = new TranslateAnimation(0, 0, 100, 0);   
		slide.setDuration(200);   
		slide.setFillAfter(true);
		v.startAnimation(slide);
		actionBarShowing = true;
	}
	
	public void hideActionbarBottom(){
		View v = getView().findViewById(R.id.actionbar_bottom);
		v.clearAnimation();
		v.setVisibility(View.GONE);
		actionBarShowing = false;
	}
	
	public boolean getActionBarShowing(){
		return actionBarShowing;
	}
	**/
}
