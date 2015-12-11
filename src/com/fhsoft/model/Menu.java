package com.fhsoft.model;

/**
 * @ClassName:com.fhsoft.model.Menu
 * @Description:
 *
 * @Author:liyi
 * @Date:2015年10月20日下午2:07:39
 *
 */
public class Menu {
	
	private int id;
	private String name;
	private int parentId;
	private String url;
	private int type;
	
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
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
