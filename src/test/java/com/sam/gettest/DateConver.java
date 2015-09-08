package com.sam.gettest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConver {

	public static void main(String[] args) {
		Locale locale = new Locale("en");
		String datestr = "Mon,03 Aug 2015 14:38:00  +0800";
		//String datestr = "Mon, 03 Aug 2015 04:29:18 GMT";
		
		
		try {
			//System.out.println(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss  Z", locale).format(new Date()));
			System.out.println(new SimpleDateFormat("EEE,dd MMM yyyy HH:mm:ss Z", locale).parse(datestr));
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
