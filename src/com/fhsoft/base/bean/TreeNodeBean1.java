package com.fhsoft.base.bean;

/** 
 * @ClassName: com.fhsoft.base.bean
 * @Description: easyui tree的节点属性对象
 *
 * @author liyi 
 * @date 2014年9月19日 上午11:20:26 
 * 
 */
public class TreeNodeBean1 {
	
	private int id;  //节点ID
	private String name;  //显示的节点文本
	private int _parentId;
	private int sort;
	private String comment;
	private String level;
	private String iconCls;  //显示的节点图标CSS类ID
	private String state = "closed";  //节点状态，'open' 或 'closed'
	
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
	public int get_parentId() {
		return _parentId;
	}
	public void set_parentId(int _parentId) {
		this._parentId = _parentId;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
}
