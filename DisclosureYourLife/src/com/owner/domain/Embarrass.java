package com.owner.domain;

import java.io.Serializable;
import java.util.List;

public class Embarrass implements Serializable {

	private static final long serialVersionUID = 1388322648498345370L;
	private int eid;	
	private int uid;
	private String etitle;
	private String edesc;
	private String epic;
	private List<Picture> pics;
	
	public Embarrass(){}
	
	public int getEid() {
		return eid;
	}
	public void setEid(int eid) {
		this.eid = eid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getEtitle() {
		return etitle;
	}
	public void setEtitle(String etitle) {
		this.etitle = etitle;
	}
	public String getEpic() {
		return epic;
	}
	public void setEpic(String epic) {
		this.epic = epic;
	}
	public String getEdesc() {
		return edesc;
	}
	public void setEdesc(String edesc) {
		this.edesc = edesc;
	}
	public List<Picture> getPics() {
		return pics;
	}

	public void setPics(List<Picture> pics) {
		this.pics = pics;
	}

	public Embarrass(int uid, String etitle, String edesc, String epic) {
		super();
		this.uid = uid;
		this.etitle = etitle;
		this.edesc = edesc;
		this.epic = epic;
	}

	@Override
	public String toString() {
		return "Embarrass [eid=" + eid + ", uid=" + uid + ", etitle=" + etitle
				+ ", edesc=" + edesc + ", epic=" + epic + ", pics=" + pics
				+ "]";
	}


}
