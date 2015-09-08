package com.sam.gettest;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import com.sam.data.StockAlert;
import com.sam.entities.NewsEntry;
import com.sam.utils.Opencsv;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class TestGetAlert {

	public static void main(String[] args) {
		try {
			List<StockAlert> list = Opencsv.parseFileToBeans("D:/stockalert.txt", ',', StockAlert.class);
			for(StockAlert alert:list){
				String data = httpget(alert.getUrl());
				parse(data);


				//System.out.println(String.format("id=%s , name=%s , url=%s", alert.getId(), alert.getName(), alert.getUrl()));




			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String httpget(String url) throws IOException {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
		.url(url)
		.build();

		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	public static String httpgetRealLink(String url) throws IOException {
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

	public static void parse(String data) throws JDOMException, IOException{
		Namespace ns = Namespace.getNamespace("", "http://www.w3.org/2005/Atom");
		SAXBuilder saxBuilder = new SAXBuilder();

		Document doc = saxBuilder.build(new StringReader(data));
		Element root = doc.getRootElement();

		//System.out.println(root.getChildText("title", ns));

		List<Element> children = root.getChildren("entry", ns);
		for(Element el : children){
			NewsEntry entry = new NewsEntry();
			entry.setId(el.getChildText("id", ns));
			entry.setLink(el.getChild("link", ns).getAttributeValue("href"));
			entry.setTitle(Jsoup.clean(el.getChildText("title", ns), Whitelist.none()));
			entry.setContent(Jsoup.clean(el.getChildText("content", ns), Whitelist.none()).replace("&nbsp;", ""));
			String reallink = httpgetRealLink(entry.getLink());
			entry.setReallink(reallink);
		}
	}
}
