package com.babbler.ws.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import com.babbler.ws.io.kafka.model.BabblerKafkaConsumer;
import com.babbler.ws.io.kafka.model.BabblerKafkaProducersPool;
import com.babbler.ws.io.kafka.model.BabblerKafkaSupervisor;
import com.babbler.ws.io.kafka.settings.BabblerKafkaSettings;
import com.babbler.ws.ui.options.BabblerRequestedOperation;
import com.babbler.ws.ui.options.UserRequestedOperation;
import com.babbler.ws.ui.responses.BasicResponse;
import com.babbler.ws.ui.responses.GetBabbleResponse;
import com.babbler.ws.ui.responses.PostBabbleResponse;
import com.babbler.ws.utilities.exceptions.BabblerExceptionCodes;
import com.babbler.ws.utilities.settings.BabblerSettings;
/**
 * Class used for managing the requests coming from a User at a given time
 * @author Luca Lagni
 *
 */
public class ClientRequestHandler {
	//Nickname associated to the user
	private String nickname = null;
	//List of babbles retrieved with the query
	private ArrayList<Babble> babbles = null;
	//List of settings associated to the kafka side application
	private BabblerKafkaSettings kafkaSettings = null;
	//List of settings associated to babbler side of the application
	private BabblerSettings babblerSettings = null;
	
	public ClientRequestHandler(String nickname) {
		this.nickname = nickname;
		this.babbles = new ArrayList<Babble>();
		this.kafkaSettings = BabblerKafkaSettings.getInstance();
		this.babblerSettings = BabblerSettings.getInstance();
	}
	
	/**
	 * Method used for sorting the provided babbles
	 * @param babbles
	 */
	private void sortAndFillBabbles(HashSet<Babble> babbles,String filterType,String filterValue) {
		this.babbles = new ArrayList<Babble>();
		
		
		for(Babble b : babbles) {
			//Filtering based on location
			if(filterType.equals(this.babblerSettings.getLocationFilterType())) {
				if(b.getLocation().equals(filterValue)) {
					boolean same = false;
					for(Babble bi : this.babbles) {
						if(bi.equals(b)){
							same = true;
							break;
						}
					}
					
					if(!same) {
						this.babbles.add(b);
					}
					
				}
				continue;
			}
			
			//Filtering based on tags
			if(filterType.equals(this.babblerSettings.getTagsFilterType())) {
				for(String tag : b.getTags()) {
					if(tag.equals(filterValue)) {
						
						boolean same = false;
						for(Babble bi : this.babbles) {
							if(bi.equals(b)){
								same = true;
								break;
							}
						}
						
						if(!same) {
							this.babbles.add(b);
							break;
						}
					}
				}
				continue;
			}
			
			//Filtering based on mentions
			if(filterType.equals(this.babblerSettings.getMentionsFilterType())) {
				for(String mention : b.getMentions()) {
					if(mention.equals(filterValue)) {
						
						boolean same = false;
						for(Babble bi : this.babbles) {
							if(bi.equals(b)){
								same = true;
								break;
							}
						}
						
						if(!same) {
							this.babbles.add(b);
							break;
						}
					}
				}
				
				continue;
			}
			
		}
		
		//Sort based on the timestamp
		Collections.sort(this.babbles, 
				new Comparator<Babble>() {

					@Override
					public int compare(Babble b1, Babble b2) {
						return b1.getTimestamp().compareTo(b2.getTimestamp());
					}
			
				}
		);
		
	}
	
	/**
	 * Method used for authenticate the user associated to a request
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	public boolean authenticateUser() throws InterruptedException, ExecutionException, TimeoutException {
		boolean authenticated = true;
		
		BabblerKafkaSupervisor supervisor = new BabblerKafkaSupervisor(this.nickname,BabblerRequestedOperation.AUTHENTICATE_USER);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<Boolean> authenticateFuture = executor.submit(supervisor);
		
		authenticated = authenticateFuture.get(this.babblerSettings.getTaskTimeout(), this.babblerSettings.getTaskTimeoutTimeunit());
		
		executor.shutdown();
		
		return authenticated;
	}
	

	
	/**
	 * Method used for subscribing a new user
	 * @return
	 */
	public BasicResponse subscribe() {
		BasicResponse response = new BasicResponse();
		
		boolean available = false;
		
		BabblerKafkaConsumer consumer = new BabblerKafkaConsumer(this.nickname, this.kafkaSettings.getBabblerUserTopic(), "0" , UserRequestedOperation.SUBSCRIBE);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<HashSet<Babble>> future = executor.submit(consumer);
		
		
		response.setNickname(this.nickname);
		response.setOperation(UserRequestedOperation.SUBSCRIBE.getOperation());
		
		try {
			available = this.authenticateUser();
			if(!available) {
				future.get(60000, this.babblerSettings.getTaskTimeoutTimeunit());
				if(this.authenticateUser()) {
					response.setSuccess(true);
				}else {
					response.setSuccess(false);
					response.setError(BabblerExceptionCodes.INTERNAL_SERVER_ERROR.getCode());
				}
				
			}else {
				response.setSuccess(false);
				response.setError(BabblerExceptionCodes.NICKNAME_NOT_AVAILABLE.getCode());
			}
			
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			future.cancel(true);
			response.setSuccess(false);
			response.setError(e.getMessage());
		} finally {
			executor.shutdown();
		}
			
		return response;
	}
	
	/**
	 * Method used for reading in a Batch way all the babbles related to a
	 * certain topic
	 * @param topic
	 */
	public GetBabbleResponse read(String filterType,String filterValue) {
		GetBabbleResponse response = new GetBabbleResponse();
		HashSet<Babble> tmp = null;
		
		BabblerKafkaConsumer consumer = new BabblerKafkaConsumer(this.nickname, this.kafkaSettings.fromFilterToTopic(filterType), filterValue, UserRequestedOperation.READ_BATCH);
		System.out.println(this.kafkaSettings.fromFilterToTopic(filterType));
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<HashSet<Babble>> future = executor.submit(consumer);
		boolean authenticated = false;
		
		//Creating the response
		response.setNickname(this.nickname);
		response.setFilterType(filterType);
		response.setFilterValue(filterValue);
		response.setOperation(UserRequestedOperation.READ_BATCH.getOperation());
			
		try {
			authenticated = this.authenticateUser();
			
			if(authenticated) {
				tmp = future.get(60000, this.babblerSettings.getTaskTimeoutTimeunit());
				this.sortAndFillBabbles(tmp, filterType, filterValue);
				
				response.setSuccess(true);
			}else {
				response.setSuccess(false);
				response.setError(BabblerExceptionCodes.USER_NOT_REGISTRED.getCode());
			}
			
			
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			future.cancel(true);
			response.setSuccess(false);
			response.setError(e.getMessage());
		} finally {
			executor.shutdown();
		}
		
		response.setBabbles(babbles);
		
		return response;
		
	}
	
	/**
	 * Method used for posting a new babble
	 * @param babble
	 * @return
	 */
	public PostBabbleResponse post(Babble babble) {
		BabblerKafkaProducersPool producersPool = BabblerKafkaProducersPool.getInstance();
		PostBabbleResponse response = new PostBabbleResponse();
		boolean authenticated = false;
		
		response.setNickname(this.nickname);
		response.setBabble(babble.toString());
		response.setOperation(UserRequestedOperation.POST_BABBLE.getOperation());
		
		try {
			authenticated = this.authenticateUser();
			
			if(authenticated) {
					response.setSuccess((producersPool.postBabble(babble)) >= 0);

			}else {
				response.setSuccess(false);
				response.setError(BabblerExceptionCodes.USER_NOT_REGISTRED.getCode());
			}
			
			
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			response.setSuccess(false);
			response.setError(e.getMessage());
		} 
		
		
		return response;
	}

}
