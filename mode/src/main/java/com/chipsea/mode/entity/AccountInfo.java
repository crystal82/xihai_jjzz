package com.chipsea.mode.entity;

import java.io.Serializable;

public class AccountInfo implements Serializable {

	/**
	 * 账号信息包括角色信息、设备信息、角色数据
	 */

	private String userName;
	private String nickName;
	private String password;
	private String uid;
	private String token;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
