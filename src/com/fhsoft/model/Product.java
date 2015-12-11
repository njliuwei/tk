package com.fhsoft.model;

/**
 * @ClassName:com.fhsoft.model.Product
 * @Description:
 *
 * @Author:liyi
 * @Date:2015年11月6日下午2:45:54
 *
 */
public class Product {
	
	private int id;
	private String name;
	private String propversion;
	private int subject;
	private String zsd;
	private String tx;
	private String nd;
	private String status;
	private int qstCount;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPropversion() {
		return propversion;
	}
	public void setPropversion(String propversion) {
		this.propversion = propversion;
	}
	public int getSubject() {
		return subject;
	}
	public void setSubject(int subject) {
		this.subject = subject;
	}
	public String getZsd() {
		return zsd;
	}
	public void setZsd(String zsd) {
		this.zsd = zsd;
	}
	public String getTx() {
		return tx;
	}
	public void setTx(String tx) {
		this.tx = tx;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getQstCount() {
		return qstCount;
	}
	public void setQstCount(int qstCount) {
		this.qstCount = qstCount;
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
	public String getNd() {
		return nd;
	}
	public void setNd(String nd) {
		this.nd = nd;
	}
	
}
