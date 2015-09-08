package com.sam.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.sam.entities.NewsEntry;
import com.sam.utils.Http;

public class CrawlerYahoo extends Crawler{
	private List<String> urllist = Arrays.asList(
			"https://tw.stock.yahoo.com/rss/url/d/e/N3.html", 
			"https://tw.stock.yahoo.com/rss/url/d/e/N4.html",
			"https://tw.stock.yahoo.com/rss/url/d/e/N5.html");
	//private String url = "https://tw.stock.yahoo.com/rss/url/d/e/N3.html";

	@Override
	public List<NewsEntry> getNews() throws Exception {
		List<NewsEntry> list = new ArrayList<NewsEntry>();
		try {
			urllist.forEach( url -> {
				List<NewsEntry> temp = this.getUrlRss(url);
				list.addAll(temp);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private List<NewsEntry> getUrlRss(String url){
		List<NewsEntry> list = new ArrayList<NewsEntry>();
		try {
			URL feedUrl = new URL(url);
			SyndFeedInput input = new SyndFeedInput();
			XmlReader reader = new XmlReader(feedUrl);
			SyndFeed feed = input.build(reader);
			feed.getEntries().stream().forEach( e -> {
				//System.out.println(e + "\r\n");
				NewsEntry news = new NewsEntry();
				try {
					String link = e.getLink();
					link = link.substring(link.indexOf("*") + 1);
					news.setPortal("yahoo");
					news.setTitle(e.getTitle());
					news.setContent(Jsoup.clean(e.getDescription().getValue(), Whitelist.none()));
					news.setLink(link);
					news.setReallink(link);
					news.setPublished(e.getPublishedDate());
					news.setId(DigestUtils.md5Hex(news.getReallink()));
					news.setStockid(new ArrayList());
					news.setStockname(new ArrayList());
					list.add(news);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			this.getLinkContent(list);
			//System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	private void getLinkContent(List<NewsEntry> list){
		list.forEach( news -> {
			try {
				org.jsoup.nodes.Document doc = Http.getDoc(news.getReallink());
				String content = doc.select("p").text();
				//System.out.println(news.getReallink() + " " + content);
				news.setAllcontent(content);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

}
