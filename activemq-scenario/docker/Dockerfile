FROM openjdk:8-jdk

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    ACTIVEMQ_BOOTSTRAP_SERVERS=127.0.0.1:61616 \
    ACTIVEMQ_VERSION=5.15.8

WORKDIR /usr/local/activemq-scenario
# download the activemq
RUN wget -q "http://mirror.bit.edu.cn/apache/activemq/${ACTIVEMQ_VERSION}/apache-activemq-${ACTIVEMQ_VERSION}-bin.tar.gz"  -O "/usr/local/activemq-scenario/apache-activemq-${ACTIVEMQ_VERSION}-bin.tar.gz" && tar -xvf /usr/local/activemq-scenario/apache-activemq-${ACTIVEMQ_VERSION}-bin.tar.gz

# copy required packages
ADD start_up.sh /usr/local/activemq-scenario/
ADD activemq-scenario.jar /usr/local/activemq-scenario
ADD docker-entrypoint.sh /

RUN chmod +x /docker-entrypoint.sh

VOLUME /usr/local/activemq-scenario/agent

# port
EXPOSE 8080

#
#ENTRYPOINT /docker-entrypoint.sh

CMD sh /usr/local/activemq-scenario/start_up.sh
