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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Transient;
import javax.persistence.Lob;
import java.lang.String;
import javax.persistence.FetchType;

/**
 * 请假单Entity
 * @author ligm
 * @version 2017-11-20
 */
@Entity
@Table(name = "GXQJ0101")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GXQJ0101 extends XGXTEntity<GXQJ0101> {
	
	private static final long serialVersionUID = 1L;
	private String gxqj0101Id;//请假单_ID
	private String gxxs0101Id;//学生基本信息_ID
	private String xm;//姓名
	private String qjdbt;//请假单标题
	private String qjlb;//请假类别
	private String sqrq;//请假单的创建日期
	private String sqsj;//请假单的创建时间
	private String qjksrq;//请假开始日期
	private String qjkssj;//请假开始时间
	private String qjjsrq;//请假结束日期
	private String qjjssj;//请假结束时间
	private String qjzsc;//请假总时长（天）
	private String sjqjzsj;//实际请假总时长（天）
	private String zt;//审核状态、续假、销假
	private String sfbl;//是否为事后补假
	private String xyid;//学院ID
	private String xymc;//学院名称
	private String zyid;//专业ID
	private String zymc;//专业名称
	private String bjid;//班级ID
	private String bjmc;//班级名称
	private String slid;//工作流实例ID
	private byte[] qjsy;//请假事由
	private byte[] bz;//备注
	private String xh;//学号
	private String jzyj;//家长意见  由辅导员审批时填入 同时可上传附件  不保存历史审批中的附件
	private String sfxj;//是否有续假
	private String sfyxj;//销假状态 1为申请销假  2为销假结束  3为销假被驳回
	private String xjsfyjs;//续假状态 1为申请续假  2为审批已结束 3为续假驳回
	private String sznj;//所在年级

	public GXQJ0101() {
		super();
	}
	
	@PrePersist
	public void prePersist(){
		this.gxqj0101Id = IdGen.uuid();
	}
		
	@Id
	@Column(name="GXQJ0101_ID")
	public String getGxqj0101Id() {
		return this.gxqj0101Id;
	}
	public void setGxqj0101Id(String gxqj0101Id) {
		this.gxqj0101Id = gxqj0101Id;
	}
	
	@Column(name="GXXS0101_ID")
	public String getGxxs0101Id() {
		return this.gxxs0101Id;
	}
	public void setGxxs0101Id(String gxxs0101Id) {
		this.gxxs0101Id = gxxs0101Id;
	}
	
	public String getXm() {
		return this.xm;
	}
	public void setXm(String xm) {
		this.xm = xm;
	}
	
	public String getQjdbt() {
		return this.qjdbt;
	}
	public void setQjdbt(String qjdbt) {
		this.qjdbt = qjdbt;
	}
	
	public String getQjlb() {
		return this.qjlb;
	}
	public void setQjlb(String qjlb) {
		this.qjlb = qjlb;
	}
	
	public String getSqrq() {
		return this.sqrq;
	}
	public void setSqrq(String sqrq) {
		this.sqrq = sqrq;
	}
	
	public String getSqsj() {
		return this.sqsj;
	}
	public void setSqsj(String sqsj) {
		this.sqsj = sqsj;
	}
	
	public String getQjksrq() {
		return this.qjksrq;
	}
	public void setQjksrq(String qjksrq) {
		this.qjksrq = qjksrq;
	}
	
	public String getQjkssj() {
		return this.qjkssj;
	}
	public void setQjkssj(String qjkssj) {
		this.qjkssj = qjkssj;
	}
	
	public String getQjjsrq() {
		return this.qjjsrq;
	}
	public void setQjjsrq(String qjjsrq) {
		this.qjjsrq = qjjsrq;
	}
	
	public String getQjjssj() {
		return this.qjjssj;
	}
	public void setQjjssj(String qjjssj) {
		this.qjjssj = qjjssj;
	}
	
	public String getQjzsc() {
		return this.qjzsc;
	}
	public void setQjzsc(String qjzsc) {
		this.qjzsc = qjzsc;
	}
	
	public String getSjqjzsj() {
		return this.sjqjzsj;
	}
	public void setSjqjzsj(String sjqjzsj) {
		this.sjqjzsj = sjqjzsj;
	}
	
	public String getZt() {
		return this.zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	
	public String getSfbl() {
		return this.sfbl;
	}
	public void setSfbl(String sfbl) {
		this.sfbl = sfbl;
	}
	
	public String getXyid() {
		return this.xyid;
	}
	public void setXyid(String xyid) {
		this.xyid = xyid;
	}
	
	public String getXymc() {
		return this.xymc;
	}
	public void setXymc(String xymc) {
		this.xymc = xymc;
	}
	
	public String getZyid() {
		return this.zyid;
	}
	public void setZyid(String zyid) {
		this.zyid = zyid;
	}
	
	public String getZymc() {
		return this.zymc;
	}
	public void setZymc(String zymc) {
		this.zymc = zymc;
	}
	
	public String getBjid() {
		return this.bjid;
	}
	public void setBjid(String bjid) {
		this.bjid = bjid;
	}
	
	public String getBjmc() {
		return this.bjmc;
	}
	public void setBjmc(String bjmc) {
		this.bjmc = bjmc;
	}
	
	public String getSlid() {
		return this.slid;
	}
	public void setSlid(String slid) {
		this.slid = slid;
	}
	
	@Transient	
	public String getQjsyString() {
		if(this.qjsy == null){
			return null;
		}
		return new String(this.qjsy);
	}
		
	@Lob
  	@Basic(fetch = FetchType.EAGER)
  	@Column(columnDefinition="BLOB", nullable=true)
	public byte[] getQjsy() {
		return this.qjsy;
	}
	public void setQjsy(byte[] qjsy) {
		this.qjsy = qjsy;
	}
	
	@Transient	
	public String getBzString() {
		if(this.bz == null){
			return null;
		}
		return new String(this.bz);
	}
		
	@Lob
  	@Basic(fetch = FetchType.EAGER)
  	@Column(columnDefinition="BLOB", nullable=true)
	public byte[] getBz() {
		return this.bz;
	}
	public void setBz(byte[] bz) {
		this.bz = bz;
	}
	
	public String getXh() {
		return this.xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	
	public String getJzyj() {
		return this.jzyj;
	}
	public void setJzyj(String jzyj) {
		this.jzyj = jzyj;
	}
	
	public String getSfxj() {
		return this.sfxj;
	}
	public void setSfxj(String sfxj) {
		this.sfxj = sfxj;
	}
	
	public String getSfyxj() {
		return this.sfyxj;
	}
	public void setSfyxj(String sfyxj) {
		this.sfyxj = sfyxj;
	}
	
	public String getXjsfyjs() {
		return this.xjsfyjs;
	}
	public void setXjsfyjs(String xjsfyjs) {
		this.xjsfyjs = xjsfyjs;
	}
	
	public String getSznj() {
		return this.sznj;
	}
	public void setSznj(String sznj) {
		this.sznj = sznj;
	}
	
}


