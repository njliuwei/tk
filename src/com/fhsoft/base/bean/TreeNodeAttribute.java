package com.fhsoft.base.bean;

/** 
 * @ClassName: com.fhsoft.base.bean
 * @Description: easyui tree的自定义属性
 *
 * @author liyi 
 * @date 2014年9月19日 上午11:31:56 
 * 
 */
public class TreeNodeAttribute {

	private String url;  //节点链接地址
	
	private String title;
	
	private String type;

	/**
	 * 
	 */
	public TreeNodeAttribute() {
		super();
	}

	/**
	 * @param url
	 */
	public TreeNodeAttribute(String url) {
		super();
		this.url = url;
	}
	

	/**
	 * 
	 */
	public TreeNodeAttribute(String url, String title, String type) {
		super();
		this.url = url;
		this.title = title;
		this.type = type;
	}

	/**
	 * 
	 */
	public TreeNodeAttribute(String url, String title) {
		super();
		this.url = url;
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
