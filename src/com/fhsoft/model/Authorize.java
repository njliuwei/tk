package com.fhsoft.model;

/**
 * @ClassName:com.fhsoft.model.Authorize
 * @Description:authorize表的数据库操作
 *
 * @Author:liyi
 * @Date:2015年11月16日下午4:45:08
 *
 */
public class Authorize {
	private int id;
	private int productId;
	private String productName;
	private String userName;
	private String userIP;
	private String dueDate;
	private String isMember;
	private String code;
	private String created;
	private Integer creatorId;
	private String creator;
	private String lastmodified;
	private Integer lastmodifierId;
	private String lastmodifier;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserIP() {
		return userIP;
	}
	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getIsMember() {
		return isMember;
	}
	public void setIsMember(String isMember) {
		this.isMember = isMember;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public Integer getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getLastmodified() {
		return lastmodified;
	}
	public void setLastmodified(String lastmodified) {
		this.lastmodified = lastmodified;
	}
	public Integer getLastmodifierId() {
		return lastmodifierId;
	}
	public void setLastmodifierId(Integer lastmodifierId) {
		this.lastmodifierId = lastmodifierId;
	}
	public String getLastmodifier() {
		return lastmodifier;
	}
	public void setLastmodifier(String lastmodifier) {
		this.lastmodifier = lastmodifier;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
}
