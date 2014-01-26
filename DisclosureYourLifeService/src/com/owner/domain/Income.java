package com.owner.domain;

import java.io.Serializable;
import java.util.Date;

public class Income implements Serializable {

	private static final long serialVersionUID = -773479086546662490L;
	private int iid;	
	private int uid;
	private String name;
	private Double money;
	private Date date;
	
	public Income(){}
	
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

	public Income(int uid, String name, Double money) {
		super();
		this.uid = uid;
		this.name = name;
		this.money = money;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Income [iid=" + iid + ", uid=" + uid + ", name=" + name
				+ ", money=" + money + ", date=" + date + "]";
	}

}
