package test.app.net.req;

import java.io.Serializable;

public class BaseReq implements Serializable {
	public String service = "";
	/**
	 * 服务商编码
	 **/
	public String spid = "1001";
	private String pwd = "aAr9MVS9j1";
	/**
	 * 终端ip
	 **/
	public String oper = "127.0.0.1";
	public String channel = "23";
	/**
	 * 随机码
	 */
	public String random = "1234";

	public String format = "JSON";

	public String token;
	//版本号
	public String version = "1.0";
	public String sign;

	public void setToken(String token) {
		this.token = token;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setRandom(String random) {
		this.random = random;
	}

	public String getPwd() {
		return pwd;
	}

	public void setService(String service) {
		this.service = service;
	}

}
