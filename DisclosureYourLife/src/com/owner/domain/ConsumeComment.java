package com.owner.domain;

import java.io.Serializable;
import java.util.Date;

public class ConsumeComment implements Serializable {
	private static final long serialVersionUID = -8459510750594761933L;
    private int id;
    private int cid;
    private String comment;
    private Date date;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "ConsumeComment [id=" + id + ", cid=" + cid + ", comment="
				+ comment + ", date=" + date + "]";
	}
	
}
