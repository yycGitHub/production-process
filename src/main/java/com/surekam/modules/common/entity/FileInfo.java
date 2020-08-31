package com.surekam.modules.common.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.surekam.common.persistence.BaseEntity;
import com.surekam.common.utils.IdGen;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.utils.UserUtils;

/**
 * 文件信息Entity
 * @author l
 * @version 2017-09-30
 */
@Entity
@Table(name = "SYS_FILE_INFO")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FileInfo extends BaseEntity<FileInfo> {
	
	private static final long serialVersionUID = 1L;
	private String id;//
	private String fileName;//源文件名
	private String newFileName;//保存的文件名
	private String url;//文件完整访问路径
	private String absolutePath;//相对路径
	private Long fileSize;//文件大小
	private String type;//文件类型
	private User createBy;//创建者
	private Date createDate;//创建时间
	private User updateBy;//更新者
	private Date updateDate;//更新时间
	private String delFlag;//删除标记
	
	private String ywzbId;//业务主表主键
	private String ywzbType;//业务主表类别值
	private String fieldMark;//表字段标记
	
	//业务主表值标识  根据值以及业务主表主键对应查找对应的附件
	public static final String YWZB_TYPE = "ywzbType";
	//文章的附件类型值  暂未使用 作为范例
	public static final String CMS_ACTICLE_TYPE = "1";
	//请假单表的附件类型值  暂未使用 作为范例  每增加一个业务主表 请在此增加一个常量定义 值以此往上+1
	public static final String YWZB_TYPE_QJD = "2";
	//角色表的附件类型值  暂未使用 作为范例
	public static final String YWZB_TYPE_ROLE = "3";
	

	public FileInfo() {
		super();
	}
	
	@PrePersist
	public void prePersist(){
		this.id = IdGen.uuid();
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			this.createBy = user;
		}
		this.createDate = new Date();
		this.delFlag = FileInfo.DEL_FLAG_NORMAL;
	}
	
	@PreUpdate
	public void preUpdate(){
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			this.updateBy = user;
		}
		this.updateDate = new Date();
	}
		
	@Id
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getNewFileName() {
		return this.newFileName;
	}
	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}
	
	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Long getFileSize() {
		return this.fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	
	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public User getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(User updateBy) {
		this.updateBy = updateBy;
	}

	@JsonIgnore
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@JsonIgnore
	public String getDelFlag() {
		return this.delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	
	public String getYwzbId() {
		return ywzbId;
	}

	public void setYwzbId(String ywzbId) {
		this.ywzbId = ywzbId;
	}

	public String getYwzbType() {
		return ywzbType;
	}

	public void setYwzbType(String ywzbType) {
		this.ywzbType = ywzbType;
	}

	public String getFieldMark() {
		return fieldMark;
	}

	public void setFieldMark(String fieldMark) {
		this.fieldMark = fieldMark;
	}
}


