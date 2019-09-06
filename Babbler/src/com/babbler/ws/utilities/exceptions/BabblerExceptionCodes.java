package com.babbler.ws.utilities.exceptions;

import java.io.Serializable;

/**
 * Enum that lists all the exceptions' codes that can be rise by the babbler web service 
 * during its duty
 * 
 * @author Luca Lagni
 *
 */
public enum BabblerExceptionCodes implements Serializable{
	
	//Exception codes associated to babbles
	BABBLE_INVALID_AUTHOR("the author of the passed babble is not valid"),
	BABBLE_INVALID_CONTENT("the content of the passed babble is not valid"),
	BABBLE_INVALID_TIMESTAMP("the timestamp of the passed babble is not valid"),
	BABBLE_INVALID_LOCATION("the location of the passed babble is not valid"),
	BABBLE_INVALID_TAGS("the tags of the passed babble are not valid"),
	BABBLE_INVALID_MENTIONS("the mentions of the passed babble are not valid"),
	
	//Exception codes associated to user registration
	NICKNAME_NOT_AVAILABLE("nickname not available, already taken by someone else"),
	
	//Exception codes associated to user authentication
	USER_NOT_REGISTRED("user not registred yet, cannot perform requests"),
	
	//Exception codes associated to user requests
	MISSING_FIELDS_IN_REQUEST("missing fields in the request"),
	INVALID_REQUEST_FILTER_TYPE("invalid request filter type"),
	INVALID_JSON_PAYLOAD("the system is unable to extract correct data from the passed json payload"),
	
	//Exception codes associated to internal errors
	INTERNAL_SERVER_ERROR("internal server error, unable to perform the request");
	
	private static final long serialVersionUID = -3298125902804510222L;
	
	//Code associated with the UIException
	private String code = null;
	
	BabblerExceptionCodes(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	
}
