package com.zackehh.rssdemo;

import com.zackehh.rssdemo.R;
import com.zackehh.rssdemo.parser.RSSFeed;
import com.zackehh.rssdemo.util.LoadRSSFeed;
import com.zackehh.rssdemo.util.WriteObjectFile;

import static com.zackehh.rssdemo.parser.RSSUtil.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Main list to display the RSS Feed of a site and takes the 
 * user to the site in the inline browser when an item is clicked. 
 * Also allows manual reloading of the RSS Feed if desired.
 * 
 * @author Isaac Whitfield
 * @version 14/08/2013
 */
public class ListActivity extends Activity {

	// Check if we refreshed
	private boolean isRefresh = false;
	// The adapter for the list
	private ListAdapter adapter;
	// The list to display it in
	private ListView list;
	// The RSSFeed of the site
	private RSSFeed feed;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// Create a new ViewGroup for the fragment
		setContentView(R.layout.feed_list);
		// If we're above Honeycomb
		if(android.os.Build.VERSION.SDK_INT >= 11){
			// Remove the icon from the ActionBar
			getActionBar().setDisplayShowHomeEnabled(false);
		}

		// Get feed from the passed bundle
		feed = (RSSFeed)new WriteObjectFile(this).readObject(getFeedName());

		// Find the ListView we're using
		list = (ListView)findViewById(R.id.listView);
		// Set the vertical edges to fade when scrolling
		list.setVerticalFadingEdgeEnabled(true);

		// Create a new adapter
		adapter = new ListAdapter(this, feed);
		// Set the adapter to the list
		list.setAdapter(adapter);

		// Set on item click listener to the ListView
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// Start the new activity and pass on the feed item
				startActivity(new Intent(getBaseContext(), PostActivity.class).putExtra("pos", arg2));
			}
		});
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create a "change" option to change the feed URL
		MenuItem change = menu.add(Menu.NONE, 0, Menu.NONE, "CHANGE FEED");
		// Create a "reload" menu option to reload the feed
		MenuItem reload = menu.add(Menu.NONE, 1, Menu.NONE, "RELOAD");
		// If we're above Honeycomb
		if(android.os.Build.VERSION.SDK_INT >= 11){
			// Set the change button to show always
			change.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			// Set the reload button to show always
			reload.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// We're refreshing
		isRefresh = true;
		// Depending on what's pressed
		switch (item.getItemId()) {
		case 0:
			// Change the URL
			changeFeed(isRefresh, this);;
			return true;
		case 1:
			// Start parsing the feed again
			new LoadRSSFeed(this, RSSFEEDURL).execute();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Exit instead of going to splash screen
		adapter.notifyDataSetChanged();
	}

	@Override 
	public void onResume(){
		super.onResume();
		// This is awful, but don't change it until I work out another way
		if(isRefresh){
			feed = (RSSFeed)new WriteObjectFile(this).readObject(getFeedName());
			adapter = new ListAdapter(ListActivity.this, feed);
			list.setAdapter(adapter); 
			isRefresh = false;
		}
	}
}