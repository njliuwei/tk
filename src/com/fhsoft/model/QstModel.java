package com.fhsoft.model;

/**
 * @ClassName:com.fhsoft.model.QstModel
 * @Description:
 *
 * @Author:liyi
 * @Date:2015年12月1日下午4:09:39
 *
 */
public class QstModel {
	
	private int id;
	private String content;
	private String summary;
	private int index;
	private String type;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
