package com.sam.gettest;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class GetAlert {

	public static void main(String[] args) {
		try {
			String data = GetAlert.run("https://www.google.com.tw/alerts/feeds/16331558652310120697/5421639392935882022");
			GetAlert.parse(data);






		} catch (IOException | JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public static String run(String url) throws IOException {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
		.url(url)
		.build();

		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	public static void parse(String data) throws JDOMException, IOException{
		Namespace ns = Namespace.getNamespace("", "http://www.w3.org/2005/Atom");
		SAXBuilder saxBuilder = new SAXBuilder();

		//Document doc = saxBuilder.build(new File("D://xmltest.txt"));
		Document doc = saxBuilder.build(new StringReader(data));
		Element root = doc.getRootElement();

		System.out.println(root.getChildText("title", ns));
		
		List<Element> children = root.getChildren("entry", ns);
		for(Element el : children){
			System.out.println(el.getChildText("title", ns));
			System.out.println(el.getChildText("content", ns));
			
		}
		
		
	}

}
