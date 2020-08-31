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
 * 状态表Entity
 * @author liw
 * @version 2018-10-31
 */
@Entity
@Table(name = "yx_ztb")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class YXZtb extends XGXTEntity<YXZtb> {
	
	private static final long serialVersionUID = 1L;
	private String ztbId;//主键
	private String uniqueId;//唯一值
	private String userid;//用户ID
	private String zt;//状态（1-开启，0-关闭）

	public YXZtb() {
		super();
	}
	
	@PrePersist
	public void prePersist(){
		this.ztbId = IdGen.uuid();
	}
		
	@Id
	@Column(name="ZTB_ID")
	public String getZtbId() {
		return ztbId;
	}

	public void setZtbId(String ztbId) {
		this.ztbId = ztbId;
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

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}
	
}


