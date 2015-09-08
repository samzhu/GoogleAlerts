package com.sam.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameMappingStrategy;

public class Opencsv {

	public static <T> List<T> parseFileToBeans(final String filepath, final char fieldDelimiter, final Class<T> beanClass) throws IOException{
		//改用NIO方式讀檔
		Path file = Paths.get(filepath);
		BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8);
		//傳統讀檔方式
		//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), StandardCharsets.UTF_8));
		List<T> relist = parseBufferedReaderToBeans(br, fieldDelimiter, beanClass);
		br.close();
		return relist;
	}

	public static <T> List<T> parseResourceToBeans(final String filepath, final char fieldDelimiter, final Class<T> beanClass) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(ClassLoader.class.getResourceAsStream(filepath), "UTF-8"));
		List<T> relist = parseBufferedReaderToBeans(br, fieldDelimiter, beanClass);
		br.close();
		return relist;
	}

	public static <T> List<T> parseBufferedReaderToBeans(final BufferedReader br, final char fieldDelimiter, final Class<T> beanClass){
		CSVReader reader = null;
		List<T> relist = null;
		try {
			reader = new CSVReader(br, fieldDelimiter);
			final HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<T>();
			strategy.setType(beanClass);
			final CsvToBean<T> csv = new CsvToBean<T>();
			relist = csv.parse(strategy, reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return relist;
	}
}