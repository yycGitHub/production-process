/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sureserve/surekam">surekam</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.surekam.common.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.surekam.common.config.Global;
import com.surekam.common.utils.CookieUtils;

/**
 * 分页类
 * @author sureserve
 * @version 2013-7-2
 * @param <T>
 */
public class Page<T> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int pageNo = 1; // 当前页码
	private int pageSize = Integer.valueOf(Global.getConfig("page.pageSize")); // 页面大小，设置为“-1”表示不进行分页（分页无效）
	
	private long count;// 总记录数，设置为“-1”表示不查询总数
	private long pageTotal;//总页数
	
	private int first;// 首页索引
	private int last;// 尾页索引
	private int prev;// 上一页索引
	private int next;// 下一页索引
	
	private boolean firstPage;//是否是第一页
	private boolean lastPage;//是否是最后一页

	private int length = 8;// 显示页面长度
	private int slider = 1;// 前后显示页面长度
	
	private List<T> list = new ArrayList<T>();
	
	private String orderBy = ""; // 标准查询有效， 实例： updatedate desc, name asc
	
	private String funcName = "page"; // 设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用。
	
	private String message = ""; // 设置提示消息，显示在“共n条”之后

	private Map<String,Object> dataMap = new HashMap<String, Object>();
	
	private String frontPage;//前台展示页面样式分页
	
	private String draw;//DataTables请求序号。由于Ajax请求是异步的，和返回的参数draw一起用来确定序号
	
	private String searchValue;//DataTables全局搜索的值（将应用在每一个设置为可搜索的列中）
	
	
	public Page() {
		this.pageSize = -1;
	}
	
	/**
	 * 构造方法
	 * @param request 传递 repage 参数，来记住页码
	 * @param response 用于设置 Cookie，记住页码
	 */
	public Page(HttpServletRequest request, HttpServletResponse response){
		this(request, response, -2);
	}

	/**
	 * 构造方法
	 * @param request 传递 repage 参数，来记住页码
	 * @param response 用于设置 Cookie，记住页码
	 * @param defaultPageSize 默认分页大小，如果传递 -1 则为不分页，返回所有数据
	 */
	public Page(HttpServletRequest request, HttpServletResponse response, int defaultPageSize){
		// 设置页码参数（传递repage参数，来记住页码）
		String no = request.getParameter("pageNo");
		if (StringUtils.isNumeric(no)){
			CookieUtils.setCookie(response, "pageNo", no);
			this.setPageNo(Integer.parseInt(no));
		}else if (request.getParameter("repage")!=null){
			no = CookieUtils.getCookie(request, "pageNo");
			if (StringUtils.isNumeric(no)){
				this.setPageNo(Integer.parseInt(no));
			}
		}
		// 设置页面大小参数（传递repage参数，来记住页码大小）
		String size = request.getParameter("pageSize");
		if (StringUtils.isNumeric(size)){
			CookieUtils.setCookie(response, "pageSize", size);
			this.setPageSize(Integer.parseInt(size));
		}else if (request.getParameter("repage")!=null){
			no = CookieUtils.getCookie(request, "pageSize");
			if (StringUtils.isNumeric(size)){
				this.setPageSize(Integer.parseInt(size));
			}
		}
		if (defaultPageSize != -2){
			this.pageSize = defaultPageSize;
		}
		// 设置排序参数
		String orderBy = request.getParameter("orderBy");
		if (StringUtils.isNotBlank(orderBy)){
			this.setOrderBy(orderBy);
		}
	}
	
	/**
	 * DataTablesPage构造方法
	 * @param request 传递 repage 参数，来记住页码
	 */
	public Page(HttpServletRequest request){
		this.draw = request.getParameter("draw");//请求序号。由于Ajax请求是异步的，和返回的参数draw一起用来确定序号
		this.searchValue = request.getParameter("search[value]");//全局搜索的值（将应用在每一个设置为可搜索的列中）
		
    	String start = request.getParameter("start");//当前从第几页开始（默认第一页为'0'）
    	String length = request.getParameter("length");//当前页所需要的数据条数（值为'-1'时代表返回所有的数据）
    	//计算pageNo
    	int pageNo = Integer.valueOf(StringUtils.isBlank(start)?"-1":start)/Integer.valueOf(StringUtils.isBlank(length)?"-1":length);
    	this.setPageNo(pageNo+1);
    	this.setPageSize(Integer.valueOf(StringUtils.isBlank(length)?"-1":length));
	}
	
	public Page(HttpServletRequest request, HttpServletResponse response, int defaultPageSize, int type){
		// 设置页码参数（传递repage参数，来记住页码）
		String no = request.getParameter("pageNo");
		if (StringUtils.isNumeric(no)){
			CookieUtils.setCookie(response, "pageNo", no);
			this.setPageNo(Integer.parseInt(no));
		}else if (request.getParameter("repage")!=null){
			no = CookieUtils.getCookie(request, "pageNo");
			if (StringUtils.isNumeric(no)){
				this.setPageNo(Integer.parseInt(no));
			}
		}
		// 设置页面大小参数（传递repage参数，来记住页码大小）
		String size = request.getParameter("pageSize");
		if (StringUtils.isNumeric(size)){
			CookieUtils.setCookie(response, "pageSize", size);
			this.setPageSize(Integer.parseInt(size));
		}else if (request.getParameter("repage")!=null){
			no = CookieUtils.getCookie(request, "pageSize");
			if (StringUtils.isNumeric(size)){
				this.setPageSize(Integer.parseInt(size));
			}
		}
		if (defaultPageSize != -2){
			this.pageSize = defaultPageSize;
		}
		// 设置排序参数
		String orderBy = request.getParameter("orderBy");
		if (StringUtils.isNotBlank(orderBy)){
			this.setOrderBy(orderBy);
		}
	}
	
	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 */
	public Page(int pageNo, int pageSize) {
		this(pageNo, pageSize, 0);
	}
	
	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 * @param count 数据条数
	 */
	public Page(int pageNo, int pageSize, long count) {
		this(pageNo, pageSize, count, new ArrayList<T>());
	}
	
	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 * @param count 数据条数
	 * @param list 本页数据对象列表
	 */
	public Page(int pageNo, int pageSize, long count, List<T> list) {
		this.setCount(count);
		this.setPageNo(pageNo);
		this.pageSize = pageSize;
		this.setList(list);
	}
	
	/**
	 * 初始化参数
	 */
	public void initialize(){
				
		//1
		this.first = 1;
		
		this.last = (int)(count / (this.pageSize < 1 ? 20 : this.pageSize) + first - 1);
		
		if (this.count % this.pageSize != 0 || this.last == 0) {
			this.last++;
		}

		if (this.last < this.first) {
			this.last = this.first;
		}
		
		if (this.pageNo <= 1) {
			this.pageNo = this.first;
			this.firstPage=true;
		}

		if (this.pageNo >= this.last) {
			this.pageNo = this.last;
			this.lastPage=true;
		}

		if (this.pageNo < this.last - 1) {
			this.next = this.pageNo + 1;
		} else {
			this.next = this.last;
		}

		if (this.pageNo > 1) {
			this.prev = this.pageNo - 1;
		} else {
			this.prev = this.first;
		}
		
		//2
		if (this.pageNo < this.first) {// 如果当前页小于首页
			this.pageNo = this.first;
		}

		if (this.pageNo > this.last) {// 如果当前页大于尾页
			this.pageNo = this.last;
		}
		
	}
	
	/**
	 * 默认输出当前分页标签 
	 * <div class="page">${page}</div>
	 */
	@Override
	public String toString() {
		initialize();		
		StringBuilder sb = new StringBuilder();
		sb.append("<ul class=\"pagination\">");
		if (pageNo == first) {// 如果是首页
			sb.append("<li class=\"disabled\"><a class=\"songti\" href=\"javascript:\"><i class=\"ace-icon fa fa-angle-double-left\"></i></a></li>");
		} else {
			sb.append("<li class=\"\"><a href=\"javascript:\" onclick=\""+funcName+"("+prev+","+pageSize+");\"><i class=\"ace-icon fa fa-angle-double-left\"></li></a></i>\n");
		}
		int begin = pageNo - (length / 2);
		if (begin < first) {
			begin = first;
		}
		int end = begin + length - 1;
		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}
		if (begin > first) {
			int i = 0;
			for (i = first; i < first + slider && i < begin; i++) {
				sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+i+","+pageSize+");\">"
						+ (i + 1 - first) + "</a></li>\n");
			}
			if (i < begin) {
				sb.append("<li><a href=\"javascript:\">...</a></li>\n");
			}
		}
		for (int i = begin; i <= end; i++) {
			if (i == pageNo) {
				sb.append("<li class=\"active\"><a href=\"javascript:\">" + (i + 1 - first)
						+ "</a></li>\n");
			} else {
				sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+i+","+pageSize+");\">"
						+ (i + 1 - first) + "</a></li>\n");
			}
		}
		if (last - end > slider) {
			sb.append("<li><a href=\"javascript:\">...</a></li>\n");
			end = last - slider;
		}
		for (int i = end + 1; i <= last; i++) {
			sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+i+","+pageSize+");\">"
					+ (i + 1 - first) + "</a></li>\n");
		}
		if (pageNo == last) {
			sb.append("<li class=\"disabled\"><a class=\"songti\" href=\"javascript:\"><i class=\"ace-icon fa fa-angle-double-right\"></i></a></li>\n");
		} else {
			sb.append("<li><a class=\"songti\" href=\"javascript:\" onclick=\""+funcName+"("+next+","+pageSize+");\">"
					+ "<i class=\"ace-icon fa fa-angle-double-right\"></i></a></li>\n");
		}
		sb.append("<li>&nbsp;&nbsp;当前第 ");
		sb.append("<input class=\"input-small\" type=\"text\" value=\""+pageNo+"\" onkeypress=\"var e=window.event||this;var c=e.keyCode||e.which;if(c==13)");
		sb.append(funcName+"(this.value,"+pageSize+");\" onclick=\"this.select();\"/> 页 / 每页 ");
		sb.append("<input class=\"input-small\" type=\"text\" value=\""+pageSize+"\" onkeypress=\"var e=window.event||this;var c=e.keyCode||e.which;if(c==13)");
		sb.append(funcName+"("+pageNo+",this.value);\" onclick=\"this.select();\"/> 条，");
		sb.append("共 " + count + " 条"+(message!=null?message:"")+"</li>\n");
		sb.append("</ul>");
		return sb.toString();
	}
	
	
	public String getFrontPage() {
		initialize();		
		StringBuilder sb = new StringBuilder();	
		if (pageNo == first) {// 如果是首页
			sb.append("<i class=\"disabled\"><a href=\"javascript:\">&#171; 上一页</a></i>\n");
		} else {
			sb.append("<i><a href=\"javascript:\" onclick=\""+funcName+"("+prev+","+pageSize+");\">&#171; 上一页</a></i>\n");
		}
		int begin = pageNo - (length / 2);
		if (begin < first) {
			begin = first;
		}
		int end = begin + length - 1;
		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}
		if (begin > first) {
			int i = 0;
			for (i = first; i < first + slider && i < begin; i++) {
				sb.append("<i><a href=\"javascript:\" onclick=\""+funcName+"("+i+","+pageSize+");\">"
						+ (i + 1 - first) + "</a></i>\n");
			}
			if (i < begin) {
				sb.append("<i class=\"disabled\"><a href=\"javascript:\">...</a></i>\n");
			}
		}
		for (int i = begin; i <= end; i++) {
			if (i == pageNo) {
				sb.append("<i class=\"cur\"><a href=\"javascript:\">" + (i + 1 - first)
						+ "</a></i>\n");
			} else {
				sb.append("<i><a href=\"javascript:\" onclick=\""+funcName+"("+i+","+pageSize+");\">"
						+ (i + 1 - first) + "</a></i>\n");
			}
		}
		if (last - end > slider) {
			sb.append("<i class=\"disabled\"><a href=\"javascript:\">...</a></i>\n");
			end = last - slider;
		}
		for (int i = end + 1; i <= last; i++) {
			sb.append("<i><a href=\"javascript:\" onclick=\""+funcName+"("+i+","+pageSize+");\">"
					+ (i + 1 - first) + "</a></i>\n");
		}
		if (pageNo == last) {
			sb.append("<i class=\"disabled\"><a href=\"javascript:\">下一页 &#187;</a></i>\n");
		} else {
			sb.append("<i><a href=\"javascript:\" onclick=\""+funcName+"("+next+","+pageSize+");\">"
					+ "下一页 &#187;</a></i>\n");
		}
		sb.append("<i class=\"disabled controls\">");
		frontPage = sb.toString();
		return frontPage;
	}
	
	/**
	 * 获取分页HTML代码
	 * @return
	 */
	public String getHtml(){
		return toString();
	}

	/**
	 * 获取设置总数
	 * @return
	 */
	public long getCount() {
		return count;
	}

	/**
	 * 设置数据总数
	 * @param count
	 */
	public void setCount(long count) {
		this.count = count;
		if (pageSize >= count){
			pageNo = 1;
		}
	}
	
	/**
	 * 获取当前页码
	 * @return
	 */
	public int getPageNo() {
		return pageNo;
	}
	
	/**
	 * 设置当前页码
	 * @param pageNo
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	/**
	 * 获取页面大小
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置页面大小（最大500）
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize <= 0 ? 10 : pageSize;// > 500 ? 500 : pageSize;
	}

	/**
	 * 首页索引
	 * @return
	 */
	@JsonIgnore
	public int getFirst() {
		return first;
	}

	/**
	 * 尾页索引
	 * @return
	 */
	@JsonIgnore
	public int getLast() {
		return last;
	}
	
	/**
	 * 获取页面总数
	 * @return getLast();
	 */
	@JsonIgnore
	public int getTotalPage() {
		return getLast();
	}

	/**
	 * 是否为第一页
	 * @return
	 */
	@JsonIgnore
	public boolean isFirstPage() {
		return firstPage;
	}

	/**
	 * 是否为最后一页
	 * @return
	 */
	@JsonIgnore
	public boolean isLastPage() {
		return lastPage;
	}
	
	/**
	 * 上一页索引值
	 * @return
	 */
	@JsonIgnore
	public int getPrev() {
		if (isFirstPage()) {
			return pageNo;
		} else {
			return pageNo - 1;
		}
	}

	/**
	 * 下一页索引值
	 * @return
	 */
	@JsonIgnore
	public int getNext() {
		if (isLastPage()) {
			return pageNo;
		} else {
			return pageNo + 1;
		}
	}
	
	/**
	 * 获取本页数据对象列表
	 * @return List<T>
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * 设置本页数据对象列表
	 * @param list
	 */
	public Page<T> setList(List<T> list) {
		this.list = list;
		return this;
	}

	/**
	 * 获取查询排序字符串
	 * @return
	 */
	@JsonIgnore
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置查询排序，标准查询有效， 实例： updatedate desc, name asc
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 获取点击页码调用的js函数名称
	 * function ${page.funcName}(pageNo){location="${ctx}/list-${category.id}${urlSuffix}?pageNo="+i;}
	 * @return
	 */
	@JsonIgnore
	public String getFuncName() {
		return funcName;
	}

	/**
	 * 设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用。
	 * @param funcName 默认为page
	 */
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	/**
	 * 设置提示消息，显示在“共n条”之后
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * 分页是否有效
	 * @return this.pageSize==-1
	 */
	@JsonIgnore
	public boolean isDisabled() {
		return this.pageSize==-1;
	}
	
	/**
	 * 是否进行总数统计
	 * @return this.count==-1
	 */
	@JsonIgnore
	public boolean isNotCount() {
		return this.count==-1;
	}
	
	/**
	 * 获取 Hibernate FirstResult
	 */
	public int getFirstResult(){
		int firstResult = (getPageNo() - 1) * getPageSize();
		if (firstResult >= getCount()) {
			firstResult = 0;
		}
		return firstResult;
	}
	
	public int getLastResult(){
		int lastResult = getFirstResult()+getMaxResults();
		if(lastResult>getCount()) {
			lastResult =(int) getCount();
		}
		return lastResult;
	}
	/**
	 * 获取 Hibernate MaxResults
	 */
	public int getMaxResults(){
		return getPageSize();
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public long getPageTotal() {
		if(count % pageSize == 0) {
			pageTotal = count / pageSize;
		} else {
			pageTotal = count / pageSize + 1;
		}
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}
	
	public String getDraw() {
		return draw;
	}

	public void setDraw(String draw) {
		this.draw = draw;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	
}
