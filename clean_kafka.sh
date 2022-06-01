#!/bin/bash

KAFKA_DIR="programs/kafka/kafka_2.12-3.2.0"
HOST="localhost:9092"

cd;
cd ${KAFKA_DIR};
./bin/kafka-topics.sh --bootstrap-server ${HOST} -delete --if-exists -topic CLIENT-INFO;
./bin/kafka-topics.sh --bootstrap-server ${HOST} -delete --if-exists -topic CLIENT-REGISTER;
./bin/kafka-topics.sh --bootstrap-server ${HOST} -delete --if-exists -topic STATION-REGISTER;
./bin/kafka-topics.sh --bootstrap-server ${HOST} -delete --if-exists -topic STATION-INFO;
./bin/kafka-topics.sh --bootstrap-server ${HOST} -delete --if-exists -topic STATION-REGISTER-RESPONSE;
./bin/kafka-topics.sh --bootstrap-server ${HOST} -delete --if-exists -topic STATION-UPDATE-REQUEST;
./bin/kafka-topics.sh --bootstrap-server ${HOST} -delete --if-exists -topic R-STATION-REGISTER-RESPONSE;

./bin/kafka-topics.sh --bootstrap-server ${HOST} -create --if-not-exists -topic CLIENT-INFO;
./bin/kafka-topics.sh --bootstrap-server ${HOST} -create --if-not-exists -topic CLIENT-REGISTER;
./bin/kafka-topics.sh --bootstrap-server ${HOST} -create --if-not-exists -topic STATION-REGISTER;
./bin/kafka-topics.sh --bootstrap-server ${HOST} -create --if-not-exists -topic STATION-INFO;
./bin/kafka-topics.sh --bootstrap-server ${HOST} -create --if-not-exists -topic STATION-REGISTER-RESPONSE;
./bin/kafka-topics.sh --bootstrap-server ${HOST} -create --if-not-exists -topic STATION-UPDATE-REQUEST;
./bin/kafka-topics.sh --bootstrap-server ${HOST} -create --if-not-exists -topic R-STATION-REGISTER-RESPONSE;
