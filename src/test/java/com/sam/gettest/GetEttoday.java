package com.sam.gettest;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.sam.entities.NewsEntry;
import com.sam.utils.Http;

public class GetEttoday {

	public static void main(String[] args) {
		String url = "http://feedproxy.google.com/~r/ettoday/finance/~3/Midk_Pg401c/544353.htm";
		try {
			Document doc = Jsoup.connect(url).get();
			
			
			
			String content = doc.select("div.story p").text();
			
			System.out.println(content);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getNewsDetail(NewsEntry newsEntry){
		try {
			org.jsoup.nodes.Document doc = Http.getDoc(newsEntry.getReallink());
			String content = doc.select("div.articulum p,div.articulum h2").text();
			newsEntry.setContent(content);
			//System.out.println(doc.select("header hgroup:eq(1) h1").text());
			//System.out.println(doc.select("header hgroup:eq(1) h2").text());
			//System.out.println(doc.select("div.articulum p,div.articulum h2").text());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
