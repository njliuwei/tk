package com.fhsoft.base.bean;

/** 
 * @ClassName: com.fhsoft.base.bean
 * @Description: easyui tree的节点属性对象
 *
 * @author liyi 
 * @date 2014年9月19日 上午11:20:26 
 * 
 */
public class TreeNodeBean {
	
	private int id;  //节点ID
	private int pid;
	private String text;  //显示的节点文本
	private String iconCls;  //显示的节点图标CSS类ID
	private boolean checked = false;  //该节点是否被选中
	private String state = "closed";  //节点状态，'open' 或 'closed'
	private TreeNodeAttribute attributes;  //绑定该节点的自定义属性
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public TreeNodeAttribute getAttributes() {
		return attributes;
	}
	public void setAttributes(TreeNodeAttribute attributes) {
		this.attributes = attributes;
	}
}
