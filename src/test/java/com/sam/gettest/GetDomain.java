package com.sam.gettest;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class GetDomain {

	public static void main(String[] args) {
		String url = "http://dshfy.com/p/shfy-528198-[%E5%88%9D%E4%B8%8A%E5%B8%82]%E5%AE%87%E7%9E%BB%E7%A7%91%E6%8A%80%E8%82%A1%E4%BB%BD%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8(%E8%82%A1%E7%A5%A8%E4%BB%A3%E8%99%9F%EF%BC%9A8271).html";
		URI uri;
		try {
			//uri = new URI(url);

			URL aURL = new URL(url);

			System.out.println("protocol = " + aURL.getProtocol());
			System.out.println("authority = " + aURL.getAuthority());
			System.out.println("host = " + aURL.getHost());
			System.out.println("port = " + aURL.getPort());
			System.out.println("path = " + aURL.getPath());
			System.out.println("query = " + aURL.getQuery());
			System.out.println("filename = " + aURL.getFile());
			System.out.println("ref = " + aURL.getRef());


			String domain = aURL.getHost();
			System.out.println( domain.startsWith("www.") ? domain.substring(4) : domain);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
