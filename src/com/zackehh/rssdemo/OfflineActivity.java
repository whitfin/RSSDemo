package com.zackehh.rssdemo;

import com.zackehh.rssdemo.parser.RSSFeed;
import com.zackehh.rssdemo.parser.RSSUtil;
import com.zackehh.rssdemo.util.WriteObjectFile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.ScrollView;

/**
 * Class to allow reading of posts offline, and without cache. Effectively
 * Plan C for displaying a post in case of an offline device and a missing
 * cache.
 * 
 * @author Isaac Whitfield
 * @version 15/08/2013
 */
public class OfflineActivity extends Activity {

	// The RSS Feed item
	private RSSFeed feed;
	// The webview to display in
	private WebView browser;

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offline_reading);
		// Enable the vertical fading edge (by default it is disabled)
		((ScrollView)findViewById(R.id.scrollview)).setVerticalFadingEdgeEnabled(true);
		// Get the feed object
		feed = (RSSFeed)new WriteObjectFile(this).readObject(RSSUtil.getFeedName());
		// Get the position from the intent
		int position = getIntent().getExtras().getInt("pos");
		// Set the title based on the post
		setTitle(feed.getItem(position).getTitle());
		// Initialize the views
		browser = (WebView)findViewById(R.id.browser);
		// Set the background transparent
		browser.setBackgroundColor(Color.TRANSPARENT);
		// Set our webview properties
		WebSettings browserSettings = browser.getSettings();
		browserSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		browserSettings.setPluginState(PluginState.ON);
		browserSettings.setJavaScriptEnabled(true);
		// Set the views
		browser.loadDataWithBaseURL("http://blog.zackehh.com/", feed.getItem(position).getDescription(), "text/html", "UTF-8", null);
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create a "back" menu option to go back to the parent activity
		MenuItem back = menu.add(Menu.NONE, 0, Menu.NONE, "BACK");
		// If we're on Honeycomb or above
		if(android.os.Build.VERSION.SDK_INT >= 11){
			// Show the back button always
			back.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			// Finish the activity
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
