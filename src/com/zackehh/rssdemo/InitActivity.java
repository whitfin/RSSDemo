package com.zackehh.rssdemo;

import com.zackehh.rssdemo.R;
import static com.zackehh.rssdemo.parser.RSSUtil.*;
import com.zackehh.rssdemo.util.LoadRSSFeed;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Base screen shown as the feed is initially loaded. Not 
 * shown after the initial loading. Not needed, but here
 * as a placeholder in case you're loading a lot of data.
 * 
 * @author Isaac Whitfield
 * @version 06/08/2013
 */
public class InitActivity extends Activity {

	// Keep track of when feed exists
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get our preferences
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		// Check if a feed exists
		if(!prefs.getBoolean("isSetup", false)){
			// Set the content view
			setContentView(R.layout.splash);
			// Detect if there's a connection issue or not
			ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			// If there's a connection problem
			if (conMgr.getActiveNetworkInfo() == null
					|| !conMgr.getActiveNetworkInfo().isConnected()
					|| !conMgr.getActiveNetworkInfo().isAvailable()) {
				// Display an alert to the user
				AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertBox));
				// Tell the user what happened
				builder.setMessage("Unable to reach server.\nPlease check your connectivity.")
				// Alert title
				.setTitle("Connection Error")
				// Can't exit via back button
				.setCancelable(false)
				// Create exit button
				.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// Exit the application
						finish();
					}
				});
				// Create dialog from builder
				AlertDialog alert = builder.create();
				// Show dialog
				alert.show();
				// Center the message of the dialog
				((TextView)alert.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
				// Center the title of the dialog
				((TextView)alert.findViewById((getResources().getIdentifier("alertTitle", "id", "android")))).setGravity(Gravity.CENTER);
			} else {
				// Set the content view
				setContentView(R.layout.splash);
				// Load the RSS Feed
				new LoadRSSFeed(this, RSSFEEDURL).execute();
			}
		} else {
			// Start the new activity
			startActivity(new Intent(this, ListActivity.class));
			// Kill this one
			finish();
		}
	}
}
