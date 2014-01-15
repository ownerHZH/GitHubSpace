package com.owner.domain;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 7655485223640823062L;
	private int uid;
	private String phone;
	private String device;
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	@Override
	public String toString() {
		return "User [uid=" + uid + ", phone=" + phone + ", device=" + device
				+ "]";
	}

}
