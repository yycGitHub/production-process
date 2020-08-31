package com.surekam.modules.api.entity.print;

/**
* Created by wangyuewen on 2018年12月7日
*/

/**
 * 文字
 * @author wangyuewen
 *
 */
public class FontElement {
	//类型
	private Integer type = 1;
	//整数类型，文字X方向起始点，以点表示。  
	private Double xStartPoint;
    //整数类型，文字Y方向起始点，以点表示。
	private Double yStartPoint;
    //整数类型，字体高度，以点表示。
	private Double fontHeight = 2.54;
    //整数类型，旋转角度，逆时针方向旋转。0-旋转0°，90-旋转90°，180-旋转180°，270-旋转270°。  
	private Integer rotation = 0;
    //整数类型，字体外形。0：标准；1：斜体；2：粗体；3：粗斜体。 
	private Integer fontShape = 0;
    //整数类型，下划线，0：无下划线；1：加下划线。
	private Integer underline = 0;
    //字符串类型，字体名称。如：Arial，Times new Roman。  
	private String fontName = "黑体";
    //字符串类型，打印文字内容。  
	private String content;//打印内容
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Double getxStartPoint() {
		return xStartPoint;
	}
	public void setxStartPoint(Double xStartPoint) {
		this.xStartPoint = xStartPoint;
	}
	public Double getyStartPoint() {
		return yStartPoint;
	}
	public void setyStartPoint(Double yStartPoint) {
		this.yStartPoint = yStartPoint;
	}
	public Double getFontHeight() {
		return fontHeight;
	}
	public void setFontHeight(Double fontHeight) {
		this.fontHeight = fontHeight;
	}
	public Integer getRotation() {
		return rotation;
	}
	public void setRotation(Integer rotation) {
		this.rotation = rotation;
	}
	public Integer getFontShape() {
		return fontShape;
	}
	public void setFontShape(Integer fontShape) {
		this.fontShape = fontShape;
	}
	public Integer getUnderline() {
		return underline;
	}
	public void setUnderline(Integer underline) {
		this.underline = underline;
	}
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
}


