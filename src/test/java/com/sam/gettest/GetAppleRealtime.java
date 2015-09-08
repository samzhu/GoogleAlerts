package com.sam.gettest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.jdom2.JDOMException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class GetAppleRealtime {
	private String url = "http://www.appledaily.com.tw/realtimenews/section/finance/paperart_right";

	public static void main(String[] args) {
		GetAppleRealtime main = new GetAppleRealtime();
		try {
			main.precess();
		} catch (IOException | JDOMException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void precess() throws IOException, JDOMException, URISyntaxException{
		Document doc = Jsoup.connect(url).get();
		URI uri = new URI(url);
		//System.out.println(uri.getScheme()+"://"+uri.getHost());
		Elements els = doc.select("li.rtddt a");
		els.stream().forEach( e -> {
			System.out.println(e.text().substring(6));
			System.out.println(uri.getScheme()+"://"+uri.getHost()+e.attr("href"));
		});
	}
	
	public void detail(){
		
	}

}
