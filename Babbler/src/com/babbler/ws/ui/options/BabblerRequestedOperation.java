package com.babbler.ws.ui.options;

import java.io.Serializable;

/**
 * Enum used to list all the operations that the Babbler side of the application can perform
 * @author Luca Lagni
 *
 */
public enum BabblerRequestedOperation implements Serializable {
	
	AUTHENTICATE_USER("authenticate user"),
	REGISTER_USER("register user");
	
	private String code = null;
	
	BabblerRequestedOperation(String code){
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
}
