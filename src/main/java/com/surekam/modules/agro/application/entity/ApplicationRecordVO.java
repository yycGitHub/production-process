package com.surekam.modules.agro.application.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.modules.agro.file.entity.AgroFileInfo;

import java.lang.String;
import java.util.List;

/**
 * 农户申请和平台委派视图Entity
 * @author xy
 * @version 2019-06-26
 */
@Entity
@Table(name = "v_agro_application_recordvo")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationRecordVO extends XGXTEntity<ApplicationRecordVO> {
	
	private static final long serialVersionUID = 1L;
	private String id;//农户申请记录id
	private String officeId;//公司id
	private String officeName;//机构名称
	private String baseId;//基地id
	private String baseName;//名称
	private String appointmentTime;//预约日期
	private String address;//地址
	private String phoneNumber;//电话号码
	private String contacts;//联系人
	private String productId;//产品品种
	private String productName;//种类名称
	private String detailedQuestions;//问题详细
	private String expertId;//专家id
	private String createName;//姓名
	private String expertName;//专家名称
	private String drId;//委派记录id
	private String acceptStatus;//是否接受委派 0：接受，1：拒绝
	private String remark;//委派说明
	private String explainNo;//拒绝原因
	private String taskStatus;//任务状态  0：已委派，1：已执行，2：已拒绝，3：已完成
	private String drStates;//更新标识
	private String estates;//更新标识
	private String soDelFlag;//删除标记
	private String suDelFlag;//删除标记
	private String btStates;//更新标识
	private String pltStates;//更新标识
	
	private String batchId;//批次id
	private String batchCode;//批次号
	
	
	private List<AgroFileInfo> fileList;//图片list
	private List<AgroFileInfo> auditList;//录音list
	
	public ApplicationRecordVO() {
		super();
	}
	
	@Id
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getOfficeId() {
		return this.officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	
	public String getOfficeName() {
		return this.officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	
	public String getBaseId() {
		return this.baseId;
	}
	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
	
	public String getBaseName() {
		return this.baseName;
	}
	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}
	
	public String getAppointmentTime() {
		return this.appointmentTime;
	}
	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}
	
	public String getAddress() {
		return this.address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getContacts() {
		return this.contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	
	public String getProductId() {
		return this.productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getProductName() {
		return this.productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getDetailedQuestions() {
		return this.detailedQuestions;
	}
	public void setDetailedQuestions(String detailedQuestions) {
		this.detailedQuestions = detailedQuestions;
	}
	
	public String getExpertId() {
		return this.expertId;
	}
	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}
	
	public String getCreateName() {
		return this.createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	
	public String getExpertName() {
		return this.expertName;
	}
	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}
	
	public String getDrId() {
		return this.drId;
	}
	public void setDrId(String drId) {
		this.drId = drId;
	}
	
	public String getAcceptStatus() {
		return this.acceptStatus;
	}
	public void setAcceptStatus(String acceptStatus) {
		this.acceptStatus = acceptStatus;
	}
	
	public String getRemark() {
		return this.remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getTaskStatus() {
		return this.taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	public String getDrStates() {
		return this.drStates;
	}
	public void setDrStates(String drStates) {
		this.drStates = drStates;
	}
	
	public String getEstates() {
		return this.estates;
	}
	public void setEstates(String estates) {
		this.estates = estates;
	}
	
	public String getSoDelFlag() {
		return this.soDelFlag;
	}
	public void setSoDelFlag(String soDelFlag) {
		this.soDelFlag = soDelFlag;
	}
	
	public String getSuDelFlag() {
		return this.suDelFlag;
	}
	public void setSuDelFlag(String suDelFlag) {
		this.suDelFlag = suDelFlag;
	}
	
	public String getBtStates() {
		return this.btStates;
	}
	public void setBtStates(String btStates) {
		this.btStates = btStates;
	}
	
	public String getPltStates() {
		return this.pltStates;
	}
	public void setPltStates(String pltStates) {
		this.pltStates = pltStates;
	}

	public String getExplainNo() {
		return explainNo;
	}

	public void setExplainNo(String explainNo) {
		this.explainNo = explainNo;
	}

	@Transient
	public List<AgroFileInfo> getFileList() {
		return fileList;
	}

	public void setFileList(List<AgroFileInfo> fileList) {
		this.fileList = fileList;
	}

	@Transient
	public List<AgroFileInfo> getAuditList() {
		return auditList;
	}

	public void setAuditList(List<AgroFileInfo> auditList) {
		this.auditList = auditList;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}
	
	
}


