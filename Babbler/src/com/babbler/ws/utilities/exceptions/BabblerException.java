package com.babbler.ws.utilities.exceptions;

/**
 * Class used for managing a Babbler exceptions
 * 
 * @author Luca Lagni
 *
 */
public class BabblerException extends Exception{
	private static final long serialVersionUID = -3298125902804510283L;
	
	//Message associated to the exception rised
	private String message = null;
	
	public BabblerException(String message) {
		super(message);
		this.message = message;
	}
	
	/**
	 * Method used for accessing the message associated to the current exception
	 * @return
	 */
	public String getMessage() {
		return this.message;
	}

}
