package com.surekam.modules.agro.productbatchtaskresolve.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.PrePersist;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;
import com.surekam.modules.agro.label.entity.Label;
import com.surekam.modules.agro.standardtasklist.entity.StandardTaskList;

import java.lang.String;
import java.util.List;

/**
 * 分解任务表Entity
 * 
 * @author liwei
 * @version 2019-04-25
 */
@Entity
@Table(name = "t_agro_product_batch_task_resolve")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductBatchTaskResolve extends XGXTEntity<ProductBatchTaskResolve> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String taskId;// 主任务ID
	private String dispatchTime;// 分解任务开始时间
	private String finishTime;// 完成时间
	private String serialNumber;// 流水号
	private String standardItemName;
	private String executionStatus;// 执行状态（0-不执行，1-执行）
	private String nonexecutionReason;// 不执行原因
	private String confirmStates;// 确认状态
	private String confirmTime;// 确认时间
	private List<Label> labelList;// 标签集合
	private StandardTaskList standardTaskList;
	private String longitude;// 经度
	private String latitude;// 纬度
	private String address;// 详细地址
	private String sendDate; // 发送日期
	private String sendStates; // 发送状态

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ProductBatchTaskResolve() {
		super();
	}

	@PrePersist
	public void prePersist() {
		this.id = IdGen.uuid();
	}

	@Id
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getDispatchTime() {
		return this.dispatchTime;
	}

	public void setDispatchTime(String dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public String getFinishTime() {
		return this.finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	@Transient
	public String getStandardItemName() {
		return standardItemName;
	}

	public void setStandardItemName(String standardItemName) {
		this.standardItemName = standardItemName;
	}

	public String getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(String executionStatus) {
		this.executionStatus = executionStatus;
	}

	public String getNonexecutionReason() {
		return nonexecutionReason;
	}

	public void setNonexecutionReason(String nonexecutionReason) {
		this.nonexecutionReason = nonexecutionReason;
	}

	@Transient
	public List<Label> getLabelList() {
		return labelList;
	}

	public void setLabelList(List<Label> labelList) {
		this.labelList = labelList;
	}

	@Transient
	public StandardTaskList getStandardTaskList() {
		return standardTaskList;
	}

	public void setStandardTaskList(StandardTaskList standardTaskList) {
		this.standardTaskList = standardTaskList;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getConfirmStates() {
		return confirmStates;
	}

	public void setConfirmStates(String confirmStates) {
		this.confirmStates = confirmStates;
	}

	public String getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getSendStates() {
		return sendStates;
	}

	public void setSendStates(String sendStates) {
		this.sendStates = sendStates;
	}
}
