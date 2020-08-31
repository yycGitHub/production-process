package com.surekam.modules.sample.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.PrePersist;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;
import java.lang.String;
import java.util.Date;

/**
 * 图片附件表Entity
 * @author liw
 * @version 2018-10-31
 */
@Entity
@Table(name = "yx_image")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class YXImage extends XGXTEntity<YXImage> {
	
	private static final long serialVersionUID = 1L;
	private String imageId;//主键
	private String uniqueId;//唯一值
	private String ms;//描述
	private String lj;//路径
	private String ywjmc;//原文件名称
	private Date scsj;//上传时间

	public YXImage() {
		super();
	}
	
	@PrePersist
	public void prePersist(){
		this.imageId = IdGen.uuid();
	}
		
	@Id
	@Column(name="IMAGE_ID")
	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	@Column(name="UNIQUE_ID")
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getYwjmc() {
		return ywjmc;
	}

	public void setYwjmc(String ywjmc) {
		this.ywjmc = ywjmc;
	}

	public String getMs() {
		return ms;
	}

	public void setMs(String ms) {
		this.ms = ms;
	}

	public String getLj() {
		return lj;
	}

	public void setLj(String lj) {
		this.lj = lj;
	}

	public Date getScsj() {
		return scsj;
	}

	public void setScsj(Date scsj) {
		this.scsj = scsj;
	}
	
	
}


