package com.babbler.ws.ui.responses;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Response sand back to the user when posting a new Babble on the system
 * @author Luca Lagni
 *
 */
@XmlRootElement
public class PostBabbleResponse extends BasicResponse{
	//Representation of the Babble
	private String babble = null;

	public String getBabble() {
		return babble;
	}

	public void setBabble(String babble) {
		this.babble = babble;
	}
	
	
}
