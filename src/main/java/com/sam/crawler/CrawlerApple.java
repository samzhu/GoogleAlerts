package com.sam.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

public class CrawlerApple extends Crawler{
	private String url = "http://www.appledaily.com.tw/rss/newcreate/kind/sec/type/8";

	@Override
	public List<NewsEntry> getNews() throws Exception {
		List<NewsEntry> list = new ArrayList<NewsEntry>();
		try {
			URL feedUrl = new URL(url);
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));

			feed.getEntries().stream().forEach( e -> {
				//System.out.println(e + "\r\n");
				NewsEntry news = new NewsEntry();
				try {
					String link = e.getLink();
					link = link.substring(0, link.length() - 1);
					news.setPortal("appledaily");
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
					e1.printStackTrace();
				}
			});
			this.getLinkContent(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private void getLinkContent(List<NewsEntry> list){
		list.forEach( news -> {
			try {
				org.jsoup.nodes.Document doc = Http.getDoc(news.getReallink());
				String content = doc.select("div.articulum p,div.articulum h2").text();
				news.setAllcontent(content);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

}
