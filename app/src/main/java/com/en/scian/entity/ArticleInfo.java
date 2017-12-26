package com.en.scian.entity;

/**
 * 专家建议
 * 
 * @author jiyx
 * 
 */
public class ArticleInfo {

	private int articleId;// 专家建议ID
	private String createTime;// 创建时间
	private String title;// 标题
	private String summary;// 摘要
	private int type;// 类型（1:日常建议2：血压3：血糖建议）
	private String picUrl;// 图片地址

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

}
