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
	
	private Integer id;
	private String name;
	private Integer parentId;
	private String url;
	private Integer type;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	
	
}
