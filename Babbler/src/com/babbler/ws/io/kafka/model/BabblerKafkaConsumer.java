package com.babbler.ws.io.kafka.model;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.utils.Utils;

import com.babbler.ws.io.avro.model.BabbleKey2;
import com.babbler.ws.io.avro.model.BabbleValue2;
import com.babbler.ws.io.kafka.settings.BabblerKafkaSettings;
import com.babbler.ws.model.Babble;
import com.babbler.ws.ui.options.UserRequestedOperation;


import io.confluent.kafka.serializers.KafkaAvroDeserializer;

/**
 * Class used for managing the consumer side of the Babbler WS application
 * 
 * @author Luca Lagni
 *
 */
public class BabblerKafkaConsumer implements Callable<HashSet<Babble>>{
	
	//ID of the user associated to a particular consumer
	private String nickname = null;
	//Topic to which the consumer is subscribed
	private String topic = null;
	//Data that a consumer wants to read from a certain topic
	private String target = null;
	//Partition on which the topic data related to target is present
	private TopicPartition partition = null;
	//Reference to the effective kafka consumer
	private KafkaConsumer<BabbleKey2, BabbleValue2> kafkaConsumer = null;
	//Enum used for define which operation we have to perform
	private UserRequestedOperation requestedOperation = null;
	//Singleton used for managing the setting of the kafka side
	private BabblerKafkaSettings kafkaSettings = null;
	private int counter = 0;
	
	/**
	 * Constructor used when we have to update the consumer knowledge of a specific topic
	 * @param nickname
	 * @param topic
	 * @param streamingRead
	 */
	@SuppressWarnings("deprecation")
	public BabblerKafkaConsumer(String nickname,String topic,String target, UserRequestedOperation requestedOperation) {
		this.nickname = nickname;
		this.topic = topic;
		this.target = target;
		this.requestedOperation = requestedOperation;
		this.kafkaSettings = BabblerKafkaSettings.getInstance();
		BabbleKey2 bKey = BabbleKey2.newBuilder()
									.setKeyword(target)
									.build();
		int targetPartition = Utils.abs(Utils.murmur2(bKey.getKeyword().getBytes(StandardCharsets.UTF_8))) % 8;
		this.partition = new TopicPartition(topic, targetPartition);
		
		
		this.kafkaConsumer = this.createConsumer();
		
		if(topic.equals(this.kafkaSettings.getBabblerUserTopic())) {
			this.kafkaConsumer.subscribe(Collections.singleton(this.topic));
		}
		else {
			this.kafkaConsumer.assign(Collections.singleton(this.partition));
			this.kafkaConsumer.seek(this.partition, this.kafkaConsumer.position(this.partition));
		}
		
		this.kafkaConsumer.poll(1);		
	}

	
	/* Method that creates a KafkaConsumer and configures it*/
	private KafkaConsumer<BabbleKey2, BabbleValue2> createConsumer(){
		Properties props = new Properties();
		
		/********** BASIC CONSUMER SETTINGS **********/
		//Bootstap-server address for the consumer
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		//props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "171.31.9.1:9092");
		//Message key serializer configuration
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
		//Message value serializer configuration
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
		//Address of the schema registry
		props.put("schema.registry.url", "http://127.0.0.1:8081");
		//props.put("schema.registry.url", "http://171.31.19.230:8081");
		//Used for specifing that we are working on a specific Avro record (in this case BabbleKey and BabbleValue)
		props.put("specific.avro.reader", "true");
		//Setting used to state that the Kafka consumer must start to read babbles from beginning
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); 
		//group associated to the following consumer
		props.put(ConsumerConfig.GROUP_ID_CONFIG, this.nickname);
		//used to be sure to commit only readed messages
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		//Used for reading babbles in case of only once semantic
		props.put("isolation.level", "read_uncommitted");
		//Maximum poll interval
		props.put("max.poll.interval.ms", 10000);
		
		
		return new KafkaConsumer<BabbleKey2, BabbleValue2>(props);
	}
	
	public String getNickname() {
		return nickname;
	}

	public String getTopic() {
		return topic;
	}

	@Override
	public HashSet<Babble> call()  {
		HashSet<Babble> babbles = null;
		
		switch(this.requestedOperation) {
			case SUBSCRIBE :
				this.kafkaConsumer.poll(Duration.ofMillis(this.kafkaSettings.getConsumerPollingPeriodMS()));
				this.kafkaConsumer.commitSync();
				break;
			case READ_BATCH:
				
				this.counter = 3;
				
				babbles = new HashSet<Babble>();
				boolean found = false;
				Timer timer = new Timer();
				
				timer.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {
						counter--;
						
					}
					
				}, 0, 1000);
				
				do
				{
					
					ConsumerRecords<BabbleKey2, BabbleValue2> records = this.kafkaConsumer.poll(Duration.ofMillis(100));
					
					for(ConsumerRecord<BabbleKey2, BabbleValue2> record : records) 
					{
						Babble babble = new Babble();
							
						babble.setAuthor(record.value().getAuthor());
						babble.setContent(record.value().getContent());
						babble.setTimestamp(record.value().getTimestamp());
						babble.setLocation(record.value().getLocation());
							
						HashSet<String> tags = new HashSet<String>();
						for(String tag : record.value().getTags()) {
							tags.add(tag);
						}
						babble.setTags(tags);
							
						HashSet<String> mentions = new HashSet<String>();
						for(String mention : record.value().getMentions()) {
							mentions.add(mention);
						}
							
						babble.setMentions(mentions);
						babbles.add(babble);
							
						this.kafkaConsumer.commitSync();
						found = true;
						
					}
					
					if(found) {
						break;
					}
					
				}while(this.counter > 0);
				
				timer.cancel();
				
			
				break;
			default:
				break;
		}
		
		this.finalize();
		
		return babbles;
	}
	
	@Override
	protected void finalize() {
		if(this.kafkaConsumer != null) {
			this.kafkaConsumer.close();		}
	}


	@Override
	public String toString() {
		return "BabblerKafkaConsumer [nickname=" + nickname + ", topic=" + topic + ", target=" + target + ", partition="
				+ partition + ", kafkaConsumer=" + kafkaConsumer + ", requestedOperation=" + requestedOperation
				+ ", kafkaSettings=" + kafkaSettings + "]";
	}
	
	
	
	

}
