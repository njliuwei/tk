package com.fhsoft.model;

/**
 * @ClassName:com.fhsoft.model.User
 * @Description:
 *
 * @Author:liyi
 * @Date:2015年6月30日上午10:56:01
 *
 */
public class User {

	private int id;
	private String loginname;
	private String username;
	private String password;
	private int type;
	private String created;
	private int creatorid;
	private String creator;
	private String lastmodified;
	private int lastmodifierid;
	private String lastmodifier;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
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
	public int getCreatorid() {
		return creatorid;
	}
	public void setCreatorid(int creatorid) {
		this.creatorid = creatorid;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getLastmodified() {
		return lastmodified;
	}
	public void setLastmodified(String lastmodified) {
		this.lastmodified = lastmodified;
	}
	public int getLastmodifierid() {
		return lastmodifierid;
	}
	public void setLastmodifierid(int lastmodifierid) {
		this.lastmodifierid = lastmodifierid;
	}
	public String getLastmodifier() {
		return lastmodifier;
	}
	public void setLastmodifier(String lastmodifier) {
		this.lastmodifier = lastmodifier;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
