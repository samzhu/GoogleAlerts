package com.sam.gettest;

import java.io.IOException;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class TestGet {

	public static void main(String[] args) {
		String url = "https://tw.stock.yahoo.com/news_content/url/d/a/20150803/%E5%85%A8%E5%8F%B0%E4%B8%8A%E5%8D%8A%E5%B9%B4%E5%90%88%E4%BD%B5%E7%8D%B2%E5%88%A91-01%E5%84%84%E5%85%83-%E6%AF%8F%E8%82%A1%E7%A8%85%E5%BE%8C0-46%E5%85%83-085126047.html";
		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
			.url(url)
			.build();

			Response response = client.newCall(request).execute();
			System.out.println( response.body().string());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
