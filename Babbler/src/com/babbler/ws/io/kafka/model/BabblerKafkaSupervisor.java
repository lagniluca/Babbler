package com.babbler.ws.io.kafka.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.apache.kafka.common.KafkaFuture;

import com.babbler.ws.io.kafka.settings.BabblerKafkaSettings;
import com.babbler.ws.ui.options.BabblerRequestedOperation;
import com.babbler.ws.utilities.settings.BabblerSettings;

/**
 * Class used for supervising certain aspects of the Babbker Kafka side 
 * 
 * @author Luca Lagni
 *
 */
public class BabblerKafkaSupervisor implements Callable<Boolean>{
	private BabblerKafkaSettings kafkaSettings = null;
	private BabblerSettings babblerSettings = null;
	private AdminClient client = null;
	private BabblerRequestedOperation operation = null;
	private String consumerGroup = null;
	
	public BabblerKafkaSupervisor(String consumerGroup, BabblerRequestedOperation operation) {
		this.kafkaSettings = BabblerKafkaSettings.getInstance();
		this.babblerSettings = BabblerSettings.getInstance();
		this.initClient();
		this.operation = operation;
		this.consumerGroup = consumerGroup;
	}
	
	//Method used for setting the configurations of the supervisor
	private void initClient() {
		HashMap<String, Object> conf = new HashMap<>();
		
		conf.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		//conf.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "171.31.9.1:9092");
		conf.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, this.kafkaSettings.getKafkaRequestTimeoutMs());
		
		this.client = AdminClient.create(conf);
	}
	
	/**
	 * Method that returns all the available consumers groups of the Babbler Application
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 */
	private HashSet<String> retrieveAllConsumersGroups() throws InterruptedException, ExecutionException, TimeoutException {
		HashSet<String> result = null;
		ListConsumerGroupsResult groupsList = this.client.listConsumerGroups();
		
		if(groupsList.all() != null) {
			KafkaFuture<Collection<ConsumerGroupListing>> futureCollection = groupsList.all();
			
			
			if(futureCollection != null) {
				ArrayList<ConsumerGroupListing> groups = (ArrayList<ConsumerGroupListing>) futureCollection.get(this.babblerSettings.getTaskTimeout(), this.babblerSettings.getTaskTimeoutTimeunit());
				
				if((groups != null) && (!groups.isEmpty())) {
					result = new HashSet<String>();
					
					for(ConsumerGroupListing cgl : groups) {
						result.add(cgl.groupId());
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Method used to check if a certain user is already subscribed to Babbler
	 * @param consumerGroup
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 */
	private Boolean availableConsumerGroup() throws InterruptedException, ExecutionException, TimeoutException {
		Boolean available = true;
		
		HashSet<String> res = this.retrieveAllConsumersGroups();
			
		if(res != null) {
			
			for(String group : res) {
				if(group.equals(this.consumerGroup)) {
					available = false;
					break;
				}
			}
		}
		
		return available;
		
	}

	@Override
	public Boolean call() throws InterruptedException, ExecutionException, TimeoutException{
		Boolean result = false;
		
		switch(this.operation) {
		case REGISTER_USER:
			result = this.availableConsumerGroup();
			break;
		case AUTHENTICATE_USER:
			result = !this.availableConsumerGroup();
			break;
		default:
			break;
		}
		
		this.finalize();
		
		return result;
	}
	
	@Override
	protected void finalize() {
		if(this.client != null) {
			this.client.close();
		}
	}

}
