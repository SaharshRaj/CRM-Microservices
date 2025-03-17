package com.crm.exception;

public class CampaignNotFoundException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String msg;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public CampaignNotFoundException(String msg) {
		super(msg);
	}
}
