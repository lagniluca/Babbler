package com.babbler.ws.ui;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.babbler.ws.model.ClientRequestHandler;
import com.babbler.ws.ui.options.UserRequestedOperation;
import com.babbler.ws.ui.requests.SubscribeUserRequest;
import com.babbler.ws.ui.responses.BasicResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class that manages the subscription of a user to the Babbler Web Service
 * 
 * @author Luca Lagni
 *
 */
@Path("users") //Annotation used for defining the path for the user management
public class UsersEntryPoint {
	
	@POST //Type of operation to perform on the invocation of this method
	@Path("/id") //Relative path for calling this resource
	@Consumes(MediaType.APPLICATION_JSON) //Type of payload the method will accept
	@Produces(MediaType.APPLICATION_JSON) //Type of payload the method will return
	//Method used for subscribing to Babbler a new user
	public Response subscribeUser(String jsonRequest) throws JsonProcessingException {
		Response response = null;
		BasicResponse subscribeUserResponse = null;
		//Translate the Json payload into a UserModel object
		ObjectMapper userMapper = new ObjectMapper();
		ClientRequestHandler handler = null;
		
		subscribeUserResponse = new BasicResponse();
		subscribeUserResponse.setOperation(UserRequestedOperation.SUBSCRIBE.getOperation());
		
		try {
			SubscribeUserRequest subscribeUserRequest = userMapper.readValue(jsonRequest, SubscribeUserRequest.class);
			
			handler = new ClientRequestHandler(subscribeUserRequest.getNickname());
			subscribeUserResponse = handler.subscribe();
			
			//Building the response
			String jsonResponse = userMapper.writeValueAsString(subscribeUserResponse);
			response = Response.ok().entity(jsonResponse).build();
			
		} catch (IOException e) {
			
			subscribeUserResponse.setNickname("");
			subscribeUserResponse.setSuccess(false);
			subscribeUserResponse.setError(e.getMessage());
			
			String jsonError = userMapper.writeValueAsString(subscribeUserResponse);
			
			response = Response.status(Response.Status.EXPECTATION_FAILED).entity(jsonError).build();
		}
		
		return response;
	}

}
