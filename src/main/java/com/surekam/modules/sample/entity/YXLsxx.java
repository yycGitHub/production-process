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

/**
 * 历史信息表Entity
 * @author liw
 * @version 2018-10-31
 */
@Entity
@Table(name = "yx_lsxx")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class YXLsxx extends XGXTEntity<YXLsxx> {
	
	private static final long serialVersionUID = 1L;
	private String lsxxId;//主键
	private String uniqueId;//唯一值
	private String userid;//用户ID
	private String imageId;//图片主键

	public YXLsxx() {
		super();
	}
	
	@PrePersist
	public void prePersist(){
		this.lsxxId = IdGen.uuid();
	}
		
	@Id
	@Column(name="LSXX_ID")
	public String getLsxxId() {
		return lsxxId;
	}

	public void setLsxxId(String lsxxId) {
		this.lsxxId = lsxxId;
	}

	@Column(name="UNIQUE_ID")
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Column(name="IMAGE_ID")
	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
}


