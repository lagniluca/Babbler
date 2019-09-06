package com.babbler.ws.io.kafka.model;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.ProducerFencedException;

import com.babbler.ws.io.avro.model.BabbleKey2;
import com.babbler.ws.io.avro.model.BabbleValue2;
import com.babbler.ws.io.kafka.partitioning.BabblerPartitioner;
import com.babbler.ws.io.kafka.settings.BabblerKafkaSettings;
import com.babbler.ws.model.Babble;
import com.babbler.ws.utilities.settings.BabblerSettings;

import io.confluent.kafka.serializers.KafkaAvroSerializer;

/**
 * Class used for posting Babbles on the Kafka cluster
 * This class is buffered so we can decide to wait to collect multiple Babbles before posting them for optimization purposes
 * 
 * @author Lagni Luca
 *
 */
class BufferedBabblerKafkaProducer extends Thread{
	private BabblerSettings babblerSettings = null;
	private BabblerKafkaSettings kafkaSettings = null;
	//Set of babbles that the producer have to collect before sending them to the cluster
	private volatile Babble[] babbleBuffer = null;
	private KafkaProducer<BabbleKey2, BabbleValue2> producer = null;
	private volatile boolean bufferFull = false;
	private volatile boolean stopProducer = false;
	
	public BufferedBabblerKafkaProducer() {
		this.babblerSettings = BabblerSettings.getInstance();
		this.kafkaSettings = BabblerKafkaSettings.getInstance();
		//Creating the buffer of babbles
		this.babbleBuffer = new Babble[this.babblerSettings.getBubblesBufferSize()];
		
		this.producer = this.createProducer();
	}
	
	/**
	 * Method used for adding a babble to the buffer in case it is not full
	 * @param babble
	 * @return
	 */
	public synchronized boolean addBabble(Babble babble) {
		boolean performed = false ;
		
		if(!this.bufferFull) {
			int i = 0;
			
			for(i = 0; i < babbleBuffer.length; i++) {
				if(this.babbleBuffer[i] == null) {
					babbleBuffer[i] = babble;
					break;
				}
			}
			
			performed = true;
			
			if(i == (this.babbleBuffer.length - 1)) {
				this.bufferFull = true;
			}
		}
		
		return performed;
		
	}
	
	/**
	 * Method used for stopping the current producer
	 * @return
	 */
	public synchronized boolean stopProducer() {
		this.stopProducer = true;
		
		return this.stopProducer;
	}
	
	private KafkaProducer<BabbleKey2, BabbleValue2> createProducer(){
		Properties props = new Properties();
		
		/********** BASIC PRODUCER SETTINGS **********/
		//Address of the bootstrap server
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		//props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "171.31.9.1:9092");
		//Kind of message key serializer
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
		//Kind of message value serializer
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
		//Address of the schema registry
		props.put("schema.registry.url", "http://127.0.0.1:8081");
		//props.put("schema.registry.url", "http://171.31.19.230:8081");
		
		
		/********** SAFE PRODUCER SETTINGS **********/
		//We are stating that we want the producer to write the message only once
		props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		//ID for the transaction in order to enable idempotence
		props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, Long.toString(System.currentTimeMillis()));
		//Here we want all the replicas acknowledgement
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		//Number of retries we want to perform in case of errors
		props.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
		//Configuration for ensuring ordering (default = 5),
		//setting this value to 1 we are sure that the messages will be ordered based on the key
		props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");
		
		/********** OPTIMIZATION PRODUCER SETTINGS **********/
		//Type of compression applied to batches
		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
		//Linger is used for waiting some milliseconds in order to collect more than one messages and send them to kafka in batch mode
		props.put(ProducerConfig.LINGER_MS_CONFIG, "20"); //20 milliseconds of delay
		//Batch size if used for defining the maximum size of a Kafka messages batch collection
		//If a message is greather than the batch size , it will not be batch
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024)); //32KB batch size
		
		/********** WORKLOAD-PEAKS PRODUCER SETTINGS **********/
		//Case in which the producer produce data way faster than what the broker can manage
		props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "60000"); //60s of blocking of the producer before rising an exception
		
		props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, BabblerPartitioner.class.getName()); //60s of blocking of the producer before rising an exception


		return new KafkaProducer<BabbleKey2, BabbleValue2>(props);
	}
	
	/**
	 * Method used for creating producer records based on the producer-partition
	 * @param message
	 * @return
	 */
	private ArrayList<ProducerRecord<BabbleKey2,BabbleValue2>> createProducerRecords(){
		ArrayList<ProducerRecord<BabbleKey2, BabbleValue2>> records = new ArrayList<ProducerRecord<BabbleKey2, BabbleValue2>>();
		
		for(Babble babble : this.babbleBuffer) {
			BabbleValue2 bValue;
			ArrayList<String> bTags = new ArrayList<String>();
			ArrayList<String> bMentions = new ArrayList<String>();
			
			for(String tag : babble.getTags()) {
				bTags.add(tag);
			}
			
			for(String mention : babble.getMentions()) {
				bMentions.add(mention);
			}
			
			bValue = BabbleValue2.newBuilder()
					.setAuthor(babble.getAuthor())
					.setContent(babble.getContent())
					.setTimestamp(babble.getTimestamp())
					.setLocation(babble.getLocation())
					.setTags(bTags)
					.setMentions(bMentions)
					.build();
			
			//Creating the record associated to the location key
			if(!(babble.getLocation().isEmpty() || (babble.getLocation() == null))) {
				BabbleKey2 lKey = BabbleKey2.newBuilder()
											.setKeyword(babble.getLocation())
											.build();
				records.add(new ProducerRecord<BabbleKey2,BabbleValue2>(this.kafkaSettings.getBabblerLocationTopic(), lKey, bValue));
			}
			
			//Creating producer records associated to tags keys
			for(String tag : babble.getTags()) {
				BabbleKey2 tKey = BabbleKey2.newBuilder()
										   .setKeyword(tag)
										   .build();
				
				records.add(new ProducerRecord<BabbleKey2,BabbleValue2>(this.kafkaSettings.getBabblerTagsTopic(), tKey, bValue));
				
			}
			
			//Creating producer records associated to mentions keys
			for(String mention : babble.getMentions()) {
				BabbleKey2 mKey = BabbleKey2.newBuilder()
										    .setKeyword(mention)
										    .build();
						
				records.add(new ProducerRecord<BabbleKey2,BabbleValue2>(this.kafkaSettings.getBabblerMentionsTopic(), mKey, bValue));
						
			}
			
		}
		
		return records;
	}
	
	@Override
	public void run(){
		
		while(!this.stopProducer) {
			
			if(this.bufferFull) {
				this.producer = this.createProducer();
				ArrayList<ProducerRecord<BabbleKey2,BabbleValue2>> records = null;
				
				records = this.createProducerRecords();
				
				this.producer.initTransactions();
					
				try {
						
					this.producer.beginTransaction();
					
					for(ProducerRecord<BabbleKey2, BabbleValue2> record : records) {
												
						Future<RecordMetadata> metadata = this.producer.send(record, new Callback() {

							@Override
							public void onCompletion(RecordMetadata metadata, Exception exception) {
								if(exception != null) {
									
									throw new NullPointerException(exception.getMessage());
								}
								
							}
							
						});
						
						try {
							RecordMetadata meta = metadata.get();
							System.out.println("topic: " + record.topic() + " key:" + record.key() + " partition:" + meta.partition());
						} catch (InterruptedException | ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					//The producer is preparing for sending the record to the cluster
					this.producer.commitTransaction();
					
					this.producer.close();
					} catch( ProducerFencedException e) {
						this.finalize();
					} catch( KafkaException e ) {
					    producer.abortTransaction();
					} catch(NullPointerException ex) {
						ex.printStackTrace();
					} finally {
						this.babbleBuffer = new Babble[this.babblerSettings.getBubblesBufferSize()];
						this.bufferFull = false;
					}
				}
			}
		
		this.finalize();
		
		}
	
	@Override
	protected void finalize() {
		if(this.producer != null) {
			this.producer.close();
		}
	}
}
	