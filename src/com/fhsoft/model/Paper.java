package com.fhsoft.model;

/**
 * @ClassName:com.fhsoft.model.Paper
 * @Description:
 *
 * @Author:liyi
 * @Date:2015年11月17日下午3:05:05
 *
 */
public class Paper {
	
	private int id;
	private String name;
	private int product;
	private String productName;
	private String paperContent;
	private int subject;
	private String exportPath1;
	private String exportPath2;
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
	public int getProduct() {
		return product;
	}
	public void setProduct(int product) {
		this.product = product;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPaperContent() {
		return paperContent;
	}
	public void setPaperContent(String paperContent) {
		this.paperContent = paperContent;
	}
	public int getSubject() {
		return subject;
	}
	public void setSubject(int subject) {
		this.subject = subject;
	}
	public String getExportPath1() {
		return exportPath1;
	}
	public void setExportPath1(String exportPath1) {
		this.exportPath1 = exportPath1;
	}
	public String getExportPath2() {
		return exportPath2;
	}
	public void setExportPath2(String exportPath2) {
		this.exportPath2 = exportPath2;
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
}
