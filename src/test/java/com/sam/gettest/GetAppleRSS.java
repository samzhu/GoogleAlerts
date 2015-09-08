package com.sam.gettest;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class GetAppleRSS {
	private String url = "http://www.appledaily.com.tw/rss/newcreate/kind/sec/type/8";
	private Locale locale = new Locale("en");

	public static void main(String[] args) {
		GetAppleRSS main = new GetAppleRSS();
		try {
			main.precess();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void precess() throws IOException, JDOMException{
		String data = this.httpget(url);
		this.parse(data);
	}


	public String httpget(String url) throws IOException {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
		.url(url)
		.build();

		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	public void parse(String data) throws JDOMException, IOException{
		Namespace ns = Namespace.getNamespace("atom", "http://www.w3.org/2005/Atom");
		SAXBuilder saxBuilder = new SAXBuilder();

		//Document doc = saxBuilder.build(new File("D://xmltest.txt"));
		Document doc = saxBuilder.build(new StringReader(data));
		Element root = doc.getRootElement();

		//System.out.println(root.getChild("channel").getChildren("item"));

		List<Element> children = root.getChild("channel").getChildren("item");
		for(Element el : children){

			try {
				System.out.println(el.getChildText("title"));
				System.out.println(el.getChildText("pubDate"));
				System.out.println(el.getChildText("link").substring(0, el.getChildText("link").length()-1));

				//System.out.println(Jsoup.clean(el.getChildText("description"), Whitelist.none()));
				System.out.println(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", locale).parse(el.getChildText("pubDate")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
