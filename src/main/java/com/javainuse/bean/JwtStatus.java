package com.javainuse.bean;

public class JwtStatus {

	private String msg;
	private boolean success;
	
	public JwtStatus() {
		super();
	}

	public JwtStatus(String msg, boolean success) {
		super();
		this.msg = msg;
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
