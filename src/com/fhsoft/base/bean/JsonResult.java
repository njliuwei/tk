package com.fhsoft.base.bean;

/**
 * @ClassName: com.fhsoft.base.bean
 * @Description:
 * 
 * @author liyi
 * @date 2014年9月28日 上午1:08:51
 * 
 */
public class JsonResult {

	/* JSON返回对象 */
	private boolean success = false;
	private String msg = "";
	private Object obj = null;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}
