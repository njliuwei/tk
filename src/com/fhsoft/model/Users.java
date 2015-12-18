package com.fhsoft.model;

/**
 * 
 * @author hjb
 * @date 2015-11-18 下午3:46:05
 */
public class Users {

	private int id;
	
	private String name;
	
	private String username;
	
	private String password;

	private String created;
	
	private Integer creator;
	
	private String lastmodified;
	
	private Integer lastmodifier;
	
	private Integer is_member;
	
	public Integer getIs_member() {
		return is_member;
	}

	public void setIs_member(Integer is_member) {
		this.is_member = is_member;
	}

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	public String getLastmodified() {
		return lastmodified;
	}

	public void setLastmodified(String lastmodified) {
		this.lastmodified = lastmodified;
	}

	public Integer getLastmodifier() {
		return lastmodifier;
	}

	public void setLastmodifier(Integer lastmodifier) {
		this.lastmodifier = lastmodifier;
	}
	
	
	
}
