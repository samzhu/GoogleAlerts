package com.sam.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.Setter;

import org.apache.commons.codec.digest.DigestUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.sam.app.CrawlerConfig;
import com.sam.crawler.Crawler;
import com.sam.data.TwseStock;
import com.sam.entities.NewsEntry;
import com.sam.repository.NewsEntryRepository;
import com.sam.scheduled.ScheduledTasks;
import com.sam.utils.Http;

@Service
public class NewsService {

	@Autowired
	private NewsEntryRepository newsEntryRepository;

	@Autowired
	private CrawlerConfig crawlerConfig;

	public void getRSS(){
		//crawlerConfig.getCrawlerClassArray().forEach( classname -> System.out.println(classname));
		
		crawlerConfig.getTargets().forEach( crawlerTarget -> {
			try {
				Crawler crawler = (Crawler) Class.forName("com.sam.crawler." + crawlerTarget.getName()).newInstance();
				crawler.setSource(crawlerTarget.getSource());
				System.out.println(crawlerTarget.getName() + " start");
				List<NewsEntry> list = crawler.getNews();
				//System.out.println(list);
				list.forEach( e-> {
					if(ScheduledTasks.stocklist != null){
						ScheduledTasks.stocklist.stream().forEach( s -> {
							boolean hasName = (e.getTitle().indexOf(s.getName()) != -1);
							//boolean hasId = (e.getAllcontent().indexOf(s.getId()) != -1);
							if(hasName){// 九成機率是對的就暫時視為正確
								e.addStockid(s.getId());
								e.addStockname(s.getName());
							}
						});
					}
					newsEntryRepository.save(e);
//					if(e.getStockid().size() > 0){
//						newsEntryRepository.save(e);
//					}else{
//						System.out.println("丟棄 " + e.getTitle() + " " + e.getReallink());
//					}
				});
				System.out.println(crawlerTarget.getName() + " end size=" + list.size());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});


	}


}
