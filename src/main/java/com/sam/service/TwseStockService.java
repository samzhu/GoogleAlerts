package com.sam.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.sam.data.TwseStock;

@Service
public class TwseStockService {
	private String url = "http://isin.twse.com.tw/isin/C_public.jsp?strMode=2";
	
	public List<TwseStock> getTwseStock() throws IOException{
		List<TwseStock> stocklist = new ArrayList();
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements els = doc.select("table tr");
			els.stream()
			.filter( e -> e.childNodeSize() == 7)
			.filter( e -> e.child(0).text().split("　").length == 2)
			.filter( e -> e.child(0).text().split("　")[0].trim().length() == 4)
			.forEach(e -> {
				TwseStock stock = new TwseStock();
				String[] temp = e.child(0).text().split("　");
				stock.setId(temp[0].trim());
				stock.setName(temp[1].trim());
				stock.setIsincode(e.child(1).text());
				stock.setOfferingday(e.child(2).text());
				stock.setMarket(e.child(3).text());
				stock.setIndustry(e.child(4).text());
				stock.setCficode(e.child(5).text());
				stocklist.add(stock);
			});
			System.out.println("getTwseStock size=" + stocklist.size());
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		return stocklist;
	}
}
