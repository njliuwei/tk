package com.fhsoft.model;

import java.util.List;

/**
 * @ClassName:com.fhsoft.model.QstTypeModel
 * @Description:
 *
 * @Author:liyi
 * @Date:2015年12月1日下午4:07:50
 *
 */
public class QstTypeModel {
	
	private int id;
	private String name;
	private String index;
	private String comment;
	private List<QstModel> qstList;
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
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List<QstModel> getQstList() {
		return qstList;
	}
	public void setQstList(List<QstModel> qstList) {
		this.qstList = qstList;
	}
}
