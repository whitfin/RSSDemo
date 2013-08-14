package com.zackehh.rssdemo.parser;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.validator.routines.UrlValidator;

import com.zackehh.rssdemo.util.LoadRSSFeed;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Just a place to store an RSS feed easily. Also strips
 * feed URLs to the domain for convenient storage. For an
 * actual feed reader you'll have to store the URL somewhere
 * else if it's stored by the user.
 * 
 * @author Isaac Whitfield
 * @version 06/08/2013
 */
public class RSSUtil {

	// Put your RSS feed URL here
	public static String RSSFEEDURL = "http://blog.zackehh.com/feed/";

	// Change to a given feed
	public static void changeFeed(boolean refresh, Context context){
		changeURL(refresh, context);
	}

	// Returns the filename of the stored feed
	public static String getFeedName(){
		return getDomainName(RSSFEEDURL);
	}

	// Strips a URL to the domain
	private static String getDomainName(String url) {
		String domain = null;
		try {
			domain = new URI(url).getHost();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return domain.startsWith("www.") ? domain.substring(4) : domain;
	}

	// Takes a string URL as a new feed
	private static void changeURL(final boolean refresh, final Context context){
		// Set the content view
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// Set an EditText view to get user input 
		final EditText input = new EditText(context);
		// Set the alert title
		builder.setTitle("Changing RSS Feed URL:")
		// Set the alert message
		.setMessage("Please enter a valid RSS URL:")
		// Set the view to the dialog
		.setView(input)
		// Can't exit via back button
		.setCancelable(false)
		// Set the positive button action
		.setPositiveButton("OK", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				// Check for a valid URL
				if(new UrlValidator().isValid(input.getText().toString())){
					// Store the new URL we're working with
					RSSFEEDURL = input.getText().toString();
				}
				// Parse the new feed
				new LoadRSSFeed(context, RSSFEEDURL).execute();
			}
		})
		// Set the negative button actions
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				// If it's during initial loading
				if(!refresh){
					// Exit the application
					((Activity)context).finish();
				} else {
					// Otherwise do nothing
					dialog.dismiss();
				}
			}
		});
		// Create dialog from builder
		AlertDialog alert = builder.create();
		// Don't exit the dialog when the screen is touched
		alert.setCanceledOnTouchOutside(false);
		// Show the alert
		alert.show();
		// Center the message
		((TextView)alert.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
		// Center the title of the dialog
		((TextView)alert.findViewById((context.getResources().getIdentifier("alertTitle", "id", "android")))).setGravity(Gravity.CENTER);
	}
}
