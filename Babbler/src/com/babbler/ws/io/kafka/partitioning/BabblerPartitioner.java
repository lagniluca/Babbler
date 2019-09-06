package com.babbler.ws.io.kafka.partitioning;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import com.babbler.ws.io.avro.model.BabbleKey2;

public class BabblerPartitioner implements Partitioner{

	@Override
	public void configure(Map<String, ?> configs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		int partition = 0;
		BabbleKey2 pKey = (BabbleKey2) key;
		List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        int size = partitionInfos.size();
        
        partition = Utils.abs(Utils.murmur2(pKey.getKeyword().getBytes(StandardCharsets.UTF_8))) % size;
		
		
		return partition;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
