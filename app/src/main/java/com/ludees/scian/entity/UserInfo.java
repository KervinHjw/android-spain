package com.ludees.scian.entity;

/**
 * 第三方登录
 * 
 * @author jiyx
 * 
 */
public class UserInfo {

	private int userId;// 用户ID
	private String userName;// 第三方登录的标识
	private String realName;// 昵称
	private int userType;// 用户类型（1：手机号注册 2：第三方）
	private String phone;// 手机号
	private int sex;// 性别（1：男 2：女）
	private int age;// 年龄
	private String birthday;// 生日
	private float height;// 身高
	private float weight;// 体重
	private String createTime;// 创建时间
	private int isPush = 1;// 是否推送（1：推送 2：不推送）
	private String verificationCode;// 验证码
	private int state = 1;// 状态（1：可用 2：不可用）
	
	private String userPic;// 用户图像
	private String qrcodePic;// 二维码
	private String sendTime;//验证码发送时间
	private String usermail;//邮箱
	private String pwd;//密码
	public String getUsermail() {
		return usermail;
	}

	public void setUsermail(String usermail) {
		this.usermail = usermail;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public int getIsPush() {
		return isPush;
	}

	public void setIsPush(int isPush) {
		this.isPush = isPush;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getUserPic() {
		return userPic;
	}

	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}

	public String getQrcodePic() {
		return qrcodePic;
	}

	public void setQrcodePic(String qrcodePic) {
		this.qrcodePic = qrcodePic;
	}

}
