package com.en.scian.entity;

import java.util.List;

/**
 * 专家建议基本解析类
 * 
 * @author jiyx
 * 
 */
public class ArticleInfoBean {
	private List<ArticleInfo> data;
	private String msg;
	private Page page;
	private int status;

	public List<ArticleInfo> getData() {
		return data;
	}

	public void setData(List<ArticleInfo> data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
