FROM openjdk:8-jdk

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    KAFKA_BOOTSTRAP_SERVERS=127.0.0.1:9092 \
    KAFKA_VERSION=1.0.1 \
    SCALA_VERSION=2.11

WORKDIR /usr/local/kafka-scenario
# download the kafka
RUN  wget -q "http://mirror.bit.edu.cn/apache/kafka/${KAFKA_VERSION}/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz"  -O "/usr/local/kafka-scenario/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz" && tar -xvf /usr/local/kafka-scenario/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz

# copy required packages
ADD start_up.sh /usr/local/kafka-scenario/
ADD kafka-scenario.jar /usr/local/kafka-scenario
ADD docker-entrypoint.sh /

RUN chmod +x /docker-entrypoint.sh

VOLUME /usr/local/kafka-scenario/agent

# port
EXPOSE 8080

#
#ENTRYPOINT /docker-entrypoint.sh

CMD sh /usr/local/kafka-scenario/start_up.sh
