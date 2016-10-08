package com.app.net.req;

import java.io.Serializable;

public class BaseReq implements Serializable {
	/**
	 * 服务商编码
	 **/
	public String spid = "1001";
	private String pwd = "ahigZ8M3";
	/**
	 * 终端ip
	 **/
	public String oper = "127.0.0.1";
	public String channel = "21";
	/**
	 * 随机码
	 */
	public String random = "1234";
	//private String token = Constants.getToken();

	/**
	 * 校验码 sign = MD5(MD5(PASSWORD) + SPID + RANDOM)
	 **/
	//private String sign = Md5Utile.encode((Md5Utile.encode(pwd)
	//		+ spid + random));
	public String sign = "3f52f63fad63c5dd209d28420977fb5d";

}
