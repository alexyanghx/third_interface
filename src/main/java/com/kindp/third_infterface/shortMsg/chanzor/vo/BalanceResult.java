package com.kindp.third_infterface.shortMsg.chanzor.vo;

public class BalanceResult {
	private String returnstatus;
	private String message;
	private String payinfo;
	private String overage;
	private String sendTotal;
	private String errorstatus;

	public String getErrorstatus() {
		return errorstatus;
	}

	public void setErrorstatus(String errorstatus) {
		this.errorstatus = errorstatus;
	}

	public String getReturnstatus() {
		return returnstatus;
	}

	public void setReturnstatus(String returnstatus) {
		this.returnstatus = returnstatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPayinfo() {
		return payinfo;
	}

	public void setPayinfo(String payinfo) {
		this.payinfo = payinfo;
	}

	public String getOverage() {
		return overage;
	}

	public void setOverage(String overage) {
		this.overage = overage;
	}

	public String getSendTotal() {
		return sendTotal;
	}

	public void setSendTotal(String sendTotal) {
		this.sendTotal = sendTotal;
	}

}
