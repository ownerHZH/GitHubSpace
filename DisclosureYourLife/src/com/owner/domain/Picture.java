package com.owner.domain;

import java.io.Serializable;

public class Picture implements Serializable {

	private static final long serialVersionUID = 2117093800370552802L;
	private int pid;	
	private int eid;
	private String ppath;
	private String pname;
	private String ppic;
	
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getEid() {
		return eid;
	}
	public void setEid(int eid) {
		this.eid = eid;
	}
	public String getPpic() {
		return ppic;
	}
	public void setPpic(String ppic) {
		this.ppic = ppic;
	}
	public String getPpath() {
		return ppath;
	}
	public void setPpath(String ppath) {
		this.ppath = ppath;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	@Override
	public String toString() {
		return "Picture [pid=" + pid + ", eid=" + eid + ", ppath=" + ppath
				+ ", pname=" + pname + ", ppic=" + ppic + "]";
	}


}
