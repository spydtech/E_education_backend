package com.Eonline.Education.exceptions;

public class MandatoryFieldMissingException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String errMsg;
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public MandatoryFieldMissingException(String errMsg) {
		super();
		this.errMsg = errMsg;
	}
	

}
