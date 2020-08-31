package com.surekam.modules.api.entity.print;

/**
* 标签
* Created by wangyuewen on 2018年12月7日
*/
/**
 * 标签
 * @author wangyuewen
 *
 */
public class Label {
	 private Integer labelWidth = 80;
     private Integer labelHeight = 40;
     private Integer labelNumber = 1;
     private Double labelLeftmargin = 1.5;
     private Integer labelHorizontalInterval = 0;
     private Integer labelVerticalInterval = 2;
     private String materialName = "亮白PET";
     private Integer maximumPrintSpeed = 4;
     private Integer printingConcentration = 15;
	public Integer getLabelWidth() {
		return labelWidth;
	}
	public void setLabelWidth(Integer labelWidth) {
		this.labelWidth = labelWidth;
	}
	public Integer getLabelHeight() {
		return labelHeight;
	}
	public void setLabelHeight(Integer labelHeight) {
		this.labelHeight = labelHeight;
	}
	public Integer getLabelNumber() {
		return labelNumber;
	}
	public void setLabelNumber(Integer labelNumber) {
		this.labelNumber = labelNumber;
	}
	public Double getLabelLeftmargin() {
		return labelLeftmargin;
	}
	public void setLabelLeftmargin(Double labelLeftmargin) {
		this.labelLeftmargin = labelLeftmargin;
	}
	public Integer getLabelHorizontalInterval() {
		return labelHorizontalInterval;
	}
	public void setLabelHorizontalInterval(Integer labelHorizontalInterval) {
		this.labelHorizontalInterval = labelHorizontalInterval;
	}
	public Integer getLabelVerticalInterval() {
		return labelVerticalInterval;
	}
	public void setLabelVerticalInterval(Integer labelVerticalInterval) {
		this.labelVerticalInterval = labelVerticalInterval;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public Integer getMaximumPrintSpeed() {
		return maximumPrintSpeed;
	}
	public void setMaximumPrintSpeed(Integer maximumPrintSpeed) {
		this.maximumPrintSpeed = maximumPrintSpeed;
	}
	public Integer getPrintingConcentration() {
		return printingConcentration;
	}
	public void setPrintingConcentration(Integer printingConcentration) {
		this.printingConcentration = printingConcentration;
	}
     
}


