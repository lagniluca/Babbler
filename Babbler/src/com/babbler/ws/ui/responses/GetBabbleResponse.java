package com.babbler.ws.ui.responses;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.babbler.ws.model.Babble;

/**
 * Method used for responding back to the user the Babbles retrieved with the Polling read
 * @author Lagni Luca
 *
 */
@XmlRootElement
public class GetBabbleResponse extends BasicResponse{
	//Filter used by the client for reaching babbles of a certain topic
	private String filterType = null;
	//Filter used by the client for reading certain babbles
	private String filterValue = null;
	//Ordered list of babbles retrieved
	private ArrayList<Babble> babbles = null;
	
	public void setOperation(String operation) {
		super.setOperation(operation);
	}
	
	public String getOperation() {
		return super.getOperation();
	}
	
	public String getNickname() {
		return super.getNickname();
	}
	public void setNickname(String nickname) {
		super.setNickname(nickname);
	}
	public String getFilterType() {
		return filterType;
	}
	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}
	public String getFilterValue() {
		return filterValue;
	}
	public void setFilterValue(String filter) {
		this.filterValue = filter;
	}
	public Boolean getSuccess() {
		return super.getSuccess();
	}
	public void setSuccess(Boolean success) {
		super.setSuccess(success);
	}
	public ArrayList<Babble> getBabbles() {
		return babbles;
	}
	public void setBabbles(ArrayList<Babble> babbles) {
		this.babbles = babbles;
	}
	public String getError() {
		return super.getError();
	}
	public void setError(String error) {
		super.setError(error);
	}

}
