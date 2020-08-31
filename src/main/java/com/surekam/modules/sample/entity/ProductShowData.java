package com.surekam.modules.sample.entity;

public class ProductShowData {
	//年份
	private String year;
	//产品数量
	private String productCount;
	//总标签数
	private String labelCount;
	//周平均扫码次数
	private String weekTraceCount;
    //月平均扫码次数
	private String monthTraceCount;
    //总扫码次数
	private String traceCount;
	
	public ProductShowData() {
		super();
	}
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getProductCount() {
		return productCount;
	}
	public String getLabelCount() {
		return labelCount;
	}
	public void setLabelCount(String labelCount) {
		this.labelCount = labelCount;
	}
	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}
	public String getWeekTraceCount() {
		return weekTraceCount;
	}
	public void setWeekTraceCount(String weekTraceCount) {
		this.weekTraceCount = weekTraceCount;
	}
	public String getMonthTraceCount() {
		return monthTraceCount;
	}
	public void setMonthTraceCount(String monthTraceCount) {
		this.monthTraceCount = monthTraceCount;
	}
	public String getTraceCount() {
		return traceCount;
	}
	public void setTraceCount(String traceCount) {
		this.traceCount = traceCount;
	}
	
}
