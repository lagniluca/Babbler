package com.babbler.ws.ui.options;

import java.io.Serializable;

/**
 * Enum that lists all the available filter types for the Babbler web service interface 
 * 
 * @author Luca Lagni
 *
 */
public enum BabblerRequestsFilterTypes implements Serializable{
	
	//Filter type associated to no specific Babble field
	BROADCAST_FILTER_TYPE("broadcast"),
	LOCATION_FILTER_TYPE("location"),
	MENTIONS_FILTER_TYPE("mention"),
	TAGS_FILTER_TYPE("tag");
	
	private String filterType = null;
	
	BabblerRequestsFilterTypes(String filterType){
		this.setFilterType(filterType);
	}

	public String getFilterType() {
		return filterType;
	}

	private void setFilterType(String filterType) {
		this.filterType = filterType;
	}
	
	
}
