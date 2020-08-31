package com.surekam.common.utils;

/** 
 * <p>通用枚举类</p>
 * @author liuyi 
 * @date 2019年9月25日 下午10:17:25 
 */
public enum CommonEnum {
	
	NORMALTEXT(1, "text"),
	HTMLTEXT(2, "html"),
	IMAGE(3, "img"),
	ALBUM(4,"album"),
	TIMEWIDGET(5,"time"),
	LINK(6,"link"),
	RICHTXT(7,"richText"),
	
	NUMBER(11,"number");

    private Integer code;
    private String message;

    CommonEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}  
}
