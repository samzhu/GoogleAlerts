package com.sam.crawler;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.sam.data.StockAlert;
import com.sam.entities.NewsEntry;
import com.sam.utils.Http;
import com.sam.utils.Opencsv;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class CrawlerGoogleAlert extends Crawler{
	private static final List<String> blacklist = Arrays.asList("blog.udn.com", "dshfy.com", "m.wantgoo.com");
	private List<StockAlert> list = null;
	@Override
	public void setSource(Object source) throws Exception{
		String filepath = (String) source;
		this.list = Opencsv.parseFileToBeans(filepath, ',', StockAlert.class);
	}

	@Override
	public List<NewsEntry> getNews() throws Exception {
		List<NewsEntry> newsEntryList = new ArrayList();
		try {
			for(StockAlert alert:list){
				String data = this.httpget(alert.getUrl());
				//原始全部資料
				List<NewsEntry> entrylist = this.parse(data);
				for(NewsEntry entry:entrylist){
					entry.addStockid(alert.getId());
					entry.addStockname(alert.getName());
				}
				newsEntryList.addAll(entrylist);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newsEntryList;
	}

	public String httpget(String url) throws IOException {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
		.url(url)
		.build();

		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	public String httpgetRealLink(String url) throws IOException {
		String realLink = null;
		org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
		Elements meta = doc.head().select("meta[http-equiv=refresh]");
		if (meta != null){
			String content = meta.attr("content");
			int index = content.indexOf("=") + 2;
			realLink = meta.attr("content").substring(index, content.length() - 1);
		}
		return realLink;
	}
	
	public String getDomainName(String url) throws MalformedURLException {
		URL aURL = new URL(url);
	    String domain = aURL.getHost();
	    return domain.startsWith("www.") ? domain.substring(4) : domain;
	}

	public List<NewsEntry> parse(String data) throws JDOMException, IOException, URISyntaxException{
		List<NewsEntry> list = new ArrayList();
		Namespace ns = Namespace.getNamespace("", "http://www.w3.org/2005/Atom");
		SAXBuilder saxBuilder = new SAXBuilder();

		Document doc = saxBuilder.build(new StringReader(data));
		Element root = doc.getRootElement();

		//System.out.println(root.getChildText("title", ns));

		List<Element> children = root.getChildren("entry", ns);
		for(Element el : children){
			NewsEntry news = new NewsEntry();
			String id = el.getChildText("id", ns);
			news.setId(DigestUtils.md5Hex(id));
			news.setIdorg(id);
			news.setLink(el.getChild("link", ns).getAttributeValue("href"));
			news.setTitle(Jsoup.clean(el.getChildText("title", ns), Whitelist.none()));
			news.setContent(Jsoup.clean(el.getChildText("content", ns), Whitelist.none()).replace("&nbsp;", ""));
			String reallink = httpgetRealLink(news.getLink());
			news.setReallink(reallink);
			news.setPortal(this.getDomainName(reallink));
			news.setStockid(new ArrayList());
			news.setStockname(new ArrayList());
			if(blacklist.contains(news.getPortal())){
				System.out.println("黑名單 網域:" + news.getPortal() + ", 標題:" + news.getTitle() + ", 內文:" + news.getContent() + ", 連結:" + news.getReallink());
			}else{
				list.add(news);
			}
		}
		return list;
	}

}
