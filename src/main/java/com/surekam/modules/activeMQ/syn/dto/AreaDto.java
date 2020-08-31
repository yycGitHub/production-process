package com.surekam.modules.activeMQ.syn.dto;


public class AreaDto  {

	private String kuid;
	private String parentId;	// 父级编号
	private String parentIds; // 所有父级编号
	private String code; 	// 区域编码
	private String name; 	// 区域名称
	private String type; 	// 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
	
	public String getKuid() {
		return kuid;
	}
	public void setKuid(String kuid) {
		this.kuid = kuid;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}