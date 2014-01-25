package com.owner.domain;

import java.io.Serializable;

public class Spinner implements Serializable {
	private static final long serialVersionUID = 7425634029228551982L;
    private int id;
    private int sid;
    private String value;
    
    public Spinner(){}
    
    public Spinner(int id,String value){
    	this.sid=id;
    	this.value=value;
    }
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "Spinner [id=" + id + ", sid=" + sid + ", value=" + value + "]";
	}
    
	
    
}
