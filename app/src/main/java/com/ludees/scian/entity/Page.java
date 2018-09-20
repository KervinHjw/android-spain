package com.ludees.scian.entity;

/**
 * 
 * @author jiyx
 * 
 */
public class Page {

	private int pageCount;// 总页数
	private int pageNo;// 当前页码
	private int pageSize;// 每页记录数

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
