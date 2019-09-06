#!/bin/bash

#script used for sending a new schema to the confluent schema registry using a bash script

curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"schema" : "{\"type\":\"record\",\"name\":\"BabbleValue2\",\"namespace\":\"com.babbler.ws.io.avro.model\",\"doc\":\"Avro schema used for representing a babble on Kafka\",\"fields\":[{\"name\":\"author\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"authot of the babble\"},{\"name\":\"content\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"content of the babble\"},{\"name\":\"timestamp\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"timestamp of the babble provided by the user\"},{\"name\":\"location\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"location of the babble\"},{\"name\":\"tags\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},\"default\":[]},{\"name\":\"mentions\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},\"default\":[]}]}"}' \
http://localhost:8081/subjects/Kafka-value/versions

# {"id":63}
# curl -X GET http://localhost:8081/schemas/ids/63