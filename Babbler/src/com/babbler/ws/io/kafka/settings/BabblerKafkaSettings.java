package com.babbler.ws.io.kafka.settings;

import java.io.IOException;
import java.io.Serializable;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.babbler.ws.parsers.KafkaSettingsXMLParser;
import com.babbler.ws.utilities.settings.BabblerSettings;

/**
 * Signleton class that contains the settings used for using the server side of the Kafka application
 * We have decided to create this as a class instead of an instance in order to allow further different 
 * implementations on how these parameters should be passed to the application
 * @author luca
 *
 */
public class BabblerKafkaSettings implements Serializable{
	private static final long serialVersionUID = -4108070734487576620L;
	//Singleton instance of the current class
	private static volatile BabblerKafkaSettings instance = null;
	
	/**********{ CONNECTION SETTINGS }*********/
	//IP address of the bootstrap server
	private final String DEFAULT_KAFKA_BOOTSTRAP_SERVER_IP = "127.0.0.1";
	//Port of the bootstrap server
	private final Integer DEFAULT_KAKFA_BOOTSTRAP_SERVER_PORT = 9092;
	//Complete address of the bootstrap server
	private final String DEFAULT_KAFKA_BOOTSTRAP_SERVER_ADDRESS = DEFAULT_KAFKA_BOOTSTRAP_SERVER_IP + ":" + Integer.toString(DEFAULT_KAKFA_BOOTSTRAP_SERVER_PORT);
	//IP address of the confluent schema registry
	//private final String DEFAULT_KAFKA_SCHEMA_REGISTRY_IP = "127.0.0.1";
	//Port for the confluent schema registry
	//private final Integer DEFAULT_KAFKA_SCHEMA_REGISTRY_PORT = 8081;
	//Address of the confluent schema registry
	//private final String DEFAULT_KAFKA_SCHEMA_REGISTRY_ADDRESS = "http://" + DEFAULT_KAFKA_SCHEMA_REGISTRY_IP + ":" + DEFAULT_KAFKA_SCHEMA_REGISTRY_PORT ;
	
	//Timeout before closing the connection with the cluster
	private final Integer DEFAULT_KAFKA_REQUEST_TIMEOUT_MS = 1000;
	
	//Topic used in case of users registration
	private final String BABBLER_USERS_TOPIC = "babbler_users";
	//Topic used in case of user reading/posting babbles
	private final String BABBLER_BABBLES_TOPIC = "babbler_babbles";
	
	private final String BABBLER_TAGS_TOPIC = "babbler_tags";
	private final String BABBLER_LOCATION_TOPIC = "babbler_locations";
	private final String BABBLER_MENTIONS_TOPIC = "babbler_mentions";
	
	private final int DEFAULT_TOPIC_PARTITIONS = 8;
	
	//Period of polling
	private final Integer CONSUMER_POLLING_PERIOD_MS = 100;
	
	private BabblerKafkaSettings() {
		
	}
	
	public Integer getKafkaRequestTimeoutMs() {
		return this.DEFAULT_KAFKA_REQUEST_TIMEOUT_MS;
	}
	
	/**
	 * Method that returns the default Kafka bootstrap server address
	 * @return
	 */
	public String getKafkaBootstrapServer() {
		return this.DEFAULT_KAFKA_BOOTSTRAP_SERVER_ADDRESS ;
	}
	
	public int getTopicPartitions() {
		return this.DEFAULT_TOPIC_PARTITIONS;
	}
	
	public String getBabblerUserTopic() {
		return this.BABBLER_USERS_TOPIC;
	}
	
	public String getBabblerBabblesTopic() {
		return this.BABBLER_BABBLES_TOPIC;
	}
	
	public String getBabblerTagsTopic() {
		return this.BABBLER_TAGS_TOPIC;
	}
	
	public String getBabblerLocationTopic() {
		return this.BABBLER_LOCATION_TOPIC;
	}
	
	public String getBabblerMentionsTopic() {
		return this.BABBLER_MENTIONS_TOPIC;
	}
	
	public Integer getConsumerPollingPeriodMS() {
		return this.CONSUMER_POLLING_PERIOD_MS;
	}
	
	public static BabblerKafkaSettings getInstance() {
		if(instance == null) {
			synchronized(BabblerKafkaSettings.class) {
				instance = new BabblerKafkaSettings();
			}
			
		}
		
		return instance ;
	}
	
	/**
	 * Method used for translating a tag to the equivalent kafka topic
	 * @param filterType
	 * @return
	 */
	public synchronized String fromFilterToTopic(String filterType) {
		String result = null;
		
		if(filterType.equals(BabblerSettings.getInstance().getLocationFilterType())) {
			result = this.BABBLER_LOCATION_TOPIC;
		}
		
		if(filterType.equals(BabblerSettings.getInstance().getMentionsFilterType())) {
			result = this.BABBLER_MENTIONS_TOPIC;
		}
		
		if(filterType.equals(BabblerSettings.getInstance().getTagsFilterType())) {
			result = this.BABBLER_TAGS_TOPIC;
		}
		
		return result;
	}

}
