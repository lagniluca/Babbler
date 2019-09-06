package com.babbler.ws.ui.options;

/**
 * Enum that lists all the possible actions that an external user can ask to perform
 * @author luca
 *
 */
public enum UserRequestedOperation {
	
	SUBSCRIBE("subscribe"),
	POST_BABBLE("post babble"),
	READ_BATCH("read batch"),
	READ_STREAMING("read streaming");
	
	private String operation = null;
	
	UserRequestedOperation(String operation) {
		this.operation = operation;
	}
	
	public String getOperation() {return this.operation;}

}
