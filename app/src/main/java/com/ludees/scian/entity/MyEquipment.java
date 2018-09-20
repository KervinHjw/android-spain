package com.ludees.scian.entity;

/**
 * 我的设备类
 * 
 * @author jiyx
 * 
 */
public class MyEquipment {

	private String equipmentNo;// 设备号
	private int equipmentType;// 用户设备ID
	private int userEquipmentId;// 设备类型1:血压 2：血糖

	public String getEquipmentNo() {
		return equipmentNo;
	}

	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}

	public int getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(int equipmentType) {
		this.equipmentType = equipmentType;
	}

	public int getUserEquipmentId() {
		return userEquipmentId;
	}

	public void setUserEquipmentId(int userEquipmentId) {
		this.userEquipmentId = userEquipmentId;
	}

}
