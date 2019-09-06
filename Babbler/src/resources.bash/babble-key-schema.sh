#!/bin/bash

#script used for sending a new schema to the confluent schema registry using a bash script

curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"schema" : "{\"type\":\"record\",\"name\":\"BabbleKey2\",\"namespace\":\"com.babbler.ws.io.avro.model\",\"doc\":\"Avro schema used for representing a babble on Kafka\",\"fields\":[{\"name\":\"keyword\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"keyword that identify the partition for the babble\"}]}"}' \
http://localhost:8081/subjects/Kafka-key/versions

# {"id":62}
# curl -X GET http://localhost:8081/schemas/ids/62