package com.zackehh.rssdemo.util;

import com.zackehh.rssdemo.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.ProgressBar;

/**
 * Implementation of an inline browser inherited throughout 
 * the application. Allows for caching of pages, and displays
 * a loading bar/dialog when a URL is being loaded. Overrides
 * the back button to go back inside the webview if available.
 * Because of this a menu item is added to allow the user to 
 * exit the activity.
 * 
 * @author Isaac Whitfield
 * @version 14/08/2013
 */
public class InlineBrowser extends Activity {

	// The loading bar on the base of the ActionBar
	protected ProgressBar loadingBar;
	// The loading dialog to inform the user
	protected ProgressDialog loadingDialog;
	// The settings of the WebView
	protected WebSettings browserSettings;
	// The inline browser to display
	protected WebView browser;

	// Suppress warnings we've catered for
	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the layout
		setContentView(R.layout.inline_browser);
		// Remove the icon from the ActionBar
		if(android.os.Build.VERSION.SDK_INT >= 11){
			// Set the splash background
			getActionBar().setDisplayShowHomeEnabled(false);
		}
		// Set up the WebView 
		browser = (WebView)findViewById(R.id.inlineBrowser);
		// Set up the settings needed
		browserSettings = browser.getSettings();
		// Set the plugin state
		browserSettings.setPluginState(PluginState.ON);
		// Enable Javascript support
		browserSettings.setJavaScriptEnabled(true);
		// Set up caching for the browser
		browserSettings.setDomStorageEnabled(true);
		browserSettings.setAppCacheMaxSize(1024*1024*8);
		browserSettings.setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
		browserSettings.setAllowFileAccess(true);
		browserSettings.setAppCacheEnabled(true);
		browserSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		// Create a new dialog to inform of the loading
		loadingDialog = new ProgressDialog(new ContextThemeWrapper(this, R.style.AlertBox));
		// Set the text of the dialog
		loadingDialog.setMessage("Loading...");
		// Spin the dialog
		loadingDialog.setIndeterminate(false);
		// Stop tapping canceling the refresh
		loadingDialog.setCanceledOnTouchOutside(false);
		// Allow back button canceling the refresh
		loadingDialog.setCancelable(false);
		// Show the dialog
		loadingDialog.show();
		// Get the loading bar we're going to show
		loadingBar = (ProgressBar)findViewById(R.id.loadingBar);
		// Set a WebClient to display loading
		browser.setWebChromeClient(new WebChromeClient(){
			public void onProgressChanged(WebView view, int progress){
				// If loading isn't complete, make sure the loading bar is visible
				if(progress < 100 && loadingBar.getVisibility() == ProgressBar.GONE){
					// Set the loading bar as visible
					loadingBar.setVisibility(ProgressBar.VISIBLE);
				}
				// Update the loading bar
				loadingBar.setProgress(progress);
				// If loading is done
				if(progress == 100){
					// Remove the loading bar
					loadingBar.setVisibility(ProgressBar.GONE);
					// Display the WebView
					browser.setVisibility(View.VISIBLE);
					// Dismiss the dialog
					loadingDialog.dismiss();
				}
			}
		});
		// New WebClient to allow in-line loading of links
		browser.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		// Set the scroll bar style
		browser.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
	}

	@Override
	public void onBackPressed(){
		// If we can go back in the WebView
		if(browser.canGoBack()){
			// Do it
			browser.goBack();
			// Otherwise, take the normal action
		} else {
			// Cleanup any remaining HTML
			browser.loadUrl("file:///android_asset/nonexistent.html");
			// Finish the activity
			finish();
		}
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
			// Cleanup any remaining HTML
			browser.loadUrl("file:///android_asset/nonexistent.html");
			// Finish the activity
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume(){
		super.onResume();
		// Override transition for entering the activity
		overridePendingTransition(0, 0);
	}

	@Override
	public void onPause(){
		super.onPause();
		// Override transition for entering the activity
		overridePendingTransition(0, 0);
	}
}
