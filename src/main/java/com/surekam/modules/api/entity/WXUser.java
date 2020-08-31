package com.surekam.modules.api.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.DataEntity;
import com.surekam.common.utils.IdGen;
/**
 * 微信用户信息Entity
 * @author huangrd
 * @version 2019-01-22
 */

@Entity
@Table(name = "sys_wx_user")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WXUser extends DataEntity<WXUser> {
	
		private static final long serialVersionUID = 1L;
	 	private String openId;//用户的唯一标识
	    private String nickname;//用户昵称
	    private int sex;//用户性别 1是男性 2是女性 0是未知
	    private String province;//用户个人资料填写的省份
	    private String city;//用户个人资料填写的城市
	    private String country;//国家
	    private String headImgUrl;//用户头像 最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），
	    						  //用户没有头像时该项为空，若用户更换头像，原有头像URL将失效
	    private String unionId;//只有在用户将公众号绑定到微信开发平台账号后，才会出现该字段
	    
	    public WXUser() {
			super();
		}

		public String getOpenId() {
			return openId;
		}

		public void setOpenId(String openId) {
			this.openId = openId;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public int getSex() {
			return sex;
		}

		public void setSex(int sex) {
			this.sex = sex;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String getHeadImgUrl() {
			return headImgUrl;
		}

		public void setHeadImgUrl(String headImgUrl) {
			this.headImgUrl = headImgUrl;
		}

		public String getUnionId() {
			return unionId;
		}

		public void setUnionId(String unionId) {
			this.unionId = unionId;
		}
	    

}
