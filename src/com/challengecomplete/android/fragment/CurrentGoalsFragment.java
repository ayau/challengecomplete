package com.challengecomplete.android.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.challengecomplete.android.R;
import com.challengecomplete.android.adapter.CurrentGoalsAdapter;
import com.challengecomplete.android.models.goals.GoalContentProvider;
import com.challengecomplete.android.models.goals.GoalTable;

public class CurrentGoalsFragment extends ListFragment implements
		LoaderCallbacks<Cursor> {

	private final static int LOADER_ID = 0x01;
	private CurrentGoalsAdapter mAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAdapter = new CurrentGoalsAdapter(getActivity(), null);
		setListAdapter(mAdapter);
		getLoaderManager().initLoader(LOADER_ID, null, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_currentgoals, container, false);
		return root;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle extras) {
		return new CursorLoader(getActivity(), GoalContentProvider.CONTENT_URI,
				GoalTable.allColumns, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
		mAdapter.swapCursor(c);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}

}
