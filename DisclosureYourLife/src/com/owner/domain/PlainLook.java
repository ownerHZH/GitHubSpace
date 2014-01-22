package com.owner.domain;

import java.io.Serializable;

public class PlainLook implements Serializable{
	private static final long serialVersionUID = 1670674185685283438L;
    private int id;
    private int uid;
    private String path;
    private String title;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "PlainLook [id=" + id + ", uid=" + uid + ", path=" + path
				+ ", title=" + title + "]";
	}	
}
