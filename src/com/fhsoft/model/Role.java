package com.fhsoft.model;

public class Role {

	private Integer id;
	
	private String name;
	
	private String created;
	
	private Integer creator;
	
	private String lastmodified;
	
	private Integer lastmodifier;
	
	private String description;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
