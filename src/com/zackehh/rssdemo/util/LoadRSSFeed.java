package com.zackehh.rssdemo.util;

import com.zackehh.rssdemo.ListActivity;
import com.zackehh.rssdemo.R;
import com.zackehh.rssdemo.parser.DOMParser;
import com.zackehh.rssdemo.parser.RSSFeed;

import static com.zackehh.rssdemo.parser.RSSUtil.*;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;

/**
 * Loads an RSS feed from a given URL and writes the object
 * to a file in the application's /data directory. Parses 
 * through the feed and starts the main fragment control
 * upon completion.
 * 
 * @author Isaac Whitfield
 * @version 06/08/2013
 */
public class LoadRSSFeed extends AsyncTask<Void, Void, Void> {

	// The parent context
	private Context parent;
	// Dialog displaying a loading message
	private ProgressDialog refreshDialog;
	// The RSSFeed object
	private RSSFeed feed;
	// The URL we're parsing from
	private String RSSFEEDURL;

	public LoadRSSFeed(Context c, String url){
		// Set the parent
		parent = c;
		// Set the feed URL
		RSSFEEDURL = url;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// Parse the RSSFeed and save the object
		feed = new DOMParser().parseXML(RSSFEEDURL);
		return null;
	}

	@Override
	protected void onPreExecute(){
		// Create a new dialog
		refreshDialog = new ProgressDialog(new ContextThemeWrapper(parent, R.style.AlertBox));
		// Inform of the refresh
		refreshDialog.setMessage("Loading feed...");
		// Spin the wheel whilst the dialog exists
		refreshDialog.setIndeterminate(false);
		// Don't exit the dialog when the screen is touched
		refreshDialog.setCanceledOnTouchOutside(false);
		// Don't exit the dialog when back is pressed
		refreshDialog.setCancelable(true);
		// Show the dialog
		refreshDialog.show();
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		// Write the feed object to a file for better caching
		new WriteObjectFile(parent).writeObject(feed, getFeedName());
		// Set as loaded
		PreferenceManager.getDefaultSharedPreferences(parent).edit().putBoolean("isSetup", true).commit();
		// Dismiss the dialog
		refreshDialog.dismiss();
		// Start the main fragment control
		parent.startActivity(new Intent(parent, ListActivity.class));
	}
}