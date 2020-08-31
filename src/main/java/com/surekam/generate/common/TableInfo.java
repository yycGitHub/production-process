package com.surekam.generate.common;

import java.util.List;
import java.util.Set;

public class TableInfo {

	private String tableName;
	private List<ColumnInfo> sourceColumnList;
	private Set<String> sourceImportList;
	private String idKey;
	private String tableIdKey;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<ColumnInfo> getSourceColumnList() {
		return sourceColumnList;
	}
	public void setSourceColumnList(List<ColumnInfo> sourceColumnList) {
		this.sourceColumnList = sourceColumnList;
	}
	public Set<String> getSourceImportList() {
		return sourceImportList;
	}
	public void setSourceImportList(Set<String> sourceImportList) {
		this.sourceImportList = sourceImportList;
	}
	public String getIdKey() {
		return idKey;
	}
	public void setIdKey(String idKey) {
		this.idKey = idKey;
	}
	public String getTableIdKey() {
		return tableIdKey;
	}
	public void setTableIdKey(String tableIdKey) {
		this.tableIdKey = tableIdKey;
	}
}

