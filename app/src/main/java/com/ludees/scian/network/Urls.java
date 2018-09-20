package com.ludees.scian.network;

/**
 * @description:网络地址
 * @author zhangp
 * 
 */
public class Urls {

	//public static final String BASEURL = "http://192.168.2.222:8080/lude";// 本地
	//public static final String BASEURL = "http://192.168.2.41:8080/lude";// 本地
	//public static final String BASEURL = "http://47.88.34.121:80/lude";// 测试
	//public static final String BASEURL = "https://www.ludehealth.com:8082/lude";// 正式https
	public static final String BASEURL = "http://139.196.106.123/lude";//正式

	
	/** 验证邮箱 */
	public static final String YZ_MAIL = BASEURL
			+ "/app/userInfo/userInfoVerifyMailbox.htm";
	/** 忘记密码 */
	public static final String SEND_MAIL = BASEURL
			+ "/app/userInfo/userInfoForgetPwdMail.htm";
	/** 修改密码 */
	public static final String UPDATE_MAIL = BASEURL
			+ "/app/userInfo/updateUserInfoByMail.htm?";
	/** 添加设备 */
	public static final String ADD_EQUIPMENT = BASEURL
			+ "/app/userEquipment/saveUserEquipmentApp.htm?";

	/** 我的设备列表 */
	public static final String MY_EQUIPMENT_LIST = BASEURL
			+ "/app/userEquipment/getUserEquipmentForPageApp.htm?";

	/** 删除我的设备 */
	public static final String DEL_EQUIPMENT = BASEURL
			+ "/app/userEquipment/deleteUserEquipmentApp.htm?";

	/** 保存血压测量数据 */
	public static final String SAVE_BLOOD_PRESSURE = BASEURL
			+ "/app/bloodPressure/saveBloodPressureApp.htm?";

	/** 测量结果 */
	public static final String GET_BLOOD_PRESSURE = BASEURL
			+ "/app/bloodPressure/getBloodPressureByNewApp.htm?";
	/** 好友测量结果 */
	public static final String GET_FRIENDS_BLOOD_PRESSURE = BASEURL
			+ "/app/bloodPressure/getBloodPressureWhereApp.htm?";

	/** 历史血压数据 */
	public static final String GET_BLOOD_PRESSURE_HISTORY = BASEURL
			+ "/app/bloodPressure/getBloodPressureHisApp.htm?";
	/** 好友历史血压数据 */
	public static final String GET_FRIENDS_BLOOD_PRESSURE_HISTORY = BASEURL
			+ "/app/bloodPressure/getFriendsBloodPressureApp.htm?";

	/** 历史血压数据-趋势图 */
	public static final String GET_BLOOD_PRESSURE_HISTORY_PIC = BASEURL
			+ "/app/bloodPressure/getBloodPressureWhereApp.htm?";

	/** 发送短信验证码 */
	public static final String SEND_MESSAGE = BASEURL
			+ "/app/messageVerificationCode/sendMessage.htm?";

	/** 密码登陆 */
	public static final String PASSWORD = BASEURL
			+ "/app/userInfo/userInfoLoginMail.htm?";

	/** 校验验证码是否过期 */
	public static final String CHECK_VERIFICATIONCODE = BASEURL
			+ "/app/messageVerificationCode/checkVerificationCode.htm?";

	/** 第三方登录 */
	public static final String THIRD_LOGIN = BASEURL
			+ "/app/userInfo/thirdPartyLogin.htm?";

	/** 登录 */
	public static final String LOGIN = BASEURL
			+ "/app/userInfo/userInfoLogin.htm?";

	/** 会员注册 */
	public static final String USER_REGISTER = BASEURL
			+ "/app/userInfo/userInfoRegisterMail.htm";

	/** 轮播图 */
	public static final String GET_LUNBOTU = BASEURL
			+ "/app/bannerInfo/getBannerInfoList.htm?";

	/** 首页数据 */
	public static final String GET_MAIN = BASEURL
			+ "/app/homeIndex/getBloodPressureIndexApp.htm?";

	/** 提醒设置列表 */
	public static final String GET_REMIND_LIST = BASEURL
			+ "/app/remindInfo/getRemindInfoByList.htm?";

	/** 提醒设置详细 */
	public static final String GET_REMIND_INFO = BASEURL
			+ "/app/remindInfo/getRemindInfoById.htm?";

	/** 发布提醒设置 */
	public static final String ADD_REMIND_INFO = BASEURL
			+ "/app/remindInfo/addRemindInfo.htm?";

	/** 修改提醒设置 */
	public static final String UPDATE_REMIND_INFO = BASEURL
			+ "/app/remindInfo/updateRemindInfo.htm?";

	/** 删除提醒设置 */
	public static final String DEL_REMIND_INFO = BASEURL
			+ "/app/remindInfo/deleteRemindInfoById.htm?";

	/** 设置提醒状态 */
	public static final String SET_REMIND_INFO_STATE = BASEURL
			+ "/app/remindInfo/updateRemindInfoState.htm?";

	/** 上传图片 */
	public static final String UPLOAD_IMAGE = BASEURL
			+ "/app/uploadFile/uploadSWFImage.htm?";

	/** 获取用户资料 */
	public static final String GET_USERINFO = BASEURL
			+ "/app/userInfo/getUserInfoById.htm?";

	/** 修改用户资料 */
	public static final String UPDATE_USERINFO = BASEURL
			+ "/app/userInfo/updateUserInfo.htm?";

	/** 退出登录 */
	public static final String EXIT_LOGIN = BASEURL
			+ "/app/userInfo/userInfoLogout.htm?";

	/** 根据手机号查询用户信息 */
	public static final String SELECT_USER = BASEURL
			+ "/app/userInfo/getUserInfoByPhone.htm?";

	/** 专家建议列表 */
	public static final String EXPERT_ADVICE_LIST = BASEURL
			+ "/app/articleInfo/getArticleInfoByList.htm?";

	/** 专家建议详情 */
	public static final String EXPERT_ADVICE_INFO = BASEURL
			+ "/app/articleInfo/showContent.htm?";

	/** 使用说明 */
	public static final String SHOW_USER_INFO = BASEURL
			+ "/app/useInstructions/showUseInstructions.htm?";

	/** 关于鹿得 */
	public static final String ABOUT_LUDE = BASEURL
			+ "/app/aboutUs/showAboutUs.htm?";
	/** 获取好友列表 */
	public static final String GET_FRIENDS_LIST = BASEURL
			+ "/app/myFriends/getMyFriendsListApp.htm?";
	/** 删除好友 */
	public static final String DELETE_FRIENDS = BASEURL
			+ "/app/myFriends/updateState.htm?";
	/** 通过手机号获取好友信息 */
	public static final String GET_FRIEND_INFO = BASEURL
			+ "/app/userInfo/getUserInfoByPhone.htm?";
	/** 发送好友验证 */
	public static final String SEND_FRIENDS_VERIFICATION = BASEURL
			+ "/app/verificationMessage/saveVerificationMessageApp.htm";
	/** 好友验证消息列表 */
	public static final String GET_VERIFICATION_MESSAGE_LIST = BASEURL
			+ "/app/verificationMessage/getVerificationMessageListApp.htm?";
	/** 同意添加好友 */
	public static final String AGREE_TO_ADD_FRIENDS = BASEURL
			+ "/app/verificationMessage/saveVerificationMessageAgreeApp.htm?";
	/** 好友留言列表 */
	public static final String FRIENDS_MESSAGE_LIST = BASEURL
			+ "/app/friendsMessage/getFriendsMessageListApp.htm?";
	/** 好友留言 */
	public static final String FRIENDS_MESSAGE_EDIT = BASEURL
			+ "/app/friendsMessage/saveFriendsMessageApp.htm";
	/** 好友留言->获取留言列表 */
	public static final String FRIENDS_LIUYAN_DATA = BASEURL
			+ "/app/myFriends/getMyFriendsInfoApp.htm?";
	/** 好友数据详情->历史数据 */
	public static final String FRIENDS_HiISTORY_DATA = BASEURL
			+ "/app/bloodPressure/getBloodPressureHisApp.htm?";
	/** 好友数据详情->历史数据折线图 */
	public static final String FRIENDS_CHART_DATA = BASEURL
			+ "/app/bloodPressure/getFriendsBloodPressureByPicApp.htm?";
	/** 系统消息列表 */
	public static final String GET_SYSTEM_MESSAGE_LIST = BASEURL
			+ "/app/sysMessage/getSysMessageByList.htm?";
	/** 系统消息详情 */
	public static final String GET_SYSTEM_MESSAGE_DETAIL = BASEURL
			+ "/app/sysMessage/getSysMessageById.htm?";
	/** 我的设备列表 */
	public static final String MY_DEVICES_LIST = BASEURL
			+ "/app/userEquipment/getUserEquipmentForPageApp.htm?";
	/** 删除我的设备 */
	public static final String MY_DEVICES_DELETE = BASEURL
			+ "/app/userEquipment/deleteUserEquipmentApp.htm?";
	/** 寻医接入 */
	public static final String XUNYI_LOGIN = BASEURL
			+ "/app/seekDoctor/getSeekDoctorLoginOrReg.htm?";

	/** 获取web折线图 */
	public static final String GET_HISTORY_CHART_DATA = BASEURL
			+ "/highchartsApp/bloodPressure/bloodPressureHis.jsp?";
	/** 获取web折线图 */
	public static final String GET_HISTORY_CHART_DATA_DOWN = BASEURL
			+ "/highchartsApp/bloodPressure/bloodPressureDiff.jsp?";
	/** 获取web结果页折线图 */
	public static final String GET_DATA_CHART = BASEURL
			+ "/highchartsApp/bloodPressure/bloodPressureResult.jsp?";
	/** 获取web结果页折线图 */
	public static final String GET_DATA_CHART_DOWN = BASEURL
			+ "/highchartsApp/bloodPressure/bloodPressureDiffResult.jsp?";

}
