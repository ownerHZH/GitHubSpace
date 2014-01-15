package com.owner.domain;

import java.io.Serializable;

public class Consume implements Serializable {

	private static final long serialVersionUID = 1633547296350812572L;
	private int cid;	
	private int uid;
	private String name;
	private Double money;
	
	public Consume(){}
	
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
	@Override
	public String toString() {
		return "Consume [cid=" + cid + ", uid=" + uid + ", name=" + name
				+ ", money=" + money + "]";
	}
	
	public Consume(int uid, String name, Double money) {
		super();
		this.uid = uid;
		this.name = name;
		this.money = money;
	}


}
