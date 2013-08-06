package com.zackehh.rssdemo;

import com.zackehh.rssdemo.parser.RSSFeed;
import com.zackehh.rssdemo.parser.RSSUtil;
import com.zackehh.rssdemo.util.InlineBrowser;
import com.zackehh.rssdemo.util.WriteObjectFile;

import android.os.Bundle;

/**
 * Displays a chosen post from the RSS feed and displays
 * it inside an inline browser. There are other ways to do
 * this but this is just for example purposes.
 * 
 * @author Isaac Whitfield
 * @version 06/08/2013
 */
public class PostActivity extends InlineBrowser {

	// Position of the clicked item
	private int position;
	// Our RSS feed object
	private RSSFeed feed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the feed object
		feed = (RSSFeed)new WriteObjectFile(this).readObject(RSSUtil.getFeedName());
		// Get the position from the intent
		position = getIntent().getExtras().getInt("pos");
		// Set the title based on the post
		setTitle(feed.getItem(position).getTitle());
		// Load the URL
		browser.loadUrl(feed.getItem(position).getURL());
	}

}
