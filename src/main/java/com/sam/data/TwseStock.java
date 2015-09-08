package com.sam.data;

import lombok.Data;

@Data
public class TwseStock {
	private String id;
	private String name;
	private String isincode;
	private String offeringday;
	private String market;
	private String industry;
	private String cficode;
}