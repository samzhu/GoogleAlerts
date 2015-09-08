package com.sam.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Document(indexName = "alert", type = "news", shards = 10, replicas = 0, refreshInterval = "-1")
public class NewsEntry {
	@Id
	@Field(type= FieldType.String, index = FieldIndex.not_analyzed)
	private String id;
	// 原始識別代碼
	@Field(type= FieldType.String, index = FieldIndex.not_analyzed)
	private String idorg;
	// 股票代號
	@Field(type= FieldType.String, index = FieldIndex.not_analyzed)
	private List<String> stockid;
	// 股票名稱
	@Field(type= FieldType.String, index = FieldIndex.not_analyzed)
	private List<String> stockname;
	@Field(type= FieldType.String, index = FieldIndex.analyzed)
	private String title;
	// 原始連結(含google跳轉頁)
	@Field(type= FieldType.String, index = FieldIndex.not_analyzed)
	private String link;
	@Field(type= FieldType.String, index = FieldIndex.analyzed)
	private String content;
	@Field(type= FieldType.String, index = FieldIndex.analyzed)
	private String allcontent;
	// 來源網站
	@Field(type= FieldType.String, index = FieldIndex.not_analyzed)
	private String portal;
	// 真實連結
	@Field(type= FieldType.String, index = FieldIndex.not_analyzed)
	private String reallink;

	// 發佈時間
	@JSONField(format = "yyyyMMdd'T'HHmmss.SSS'Z'")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HHmmss.SSS'Z'")
	@Field(type = FieldType.Date, format = DateFormat.basic_date_time, index = FieldIndex.not_analyzed)
	private Date published;

	public void addStockid(String id){
		if(stockid == null)
			stockid = new ArrayList<String>();
		if(!stockid.contains(id))
			stockid.add(id);

	}

	public void addStockname(String name){
		if(stockname == null)
			stockname = new ArrayList<String>();
		if(!stockname.contains(name))
			stockname.add(name);
	}
}
