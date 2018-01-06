FROM openjdk:8-jdk

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    ROCKETMQ_VERSION=4.2.0 \
    ROCKETMQ_SCENARIO_HOME=/usr/local/rocketMQ-scenario \
    NAME_SRV_HOST=localhost:9876


WORKDIR  ${ROCKETMQ_SCENARIO_HOME}

RUN mkdir -p \
		${ROCKETMQ_SCENARIO_HOME}/rocketMQ-${ROCKETMQ_VERSION}/volumne/logs \
	    ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-${ROCKETMQ_VERSION}/volumne/store \
	    ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-consumer \
	    ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-provider \
	    ${ROCKETMQ_SCENARIO_HOME}/logs \
	    ${ROCKETMQ_SCENARIO_HOME}/agent

RUN curl http://mirror.bit.edu.cn/apache/rocketMQ/${ROCKETMQ_VERSION}/rocketMQ-all-${ROCKETMQ_VERSION}-bin-release.zip -o rocketMQ.zip \
          && unzip rocketMQ.zip -d ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-${ROCKETMQ_VERSION} \
          && rm rocketMQ.zip

COPY runbroker.sh ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-${ROCKETMQ_VERSION}/bin/
COPY runserver.sh ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-${ROCKETMQ_VERSION}/bin/
#
#
#
COPY rocketMQ-consumer.jar ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-consumer
COPY consumer-startup.sh ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-consumer
#
#
#
COPY rocketMQ-provider.jar ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-provider
COPY provider-startup.sh ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-provider
#
#
#
COPY docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh
#
# Volumes
#
VOLUME ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-${ROCKETMQ_VERSION}/volumne/logs
VOLUME ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-${ROCKETMQ_VERSION}/volumne/store
VOLUME ${ROCKETMQ_SCENARIO_HOME}/agent
#
# ports
#
EXPOSE 8080
#
# entry point
#
ENTRYPOINT /docker-entrypoint.sh
#
# start namesrv and broker
#
RUN cd ${ROCKETMQ_HOME}/bin && export JAVA_OPT=" -Duser.home=${ROCKETMQ_SCENARIO_HOME}/rocketMQ-${ROCKETMQ_VERSION}/volumne" && nohup sh mqnamesrv & && nohup sh mqbroker -n $NAME_SRV_HOST &
#
# start provider and consumer
#
RUN nohup sh ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-consumer/consumer-startup.sh > ${ROCKETMQ_SCENARIO_HOME}/logs/logs.log & && nohup sh ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-provider/provider-startup.sh > ${ROCKETMQ_SCENARIO_HOME}/logs/logs.log &
#
# tail logs
#
CMD tail -1f ${ROCKETMQ_SCENARIO_HOME}/logs/logs.log
