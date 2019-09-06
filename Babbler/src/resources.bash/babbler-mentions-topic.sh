#!/bin/bash

#Script used for creating the mentions topic for the babbler application
kafka-topics --create --zookeeper localhost:2181 --replication-factor 3 --partitions 8 --topic babbler_mentions