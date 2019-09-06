# Babbler
Web Service that uses Confluent/Apache kafka for managing messages exchange between users.

Implement a simplified version of the Twitter social network using Kafka to store tweets. Users interact with Twitter using a client that presents a timeline of tweets and allows users to publish new tweets.

Tweets have the following structure (Avro serialization is recommended but not mandatory):
Tweet <Authors, Content, Timestamp, Location, [Tags], [Mentions]>

Clients can be configured to filter the tweets that appear in the timeline by tag, location, or mentions.

Clients can update their timeline either in batch or in streaming (continuous) mode. In batch mode, the timeline is updated upon user request, while in streaming mode it is constantly updated to reflect the tweets produced in the last 5 minutes.

Timelines are ordered from the least to the most recent tweet, according to their timestamp (event time).

The communication with the client must be RESTful, with tweets represented in JSON format.

REST API:

    Subscribe to twitter - POST /users/id
    Write a new tweet - POST /tweets JSON Body
    Read tweets (polling) - GET /tweets/{filter}/latest
    Server-Sent Event (streaming) - POST /tweets/{filter} JSONBody [List of tweets]

Requirements:

    Exactly once semantics: a tweet is always stored and presented in timelines exactly once.
    Upon restart/failure clients should not read all the posts from scratch.

Assumptions:

    Clients’ disks are reliable.
    Kafka Topics with replication factor > 1 are reliable.
    
The project uses:
- Apache Maven for the libraries management
- Apache Kafka for the clients subscription and messages exchange / storage
- Apache Zookeeper for the coordination between Kafka Clusters
- Confluent schema-registry for the management of the (AVRO) schemas 
- Jersey framework for the management of the REST API and Server-Sent Event

Useful tools for testing the project are:
- Kafka tools 2 
- Postman (REST API)
- Curl (for the server sent event)


