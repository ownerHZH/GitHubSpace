package com.owner.domain;

import java.io.Serializable;

public class Income implements Serializable {

	private static final long serialVersionUID = -773479086546662490L;
	private int iid;	
	private int uid;
	private String name;
	private Double money;
	
	public int getIid() {
		return iid;
	}
	public void setIid(int iid) {
		this.iid = iid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	@Override
	public String toString() {
		return "Income [iid=" + iid + ", uid=" + uid + ", name=" + name
				+ ", money=" + money + "]";
	}

}
