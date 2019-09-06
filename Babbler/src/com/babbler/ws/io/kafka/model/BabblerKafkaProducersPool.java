package com.babbler.ws.io.kafka.model;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.babbler.ws.model.Babble;
import com.babbler.ws.utilities.settings.BabblerSettings;
import com.babbler.ws.io.kafka.model.BufferedBabblerKafkaProducer;

/**
 * Class used for the reuse Kafka producers in order to reduce the overhead related to thread creation
 * 
 * @author Lagni Luca
 *
 */
public class BabblerKafkaProducersPool {
	private static BabblerKafkaProducersPool instance = null;
	private BabblerSettings babblerSettings = null;
	//Maximum number of producers threads that can be created
	private final int parallelizationLevel ; 
	//Number of concurrent producers that can be created
	private BufferedBabblerKafkaProducer[] bufferedProducers = null;
	private Random random = null;
	private ExecutorService executor = null;

	
	private BabblerKafkaProducersPool() {
		this.random = new Random();
		this.babblerSettings = BabblerSettings.getInstance();
		this.parallelizationLevel = this.babblerSettings.getConcurrentProducers();
		
		//Creation of the buffered producers pool
		this.bufferedProducers = new BufferedBabblerKafkaProducer[this.parallelizationLevel];
		this.executor = Executors.newFixedThreadPool(this.parallelizationLevel);
		
		//creation and running of producers of the pool
		for(int i = 0 ; i < this.bufferedProducers.length; i++) {
			this.bufferedProducers[i] = new BufferedBabblerKafkaProducer();
			this.executor.execute(this.bufferedProducers[i]);
		}
	}
	
	/**
	 * Method used for adding a babble to a random bufferedProducer
	 * @param babble
	 * @return
	 */
	public synchronized int postBabble(Babble babble) {
		int i = (this.bufferedProducers.length - 1);
		Boolean[] visited = new Boolean[this.bufferedProducers.length];
		
		for(int j = 0; j < visited.length; j++) {
			visited[j] = false;
		}
		
		do {
			int designatedProducer = this.random.nextInt(this.bufferedProducers.length);
			if(visited[designatedProducer]) {
				continue;
			}else {
				
				visited[designatedProducer] = true;
				boolean result = this.bufferedProducers[designatedProducer].addBabble(babble);
				
				if(!result) {
					i--;
				}else {
					return designatedProducer;
				}
			}
			
			
		}while(i >= 0);
		
		return -1;
	}
	
	/**
	 * Method used for getting the unique reference of the producers pool
	 * @return
	 */
	public static BabblerKafkaProducersPool getInstance() {
		if(instance == null) {
			synchronized(BabblerKafkaProducersPool.class) {
				instance = new BabblerKafkaProducersPool();
			}
		}
		
		return instance ;
	}
	
	/**
	 * Method used for closing the producers pool
	 */
	public synchronized void finalize() {
		
		for(int i = 0; i < this.bufferedProducers.length; i++) {
			this.bufferedProducers[i].stopProducer();
			this.bufferedProducers[i].interrupt();
		}
		
		this.executor.shutdown();
		instance = null;	
	}

}
