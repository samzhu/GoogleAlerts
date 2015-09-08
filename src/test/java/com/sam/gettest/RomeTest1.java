package com.sam.gettest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

public class RomeTest1 {

	public static void main(String[] args) {
		try {
			URL feedUrl = new URL("http://www.appledaily.com.tw/rss/newcreate/kind/sec/type/8");
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));
			
			feed.getEntries().stream().forEach( e -> System.out.println(e+"\r\n"));
			//System.out.println(feed.getEntries());
			//System.out.println(feed);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
