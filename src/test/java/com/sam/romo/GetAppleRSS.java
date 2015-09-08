package com.sam.romo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
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

public class GetAppleRSS {

	public static void main(String[] args) {
		GetAppleRSS main = new GetAppleRSS();
		main.precess();

	}
	
	public void precess(){
		Locale locale = new Locale("en");
		List<NewsEntry> list = new ArrayList<NewsEntry>();
		try {
			URL feedUrl = new URL("http://www.appledaily.com.tw/rss/newcreate/kind/sec/type/8");
			SyndFeedInput input = new SyndFeedInput();
			XmlReader reader = new XmlReader(feedUrl);
			reader.setDefaultEncoding("UTF-8");
			SyndFeed feed = input.build(reader);
			
			feed.getEntries().stream().forEach( e -> {
				System.out.println(e + "\r\n");
				NewsEntry news = new NewsEntry();
				try {
					news.setPortal(Http.getDomainName(e.getUri()));
					news.setTitle(e.getTitle());
					news.setContent(Jsoup.clean(e.getDescription().getValue(), Whitelist.none()));
					String link = e.getLink();
					link = link.substring(0, link.length() - 1);
					news.setLink(link);
					news.setReallink(link);
					news.setPublished(e.getPublishedDate());
					news.setId(DigestUtils.md5Hex(news.getReallink()));
					list.add(news);
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
				String content = doc.select("div.articulum p,div.articulum h2").text();
				System.out.println(news.getReallink() + "  " + content);
				news.setAllcontent(content);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

}
