package com.zackehh.rssdemo.parser;

import java.net.URI;
import java.net.URISyntaxException;

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
}
