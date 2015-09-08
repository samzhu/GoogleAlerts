package com.sam.gettest;

import java.io.IOException;

import org.jdom2.JDOMException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class GetAppleNews {
	
	private String url = "http://www.appledaily.com.tw/appledaily/article/finance/20150731/36695691//";

	public static void main(String[] args) {
		GetAppleNews main = new GetAppleNews();
		try {
			main.precess();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void precess() throws IOException, JDOMException{
		Document doc = this.httpget(url);
		this.parse(doc);
	}
	
	public Document httpget(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		return doc;
	}
	
	public void parse(Document doc) throws JDOMException, IOException{
		//System.out.println(doc.select("header hgroup:eq(1) h1").text());
		//System.out.println(doc.select("header hgroup:eq(1) h2").text());
		//System.out.println(doc.select("div.articulum p,div.articulum h2").text());
	}
}
