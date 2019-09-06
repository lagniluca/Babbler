package com.babbler.ws;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Class used as entrypoint for our Babbler Web Service
 * 
 * @author Luca Lagni
 *
 */
@ApplicationPath("ws") //Base application path for reaching the resources of the web service
public class BabblerWSApplication extends ResourceConfig{
	
	/**
	 * Method used as entrypoint for our Babbler Web service
	 */
	public BabblerWSApplication() {
		packages("com.babbler.ws");
	}

}
