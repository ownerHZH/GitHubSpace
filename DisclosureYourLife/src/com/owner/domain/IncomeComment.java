package com.owner.domain;

import java.io.Serializable;

public class IncomeComment implements Serializable {
	private static final long serialVersionUID = -2337459230472967102L;
    private int id;
    private int iid;
    private String comment;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIid() {
		return iid;
	}
	public void setIid(int iid) {
		this.iid = iid;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "IncomeComment [id=" + id + ", iid=" + iid + ", comment="
				+ comment + "]";
	}
}
