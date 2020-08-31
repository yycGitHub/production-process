package com.surekam.modules.agro.batchplan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.agro.systementerprisestandards.entity.SystemEnterpriseStandards;
import com.surekam.modules.api.dto.resp.BatchCropInfoResp;

import java.lang.String;
import java.util.List;

/**
 * 批次计划表Entity
 * @author luoxw
 * @version 2019-10-16
 */
@Entity
@Table(name = "t_agro_batch_plan")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BatchPlan extends XGXTEntity<BatchPlan> {
	
	private static final long serialVersionUID = 1L;
	private String id;//批次计划ID
	private String planId;//种植计划ID
	private String standardId;//标准ID
	private String standardName;//标准名称--非数据库字段
	private String productId;// 品种主键--非数据库字段
	private String productName;// 品种名称--非数据库字段
	private String batchPlanNo;//批次计划编号
	private String batchDate;//计划批次日期
	private String batchEndDate;//计划批次日期
	private String batchNumber;//计划批次数量
	private String sort;//序号
	private String remark;//备注
	private String batchUnit;//批次单位
	private ProductionBatch productionBatch;//对应的批次
	private List<BatchCropInfoResp> standards;//对应的标准列表

	public BatchPlan() {
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
	
	public String getPlanId() {
		return this.planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	
	public String getStandardId() {
		return this.standardId;
	}
	public void setStandardId(String standardId) {
		this.standardId = standardId;
	}
	
	public String getBatchPlanNo() {
		return this.batchPlanNo;
	}
	public void setBatchPlanNo(String batchPlanNo) {
		this.batchPlanNo = batchPlanNo;
	}
	
	public String getBatchDate() {
		return this.batchDate;
	}
	public void setBatchDate(String batchDate) {
		this.batchDate = batchDate;
	}
	
	public String getBatchNumber() {
		return this.batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	
	public String getSort() {
		return this.sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public String getRemark() {
		return this.remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBatchUnit() {
		return batchUnit;
	}

	public void setBatchUnit(String batchUnit) {
		this.batchUnit = batchUnit;
	}

	@Transient
	public ProductionBatch getProductionBatch() {
		return productionBatch;
	}

	public void setProductionBatch(ProductionBatch productionBatch) {
		this.productionBatch = productionBatch;
	}

	public String getBatchEndDate() {
		return batchEndDate;
	}

	public void setBatchEndDate(String batchEndDate) {
		this.batchEndDate = batchEndDate;
	}

	@Transient
	public String getStandardName() {
		return standardName;
	}

	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}

	@Transient
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Transient
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Transient
	public List<BatchCropInfoResp> getStandards() {
		return standards;
	}

	public void setStandards(List<BatchCropInfoResp> standards) {
		this.standards = standards;
	}

	
	
}


