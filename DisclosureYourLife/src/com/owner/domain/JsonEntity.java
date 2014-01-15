package com.owner.domain;

import java.io.Serializable;
import java.util.List;


public class JsonEntity implements Serializable{

	private static final long serialVersionUID = 8067317319466922523L;
	private int status;
	private String msg;
	private String data;	
	private int total;
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}




	public int getTotal() {
		return total;
	}


	public void setTotal(int total) {
		this.total = total;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Override
	public String toString() {
		return "JsonEntity [status=" + status + ", data=" + data + ", msg="
				+ msg + ", total=" + total + "]";
	}

}
