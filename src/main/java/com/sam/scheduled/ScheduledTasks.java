package com.sam.scheduled;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sam.app.CrawlerConfig;
import com.sam.data.StockAlert;
import com.sam.data.TwseStock;
import com.sam.entities.NewsEntry;
import com.sam.repository.NewsEntryRepository;
import com.sam.service.NewsService;
import com.sam.service.TwseStockService;
import com.sam.utils.Opencsv;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

@Component
public class ScheduledTasks {
	private static final List<String> blacklist = Arrays.asList("blog.udn.com");
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	public static List<TwseStock> stocklist = null;
	@Autowired
	private NewsEntryRepository stockEntryRepository;
	
	@Autowired
	private NewsService newsService;
	@Autowired
	private TwseStockService twseStockService;
	@Autowired
	private CrawlerConfig crawlerConfig;
	
	
	@Scheduled(fixedRate = 15 * 60 * 1000, initialDelay = 10 * 1000)
	public void getNews(){
		newsService.getRSS();
	}

//	@Scheduled(fixedRate = 15 * 60 * 1000)
//	public void getTwseStock(){
//		ScheduledTasks.stocklist = twseStockService.getTwseStock();
//		System.out.println("getTwseStock 成功");
//	}
	
	//@Scheduled(fixedRate = 15 * 60 * 1000)
	public void getGoogleAlert() {
		System.out.println("The time is now " + dateFormat.format(new Date()));
		try {
			List<StockAlert> list = Opencsv.parseFileToBeans("D:/stockalert.txt", ',', StockAlert.class);
			for(StockAlert alert:list){
				String data = this.httpget(alert.getUrl());
				List<NewsEntry> entrylist = this.parse(data);
				for(NewsEntry entry:entrylist){
					List<String> stockidList = new ArrayList();
					stockidList.add(alert.getId());
					entry.setStockid(stockidList);
					if(blacklist.contains(entry.getPortal())){
						System.out.println("黑名單 網域:" + entry.getPortal() + ", 標題:" + entry.getTitle() + ", 內文:" + entry.getContent() + ", 連結:" + entry.getReallink());
					}else{
						stockEntryRepository.save(entry);
					}
					
				}
			}
			System.out.println("執行完成 The time is now " + dateFormat.format(new Date()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
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
	
	public String getDomainName(String url) throws URISyntaxException {
	    URI uri = new URI(url);
	    String domain = uri.getHost();
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
			NewsEntry entry = new NewsEntry();
			String id = el.getChildText("id", ns);
			entry.setId(DigestUtils.md5Hex(id));
			entry.setIdorg(id);
			entry.setLink(el.getChild("link", ns).getAttributeValue("href"));
			entry.setTitle(Jsoup.clean(el.getChildText("title", ns), Whitelist.none()));
			entry.setContent(Jsoup.clean(el.getChildText("content", ns), Whitelist.none()).replace("&nbsp;", ""));
			String reallink = httpgetRealLink(entry.getLink());
			entry.setReallink(reallink);
			entry.setPortal(this.getDomainName(reallink));
			list.add(entry);
		}
		return list;
	}
}
