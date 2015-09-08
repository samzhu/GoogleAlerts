package com.sam.romo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.sam.entities.NewsEntry;
import com.sam.utils.Http;

public class GetChinatimesRSS {
	
	private String url = "http://feeds.feedburner.com/chinatimes/commercial-stock";

	public static void main(String[] args) {
		GetChinatimesRSS main = new GetChinatimesRSS();
		main.precess();

	}
	
	public void precess(){
		List<NewsEntry> list = new ArrayList<NewsEntry>();
		try {
			URL feedUrl = new URL(url);
			SyndFeedInput input = new SyndFeedInput();
			XmlReader reader = new XmlReader(feedUrl);
			SyndFeed feed = input.build(reader);
			feed.getEntries().stream().forEach( e -> {
				NewsEntry news = new NewsEntry();
				try {
					String link = e.getLink();
					news.setPortal(Http.getDomainName(link));
					news.setTitle(e.getTitle());
					news.setContent(Jsoup.clean(e.getDescription().getValue(), Whitelist.none()));
					news.setLink(link);
					news.setReallink(link);
					news.setPublished(e.getPublishedDate());
					news.setId(DigestUtils.md5Hex(news.getReallink()));
					list.add(news);
					//System.out.println(news);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			//System.out.println(list);
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
		this.getLinkContent(list);
	}
	
	private void getLinkContent(List<NewsEntry> list){
		list.forEach( news -> {
			try {
				org.jsoup.nodes.Document doc = Http.getDoc(news.getReallink());
				String content = doc.select("article.clear-fix p").text();
				System.out.println(content);
				news.setAllcontent(content);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

}
