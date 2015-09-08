package com.sam.romo;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.codec.digest.DigestUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.sam.entities.NewsEntry;
import com.sam.utils.Http;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class GetEttodayRSS {

	private String url = "http://feeds.feedburner.com/ettoday/finance";

	public static void main(String[] args) {
		GetEttodayRSS main = new GetEttodayRSS();
		main.precess();

	}

	public void precess(){
		List<NewsEntry> list = new ArrayList<NewsEntry>();
		try {

			String data = this.httpget(url);
			list = this.parse(data);
			System.out.println(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String httpget(String url) throws IOException {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
		.url(url)
		.build();

		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	public List<NewsEntry> parse(String data) throws JDOMException, IOException{
		List<NewsEntry> list = new ArrayList<NewsEntry>();
		Locale locale = new Locale("en");
		SAXBuilder saxBuilder = new SAXBuilder();
		//Document doc = saxBuilder.build(new File("D://xmltest.txt"));
		Document doc = saxBuilder.build(new StringReader(data));
		Element root = doc.getRootElement();
		List<Element> children = root.getChild("channel").getChildren("item");
		for(Element el : children){
			NewsEntry news = new NewsEntry();
			try {
				String link = el.getChildText("link");
				news.setPortal(Http.getDomainName(link));
				news.setTitle(el.getChildText("title").trim());
				news.setContent(Jsoup.clean(el.getChildText("description"), Whitelist.none()));
				news.setLink(link);
				news.setReallink(link);
				Element pubDate = el.getChild("pubDate");
				news.setPublished(new SimpleDateFormat("EEE,dd MMM yyyy HH:mm:ss Z", locale).parse(pubDate.getText()));
				news.setId(DigestUtils.md5Hex(news.getReallink()));
				list.add(news);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}

}
