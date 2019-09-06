package com.babbler.ws.utilities.settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Class that contains the settings of the Babbler side of the application
 * @author Luca Lagni
 *
 */
public class BabblerSettings implements Serializable {
	private static final long serialVersionUID = 1757906231287644465L;
	private static BabblerSettings instance = null;
	
	/********** DEFUALT **********/
	//Default timeout before request expiration
	private final Integer DEFAULT_TASK_TIMEOUT = 5000;
	private final TimeUnit DEFAULT_TASK_TIMEOUT_TIMEUNIT = TimeUnit.MILLISECONDS;
	
	//Default timeouts in case of server sent events
	private final Integer DEFAULT_SSE_SLEEP_MS = 1000;
	private final Integer DEFAULT_SSE_ITERATIONS = 60*5;
	
	private final Integer BABBLER_CONCURRENT_PRODUCERS = 4;
	private final Integer BABBLES_BUFFER_SIZE = 1;
	
	/********** AVAILABLE FILTER TYPES **********/
	private final String LOCATION_FILTER_TYPE = "location";
	private final String MENTIONS_FILTER_TYPE = "mentions";
	private final String TAGS_FILTER_TYPE = "tags";
	
	public String getLocationFilterType() {
		return LOCATION_FILTER_TYPE;
	}

	public String getMentionsFilterType() {
		return MENTIONS_FILTER_TYPE;
	}

	public String getTagsFilterType() {
		return TAGS_FILTER_TYPE;
	}
	
	public ArrayList<String> getRequestsFilterTypes(){
		ArrayList<String> filterTypes = new ArrayList<String>();
		
		filterTypes.add(LOCATION_FILTER_TYPE);
		filterTypes.add(TAGS_FILTER_TYPE);
		filterTypes.add(MENTIONS_FILTER_TYPE);
		
		return filterTypes;
	}

	private BabblerSettings() {}
	
	public Integer getTaskTimeout() {
		return DEFAULT_TASK_TIMEOUT;
	}
	
	public TimeUnit getTaskTimeoutTimeunit() {
		return DEFAULT_TASK_TIMEOUT_TIMEUNIT;
	}
	
	public Integer getSSESleepMS() {
		return this.DEFAULT_SSE_SLEEP_MS;
	}
	
	public Integer getSSEIterations() {
		return this.DEFAULT_SSE_ITERATIONS;
	}
	
	public Integer getBubblesBufferSize() {
		return this.BABBLES_BUFFER_SIZE;
	}
	
	public Integer getConcurrentProducers() {
		return this.BABBLER_CONCURRENT_PRODUCERS;
	}
	
	public static BabblerSettings getInstance() {
		if(instance == null) {
			synchronized(BabblerSettings.class) {
				instance = new BabblerSettings();
			}
			
		}
		
		return instance;
	}

}
