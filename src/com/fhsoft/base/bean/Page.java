package com.fhsoft.base.bean;

import java.util.ArrayList;

public class Page {

    /** 当前第几页 */
    protected int pageNumber ;

    /** 每页最大记录数*/
    protected int pageSize ;

    /** 查询结果 */
    protected Object rows = null;

    /** 总记录数 */
    protected long total;
    
    /** 总页数 */
    protected int pageCount ;

    /** 构造方法 */
    @SuppressWarnings("rawtypes")
	public Page() {
        total = 0;
        rows = new ArrayList();
    }
    
	/**
	 * 
	 */
	public Page(int pageNumber, int pageSize,long total, Object rows) {
		super();
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.rows = rows;
		this.total = total;
	}

	/**
	 * @param pageNumber
	 * @param pageSize
	 * @param rows
	 * @param total
	 */
	public Page(int pageNumber, int pageSize,long total, Object rows,int pageCount) {
		super();
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.rows = rows;
		this.total = total;
		this.pageCount = pageCount;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Object getRows() {
		return rows;
	}

	public void setRows(Object rows) {
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}


}
