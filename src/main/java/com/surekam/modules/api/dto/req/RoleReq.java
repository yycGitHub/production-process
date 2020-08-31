package com.surekam.modules.api.dto.req;

import java.io.Serializable;
import java.util.List;

public class RoleReq implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private String id;
	private String officeId;
	private String name;
	private String dataScope;
	private String bookName;
	private List<String> checkedMenuData;
	private List<String> checkedOfficeData;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataScope() {
		return dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}

	public List<String> getCheckedMenuData() {
		return checkedMenuData;
	}

	public void setCheckedMenuData(List<String> checkedMenuData) {
		this.checkedMenuData = checkedMenuData;
	}

	public List<String> getCheckedOfficeData() {
		return checkedOfficeData;
	}

	public void setCheckedOfficeData(List<String> checkedOfficeData) {
		this.checkedOfficeData = checkedOfficeData;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

}
