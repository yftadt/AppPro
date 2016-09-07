package com.app.net.req;

import com.app.utiles.other.Md5Utile;

import java.io.Serializable;

public class BaseReq implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 服务商编码 **/
	private String spid = "1000";
	private String pwd = "ahigZ8M3";
	/** 终端ip **/
	private String oper = "127.0.0.1";
	private String channel = "21";
	/** 随机码 */
	private String random = "1234";
	//private String token = Constants.getToken();
	/** 客户端上传的消息Id,需要服务器原样返回 */
	private String clientStr;
	// md5后密码：61958f53c02d9bf7f143d0dcd4ceae8d
	/** 校验码 sign = MD5(MD5(PASSWORD) + SPID + RANDOM) **/
	private String sign = Md5Utile.encode((Md5Utile.encode(pwd)
			+ spid + random));

	public String getClientStr() {
		return clientStr;
	}

	public void setClientStr(String clientStr) {
		this.clientStr = clientStr;
	}



	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSpid() {
		return spid;
	}

	public void setSpid(String spid) {
		this.spid = spid;
	}

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getRandom() {
		return random;
	}

	public void setRandom(String random) {
		this.random = random;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
