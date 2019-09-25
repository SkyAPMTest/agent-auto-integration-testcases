FROM openjdk:8-jdk

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    PULSAR_SERVICE_URL=pulsar://127.0.0.1:6650 \
    PULSAR_VERSION=2.4.1
WORKDIR /usr/local/pulsar-scenario
# download the pulsar
RUN  wget -q "http://mirror.bit.edu.cn/apache/pulsar/pulsar-${PULSAR_VERSION}/apache-pulsar-${PULSAR_VERSION}-bin.tar.gz" -O "/usr/local/pulsar-scenario/apache-pulsar-${PULSAR_VERSION}-bin.tar.gz" && tar -xvf /usr/local/pulsar-scenario/apache-pulsar-${PULSAR_VERSION}-bin.tar.gz

# copy required packages
ADD start_up.sh /usr/local/pulsar-scenario/
ADD pulsar-scenario.jar /usr/local/pulsar-scenario
ADD docker-entrypoint.sh /

RUN chmod +x /docker-entrypoint.sh

VOLUME /usr/local/pulsar-scenario/agent

# port
EXPOSE 8082

#
#ENTRYPOINT /docker-entrypoint.sh

CMD sh /usr/local/pulsar-scenario/start_up.sh
