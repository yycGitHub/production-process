package com.surekam.modules.cms.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.surekam.common.persistence.IncrementDataEntity;
import com.surekam.modules.sys.entity.User;

/**
 * 评论Entity
 */
@Entity
@Table(name = "CMS_COMMENT")
@DynamicInsert 
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Comment extends IncrementDataEntity<Comment> {

	private static final long serialVersionUID = 1L;
	private Category category;// 分类编号
	private Integer contentId;	// 归属分类内容的编号（Article.id、Photo.id、Download.id）
	private String title;	// 归属分类内容的标题（Article.title、Photo.title、Download.title）
	private String content; // 评论内容
	private String ip; 		// 评论IP
	private User auditUser; // 审核人
	private Date auditDate;	// 审核时间
	
	public Comment() {
		super();
		this.delFlag = DEL_FLAG_AUDIT;
	}

	@ManyToOne
	@JoinColumn(name="category_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@NotNull
	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=1, max=255)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@ManyToOne
	@JoinColumn(name="audit_user_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(User auditUser) {
		this.auditUser = auditUser;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}