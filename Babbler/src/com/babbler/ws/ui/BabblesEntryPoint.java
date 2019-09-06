package com.babbler.ws.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

import com.babbler.ws.model.Babble;
import com.babbler.ws.model.ClientRequestHandler;
import com.babbler.ws.ui.options.UserRequestedOperation;
import com.babbler.ws.ui.responses.BasicResponse;
import com.babbler.ws.ui.responses.GetBabbleResponse;
import com.babbler.ws.ui.responses.PostBabbleResponse;
import com.babbler.ws.ui.validators.BabbleValidator;
import com.babbler.ws.utilities.exceptions.BabblerException;
import com.babbler.ws.utilities.exceptions.BabblerExceptionCodes;
import com.babbler.ws.utilities.settings.BabblerSettings;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class used for managing the publishing and reading of Babbles on the Babbler platform
 * 
 * @author Luca Lagni
 *
 */
@Path("babbles") //base path for the babbler operations concerning Babbles
public class BabblesEntryPoint {
	
	@POST //Method used for posting a new Babble
	@Consumes(MediaType.APPLICATION_JSON) //Type of data read by the method
	@Produces(MediaType.APPLICATION_JSON) //Type of data produced by the method
	/**
	 * Method used for posting a new Babble
	 * @param jsonBody
	 * @return
	 */
	public Response postBabble(String jsonBody) {
		PostBabbleResponse postResponse = new PostBabbleResponse();
		//Reponse to send to the client
		Response response = null;
		//Json version of the response
		String jsonResponse = "";
		//Encoding the Babble into a json string
		ObjectMapper mapper = new ObjectMapper();
		//Nickname of the user
		String nickname = null;
		ClientRequestHandler handler = null;
		
		postResponse.setOperation(UserRequestedOperation.POST_BABBLE.getOperation());
		
		try {
			Babble babble = mapper.readValue(jsonBody, Babble.class);
			
			BabbleValidator.validateBabbleWithException(babble);
			
			nickname = babble.getAuthor();
			handler = new ClientRequestHandler(nickname);
			postResponse = handler.post(babble);
			
			//Converting the response in json
			jsonResponse = mapper.writeValueAsString(postResponse);
			
			response = Response.ok().entity(jsonResponse).build();
			
		} catch (IOException | BabblerException e) {
			
			if(nickname != null) {
				postResponse.setNickname(nickname);
			}else {
				postResponse.setNickname("");
			}
			
			postResponse.setBabble(jsonBody);
			postResponse.setSuccess(false);
			postResponse.setError(e.getMessage());
			
			try {
				jsonResponse = mapper.writeValueAsString(postResponse);
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			} finally{
				response = Response.status(Response.Status.EXPECTATION_FAILED).entity(jsonResponse).build();
			}
			
		}
		
		return response;
	}
	
	@GET
	@Path("/{filter_type}/{filter_value}/latest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 * Method used for getting babbles in a Batch mode based on the filter applied
	 * @param filter
	 * @param jsonBody
	 * @return
	 * @throws JsonProcessingException
	 */
	public Response readBabblesBatch(@Context HttpHeaders headers, @PathParam("filter_type") String filterType, @PathParam("filter_value") String filterValue) throws JsonProcessingException {
		String nickname = null;
		ObjectMapper mapper = new ObjectMapper();
		GetBabbleResponse pollingReadResponse = null;
		Response response = null;
		ClientRequestHandler handler = null;
		
		
		try {
			nickname = headers.getHeaderString("nickname");
			handler = new ClientRequestHandler(nickname);
			
			//Checking if the filter type is a recognized one
			boolean filterTypeRecognized = false;
			for(String ft : BabblerSettings.getInstance().getRequestsFilterTypes()) {
				if(ft.equals(filterType)) {
					filterTypeRecognized = true;
					break;
				}
			}
			//...if not I throw an exception
			if(!filterTypeRecognized) {
				throw new BabblerException(BabblerExceptionCodes.INVALID_REQUEST_FILTER_TYPE.getCode());
			}
			
			//Checking if the user is a registred one or not
			pollingReadResponse = handler.read(filterType,filterValue);
			
			//Converting the response in json format
			String jsonResponse = mapper.writeValueAsString(pollingReadResponse);
			
			//Creating the response
			response = Response.ok().entity(jsonResponse).build();
			
		} catch (IOException | BabblerException e) {
			pollingReadResponse = new GetBabbleResponse();
			if(nickname != null)
				pollingReadResponse.setNickname(nickname);
			else
				pollingReadResponse.setNickname("");
			pollingReadResponse.setOperation(UserRequestedOperation.READ_BATCH.getOperation());
			pollingReadResponse.setFilterValue(filterValue);
			pollingReadResponse.setFilterValue(filterValue);
			pollingReadResponse.setSuccess(false);
			pollingReadResponse.setBabbles(new ArrayList<Babble>());
			pollingReadResponse.setError(e.getMessage());
			
			String jsonResponse = mapper.writeValueAsString(pollingReadResponse);
			
			//Creating the response
			response = Response.status(Response.Status.EXPECTATION_FAILED).entity(jsonResponse).build();
		}
		
		return response ;
		
	}
	
	@GET
	@Path("/{filter_type}/{filter_value}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(SseFeature.SERVER_SENT_EVENTS)
	/**
	 * Method used for reading the babbles in a streaming way
	 * 
	 * @param headers
	 * @param filter
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public EventOutput readBabblesStreaming(@Context HttpHeaders headers, @PathParam("filter_type") String filterType, @PathParam("filter_value") String filterValue) throws JsonParseException, JsonMappingException, IOException {
		final String nickname = headers.getHeaderString("nickname");
		final EventOutput eventOutput = new EventOutput();
		ObjectMapper mapper = new ObjectMapper();
		ClientRequestHandler handler = null;
		BabblerSettings babblerSettings = BabblerSettings.getInstance();
		
		try {
			
			handler = new ClientRequestHandler(nickname);
			
			if(!handler.authenticateUser()) {
				throw new BabblerException(BabblerExceptionCodes.USER_NOT_REGISTRED.getCode());
			}
			
			//Checking if the filter type is a recognized one
			boolean filterTypeRecognized = false;
			for(String ft : BabblerSettings.getInstance().getRequestsFilterTypes()) {
				if(ft.equals(filterType)) {
					filterTypeRecognized = true;
					break;
				}
			}
			//...if not I throw an exception
			if(!filterTypeRecognized) {
				throw new BabblerException(BabblerExceptionCodes.INVALID_REQUEST_FILTER_TYPE.getCode());
			}
			
			 new Thread(new Runnable() {
		            @Override
		            public void run() {
		                try {
		                    for (int i = 0; i < babblerSettings.getSSEIterations(); i++) {
		                        Thread.sleep(babblerSettings.getSSESleepMS());
		                        
		                        final OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
		                        eventBuilder.mediaType(MediaType.APPLICATION_JSON_TYPE);
		                        
		                        ClientRequestHandler handler = new ClientRequestHandler(nickname);
		            			GetBabbleResponse pollingReadResponse = handler.read(filterType, filterValue);
		            			String responseString = mapper.writeValueAsString(pollingReadResponse);
		                        
		                        eventBuilder.data(String.class, responseString);
		                        final OutboundEvent event = eventBuilder.build();
		                        eventOutput.write(event);
		                    }
		                } catch (IOException | InterruptedException e) {
		                    throw new RuntimeException(
		                        "Error when writing the event.", e);
		                } finally {
		                    try {
		                        eventOutput.close();
		                    } catch (IOException ioClose) {
		                        throw new RuntimeException(
		                            "Error when closing the event output.", ioClose);
		                    }
		                }
		            }
		        }).start();
			
		}catch(BabblerException | InterruptedException | ExecutionException | RuntimeException | TimeoutException e) {
			BasicResponse basicResponse = new BasicResponse();
			final OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
			eventBuilder.mediaType(MediaType.APPLICATION_JSON_TYPE);
			
			basicResponse.setNickname(nickname);
			basicResponse.setSuccess(false);
			basicResponse.setOperation(UserRequestedOperation.READ_STREAMING.getOperation());
			basicResponse.setError(e.getMessage());
			
			String jsonResponse = mapper.writeValueAsString(basicResponse);
			eventBuilder.data(String.class, jsonResponse);
			
			final OutboundEvent event = eventBuilder.build();
			
			
			eventOutput.write(event);
			
			eventOutput.close();
			
		}
        
        return eventOutput;
	}

}
