package com.kindp.third_infterface.shortMsg.guodu.vo;

public enum SendCode {
	SingleSuccess("03");
	private String value;
	
	private SendCode(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	
}

