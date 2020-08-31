package com.surekam.modules.sample.entity;

import java.util.List;

public class UserInfo {

	private String name;
	private String idCard;
	private List<ScoreInfo>  scoreList;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the idCard
	 */
	public String getIdCard() {
		return idCard;
	}
	/**
	 * @param idCard the idCard to set
	 */
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	/**
	 * @return the scoreList
	 */
	public List<ScoreInfo> getScoreList() {
		return scoreList;
	}
	/**
	 * @param scoreList the scoreList to set
	 */
	public void setScoreList(List<ScoreInfo> scoreList) {
		this.scoreList = scoreList;
	}
	
}
