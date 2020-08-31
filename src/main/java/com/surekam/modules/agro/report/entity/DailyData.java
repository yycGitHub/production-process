package com.surekam.modules.agro.report.entity;

import java.util.ArrayList;
import java.util.List;

public class DailyData <T>{

	private String date;
	private String countNUm;
	private List<T> dailyDatas = new ArrayList<T>();
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCountNUm() {
		return countNUm;
	}
	public void setCountNUm(String countNUm) {
		this.countNUm = countNUm;
	}
	public List<T> getDailyDatas() {
		return dailyDatas;
	}
	public void setDailyDatas(List<T> dailyDatas) {
		this.dailyDatas = dailyDatas;
	}
}
