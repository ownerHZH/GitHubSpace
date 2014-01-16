package com.owner.domain;

import java.io.Serializable;
import java.util.Date;

public class EmbarrassComment implements Serializable {
	private static final long serialVersionUID = 5349913321864129369L;
    private int id;
    private int eid;
    private String comment;
    private Date date;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEid() {
		return eid;
	}
	public void setEid(int eid) {
		this.eid = eid;
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
		return "EmbarrassComment [id=" + id + ", eid=" + eid + ", comment="
				+ comment + ", date=" + date + "]";
	}

}
