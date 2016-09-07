package com.app.net.res;


import java.io.Serializable;

/** 返回基类 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResult implements Serializable {
	// 状态码
	private String code;
	// 消息
	private String msg;
	private boolean succ;
	/** 有效期7天 */
	private String token;

	/** 翻页 */
	private Paginator paginator;
	/** 客户端上传的id，需要服务器返回 */
	private String clientStr;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSucc() {
		return succ;
	}

	public void setSucc(boolean succ) {
		this.succ = succ;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public String getClientStr() {
		return clientStr;
	}

	public void setClientStr(String clientStr) {
		this.clientStr = clientStr;
	}
}
