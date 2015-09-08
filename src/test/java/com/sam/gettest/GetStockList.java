package com.sam.gettest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sam.data.TwseStock;

public class GetStockList {
	private String url = "http://isin.twse.com.tw/isin/C_public.jsp?strMode=2";

	public static void main(String[] args) {
		GetStockList main = new GetStockList();
		main.precess();
	}

	public void precess(){
		List<TwseStock> stocklist = new ArrayList();
		try {
			Document doc = Jsoup.connect(url).get();
			Elements els = doc.select("table tr");

			//			for(int i = 0 ; i < els.size() ; i++){
			//				Element el = els.get(i);
			//				System.out.println( el.childNodeSize() );
			//				System.out.println( el.html() );
			//				//System.out.println( el.child(0).text() );
			//				//System.out.println( el.child(0).text().split("　").length );
			//				
			//				if(el.childNodeSize() == 0){
			//					break;
			//				}
			//				
			//			}

			els.stream()
			//.filter( e -> e.text().indexOf("有價證券代號及名稱") == -1)
			.filter( e -> e.childNodeSize() == 7)
			.filter( e -> e.child(0).text().split("　").length == 2)
			.filter( e -> e.child(0).text().split("　")[0].trim().length() == 4)
			.forEach(e -> {

				try {
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
				} catch (Exception e1) {
					System.out.println(e);
				}
			});
			System.out.println(stocklist.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
