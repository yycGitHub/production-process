package com.surekam.modules.agro.label.entity;

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
 * 标签表Entity
 * @author liwei
 * @version 2019-04-27
 */
@Entity
@Table(name = "t_agro_label")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Label extends XGXTEntity<Label> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String userId;//用户ID（user_id=0表示公共的标签）
	private String labelType;//标签类型（1-不执行原因标签）
	private String labelContent;//标签内容

	public Label() {
		super();
	}
	
	@PrePersist
	public void prePersist(){
		this.id = IdGen.uuid();
	}
		
	@Id
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUserId() {
		return this.userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getLabelType() {
		return this.labelType;
	}
	public void setLabelType(String labelType) {
		this.labelType = labelType;
	}
	
	public String getLabelContent() {
		return this.labelContent;
	}
	public void setLabelContent(String labelContent) {
		this.labelContent = labelContent;
	}
	
}


