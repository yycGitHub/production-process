package com.surekam.modules.agro.report.entity;

import java.util.HashMap;
import java.util.Map;

public class ReportData<T> {

	private String columnCode;
	private String columnName;
	private Map<String,T> datas = new HashMap<String,T>();
	
	public String getColumnCode() {
		return columnCode;
	}
	public void setColumnCode(String columnCode) {
		this.columnCode = columnCode;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Map<String, T> getDatas() {
		return datas;
	}
	public void setDatas(Map<String, T> datas) {
		this.datas = datas;
	}
}
