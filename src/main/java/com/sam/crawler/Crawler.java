package com.sam.crawler;

import java.util.List;

import com.sam.entities.NewsEntry;

public abstract class Crawler {
	public void setSource(Object source) throws Exception{
	}
	public abstract List<NewsEntry> getNews() throws Exception;
}
