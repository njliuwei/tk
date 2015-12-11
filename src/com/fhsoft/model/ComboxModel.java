package com.fhsoft.model;

/**
 * @ClassName:com.fhsoft.model.ComboxModel
 * @Description:
 *
 * @Author:liyi
 * @Date:2015年11月3日下午2:48:34
 *
 */
public class ComboxModel {
	
	private int id;
	private String name;
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

	public ComboxModel() {
		super();
	}
	public ComboxModel(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

}
