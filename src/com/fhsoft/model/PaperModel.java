package com.fhsoft.model;

import java.util.List;

/**
 * @ClassName:com.fhsoft.model.PaperModel
 * @Description:
 *
 * @Author:liyi
 * @Date:2015年12月1日上午11:04:48
 *
 */
public class PaperModel {
	
	private String paperTitle;
	private String paperNote;
	private String paperAuthor;
	private String parperAnswerTime;
	private int productId;
	private int subject;
	private List<QstTypeModel> typeList;
	public String getPaperTitle() {
		return paperTitle;
	}
	public void setPaperTitle(String paperTitle) {
		this.paperTitle = paperTitle;
	}
	public String getPaperNote() {
		return paperNote;
	}
	public void setPaperNote(String paperNote) {
		this.paperNote = paperNote;
	}
	public String getPaperAuthor() {
		return paperAuthor;
	}
	public void setPaperAuthor(String paperAuthor) {
		this.paperAuthor = paperAuthor;
	}
	public String getParperAnswerTime() {
		return parperAnswerTime;
	}
	public void setParperAnswerTime(String parperAnswerTime) {
		this.parperAnswerTime = parperAnswerTime;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public List<QstTypeModel> getTypeList() {
		return typeList;
	}
	public void setTypeList(List<QstTypeModel> typeList) {
		this.typeList = typeList;
	}
	public int getSubject() {
		return subject;
	}
	public void setSubject(int subject) {
		this.subject = subject;
	}
	
}
