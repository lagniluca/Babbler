package com.babbler.ws.ui.responses;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class used as a Blueprint for response send by the server to the users
 * @author luca
 *
 */
@XmlRootElement
public class BasicResponse {
	//access id of the user
	private String nickname = null;
	//operation requested by the user
	private String operation = null;
	//boolean flag used to define if the request was a success or not
	private Boolean success = null;
	//Possible error related to the request in case of failure
	private String error = "NONE";
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	
}
