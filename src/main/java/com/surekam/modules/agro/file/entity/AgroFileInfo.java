package com.surekam.modules.agro.file.entity;

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
 * 附件信息Entity
 * @author xy
 * @version 2019-04-24
 */
@Entity
@Table(name = "t_agro_file_info")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AgroFileInfo extends XGXTEntity<AgroFileInfo> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String fileName;//文件名称
	private String newFileName;//文件名称重命名
	private String url;//URL
	private String fileSize;//文件大小
	private String type;//文件类型       //1-image,2-file,3-video,4-audio
	private String absolutePath;//完整路径
	private String ywzbId;//业务主表ID
	private String ywzbType;//业务主表类别

	public AgroFileInfo() {
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
	
	public String getFileSize() {
		return this.fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	
	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getAbsolutePath() {
		return this.absolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	
	public String getYwzbId() {
		return this.ywzbId;
	}
	public void setYwzbId(String ywzbId) {
		this.ywzbId = ywzbId;
	}
	
	public String getYwzbType() {
		return this.ywzbType;
	}
	public void setYwzbType(String ywzbType) {
		this.ywzbType = ywzbType;
	}
	
}


