package com.surekam.modules.agro.addressbook.entity;

public class Profile {
	private String Tag;//指定要设置的资料字段的名称，支持设置的资料字段有：Tag_Profile_IM_Nick,Tag_Profile_IM_Image
	private String Value;//待设置的资料字段的值
	public String getTag() {
		return Tag;
	}
	public void setTag(String tag) {
		Tag = tag;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	
}
