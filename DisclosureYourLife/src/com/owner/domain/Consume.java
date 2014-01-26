package com.owner.domain;

import java.io.Serializable;
import java.util.Date;

public class Consume implements Serializable {

	private static final long serialVersionUID = 1633547296350812572L;
	private int cid;	
	private int uid;
	private String name;
	private Double money;
	private Date date;
	
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "Consume [cid=" + cid + ", uid=" + uid + ", name=" + name
				+ ", money=" + money + ", date=" + date + "]";
	}
}
