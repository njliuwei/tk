package com.fhsoft.model;

/**
 * 
 * @Classname com.fhsoft.model.Zsd
 * @Description 
 *
 * @author lw
 * @Date 2015-10-23 上午9:06:02
 *
 */
public class Zsd {
	private int id;
	
	private int parent_id;
	
	private String name;
	
	private String subject;
	
	private String code;
	
	private String pcode;
	
	private String is_leaf;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getIs_leaf() {
		return is_leaf;
	}

	public void setIs_leaf(String is_leaf) {
		this.is_leaf = is_leaf;
	}
	
	
}
