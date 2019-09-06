package com.babbler.ws.ui.requests;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class that represents the request for creating a new Babbler user
 * @author Luca Lagni
 *
 */
@XmlRootElement
public class SubscribeUserRequest {
	//Nickname associated to the new user that the client wants to create
	private String nickname = null;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public String toString() {
		return "SubscribeUserRequest [nickname=" + nickname + "]";
	}
}
