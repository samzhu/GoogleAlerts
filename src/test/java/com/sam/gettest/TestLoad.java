package com.sam.gettest;

import java.io.IOException;
import java.util.List;

import com.sam.data.StockAlert;
import com.sam.utils.Opencsv;

public class TestLoad {

	public static void main(String[] args) {
		try {
			List<StockAlert> list = Opencsv.parseFileToBeans("D:/stockalert.txt", ',', StockAlert.class);
			for(StockAlert alert:list){
				System.out.println(String.format("id=%s , name=%s , url=%s", alert.getId(), alert.getName(), alert.getUrl()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
