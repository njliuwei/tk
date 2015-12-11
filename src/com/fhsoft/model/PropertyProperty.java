package com.fhsoft.model;

/**
 * @ClassName:com.fhsoft.model.PropertyProperty
 * @Description:
 *
 * @Author:liyi
 * @Date:2015年11月3日上午9:36:39
 *
 */
public class PropertyProperty {

	private int id;
	private String name;
	private String comment;
	private int parentId;
	private int level;
	private String attachPath;
	
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getAttachPath() {
		return attachPath;
	}
	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}
	
}
