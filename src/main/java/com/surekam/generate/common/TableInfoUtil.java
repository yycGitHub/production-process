package com.surekam.generate.common;


public class TableInfoUtil {

	public static TableInfo getTableInfo(String tableName){
		JdbcUtil jdbcUtil = new JdbcUtil();
		jdbcUtil.getConnection();
		try {
			TableInfo tableInfo = jdbcUtil.getTableInfo(tableName);
			jdbcUtil.releaseConn();
			return tableInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		TableInfoUtil.getTableInfo("SSS");
	}
}
