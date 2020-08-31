package com.surekam.generate.common;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.surekam.common.config.Global;

public class JdbcUtil {

	private Properties props = new Properties();
	private Connection connection;
	private PreparedStatement pstmt;
	private ResultSet resultSet;

	public JdbcUtil() {
		
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return 数据库连接
	 */
	public Connection getConnection() {
		try {
			String username = Global.getConfig("jdbc.username");
			String password = Global.getConfig("jdbc.password");
			String driver= Global.getConfig("jdbc.driver");
			String url = Global.getConfig("jdbc.url");
			props.put("user", username);
			props.put("password", password);
			props.put("driver", driver);
			props.put("url", url);
			props.put("remarksReporting","true");
			Class.forName(driver); // 注册驱动
			connection = DriverManager.getConnection(url,props); // 获取连接
		} catch (Exception e) {
			throw new RuntimeException("get connection error!", e);
		}
		return connection;
	}

	/**
	 * 执行更新操作
	 * 
	 * @param sql
	 *            sql语句
	 * @param params
	 *            执行参数
	 * @return 执行结果
	 * @throws SQLException
	 */
	public boolean updateByPreparedStatement(String sql, List<?> params)
			throws SQLException {
		boolean flag = false;
		int result = -1;// 表示当用户执行添加删除和修改的时候所影响数据库的行数
		pstmt = connection.prepareStatement(sql);
		int index = 1;
		// 填充sql语句中的占位符
		if (params != null && !params.isEmpty()) {
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(index++, params.get(i));
			}
		}
		result = pstmt.executeUpdate();
		flag = result > 0 ? true : false;
		return flag;
	}

	/**
	 * 执行查询操作
	 * 
	 * @param sql
	 *            sql语句
	 * @param params
	 *            执行参数
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> findResult(String sql, List<?> params)
			throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int index = 1;
		pstmt = connection.prepareStatement(sql);
		if (params != null && !params.isEmpty()) {
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(index++, params.get(i));
			}
		}
		resultSet = pstmt.executeQuery();
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols_len = metaData.getColumnCount();
		while (resultSet.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < cols_len; i++) {
				String cols_name = metaData.getColumnName(i + 1);
				Object cols_value = resultSet.getObject(cols_name);
				if (cols_value == null) {
					cols_value = "";
				}
				map.put(cols_name, cols_value);
			}
			list.add(map);
		}
		return list;
	}
	
	public TableInfo getTableInfo(String tableName) {
		try {
			TableInfo tableInfo = new TableInfo();
			tableInfo.setTableName(tableName);
			
			DatabaseMetaData dbMeta = connection.getMetaData();
			ResultSet rst = dbMeta.getPrimaryKeys(null, null,tableName);
			String primaryKey = "";
            if (!rst.isAfterLast()) {
                while(rst.next()){
                	primaryKey = rst.getString("COLUMN_NAME");
                	break;
                }
            }
            tableInfo.setIdKey(lineToHump(primaryKey));
            tableInfo.setTableIdKey(primaryKey);
            
			List<ColumnInfo> sourceColumnList = new ArrayList<ColumnInfo>();
			Set<String> sourceImportList = new HashSet<String>();			
            ResultSet rs = dbMeta.getColumns(null, getSchema(connection),tableName, "%");
            while(rs.next()){
				ColumnInfo columnInfo = new ColumnInfo();
            	String columnName = rs.getString("COLUMN_NAME");  
                String columnClassName = rs.getString("TYPE_NAME");
                String logicalName = rs.getString("REMARKS");
				if(unUserXGXTEntity(lineToHump(columnName))){
	                columnInfo.setPhysicalInstance(columnName);
					columnInfo.setInstance(lineToHump(columnName));				
					columnInfo.setJavaClass(columnClassName);
					columnInfo.setLogicalName(logicalName);
					typeProcessor(columnInfo,sourceImportList,columnClassName);
					sourceColumnList.add(columnInfo);
				}
            }
			tableInfo.setSourceColumnList(sourceColumnList);
			tableInfo.setSourceImportList(sourceImportList);
			return tableInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
    private String getSchema(Connection conn) throws Exception {  
        String schema;  
        schema = conn.getMetaData().getUserName();  
        if ((schema == null) || (schema.length() == 0)) {  
            throw new Exception("ORACLE数据库模式不允许为空");  
        }  
        return schema.toUpperCase().toString();
    }
	
    public String lineToHump(String str){  
    	Pattern linePattern = Pattern.compile("_(\\w)");
    	str = str.toLowerCase();  
        Matcher matcher = linePattern.matcher(str);  
        StringBuffer sb = new StringBuffer();  
        while(matcher.find()){  
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());  
        }  
        matcher.appendTail(sb); 
        return sb.toString();  
    }
	
    public void typeProcessor(ColumnInfo columnInfo,Set<String> sourceImportList,String columnClassName) { 
    	if (columnClassName.equalsIgnoreCase("CLOB")) {
        	columnInfo.setJavaClass("String");
        	columnInfo.setPhysicalName("CLOB");
        	sourceImportList.add("java.lang.String");
        	sourceImportList.add("javax.persistence.FetchType");
        	sourceImportList.add("javax.persistence.Basic");
        	sourceImportList.add("javax.persistence.Column");
        	sourceImportList.add("javax.persistence.Lob");
        }
        else if (columnClassName.equalsIgnoreCase("BLOB")) {
        	columnInfo.setJavaClass("byte[]");
        	columnInfo.setPhysicalName("BLOB");
        	sourceImportList.add("javax.persistence.FetchType");
        	sourceImportList.add("javax.persistence.Basic");
        	sourceImportList.add("javax.persistence.Column");
        	sourceImportList.add("javax.persistence.Lob");
        	sourceImportList.add("javax.persistence.Transient");
        }
        else if (columnClassName.equalsIgnoreCase("NUMBER")||columnClassName.equalsIgnoreCase("LONG")) {
        	columnInfo.setJavaClass("BigDecimal");
        	columnInfo.setPhysicalName("BigDecimal");
        	sourceImportList.add("java.math.BigDecimal");
        }
        else if (columnClassName.equalsIgnoreCase("FLOAT")) {
        	columnInfo.setJavaClass("Double");
        	columnInfo.setPhysicalName("Double");
        	sourceImportList.add("java.lang.Double");
        }
        else if (columnClassName.equalsIgnoreCase("DATE")) {
        	columnInfo.setJavaClass("Date");
        	columnInfo.setPhysicalName("Date");
        	sourceImportList.add("java.util.Date");
        }else{
    		columnInfo.setJavaClass("String");
    		columnInfo.setPhysicalName("String");
    		sourceImportList.add("java.lang.String");
        }
    }

    public enum XGXTNames { 
    	gxbz,zhxgsj,xxid  
    }
    
    public boolean unUserXGXTEntity(String columnName){
    	 for (XGXTNames e : XGXTNames.values()) {
             if(e.toString().equals(columnName)){
            	 return false; 
             }
         }
        return true;
    }
    
	/**
	 * 释放资源
	 */
	public void releaseConn() {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}