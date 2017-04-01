package com.example.binghu.router.router.error;

/**
 * Created by binghu on 3/30/17.
 */

public class RouterErr {

	public static final int OK = 0x0;
	public static final int ERR_URL_INVALID = 0x1;
	public static final int ERR_NO_MATCH_PAGE = 0x2;
	public static final int ERR_MULTI_MATCH = 0x3;

	private int errCode;
	private String errMsg;

	public RouterErr(int errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
