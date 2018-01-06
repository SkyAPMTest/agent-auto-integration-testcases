FROM openjdk:8-jdk

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    ROCKETMQ_VERSION=4.2.0 \
    ROCKETMQ_SCENARIO_HOME=/usr/local/rocketMQ-scenario \
    ROCKETMQ_NAMESRV=localhost:9876 \
    ROCKETMQ_HOME=/usr/local/rocketMQ-scenario/rocketMQ-4.2.0


WORKDIR  ${ROCKETMQ_SCENARIO_HOME}

RUN mkdir -p \
		${ROCKETMQ_SCENARIO_HOME}/rocketMQ-${ROCKETMQ_VERSION}/volume/logs \
	    ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-${ROCKETMQ_VERSION}/volume/store \
	    ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-scenario \
	    ${ROCKETMQ_SCENARIO_HOME}/logs \
	    ${ROCKETMQ_SCENARIO_HOME}/agent

RUN curl http://mirror.bit.edu.cn/apache/rocketmq/${ROCKETMQ_VERSION}/rocketmq-all-${ROCKETMQ_VERSION}-bin-release.zip -o rocketMQ.zip \
          && unzip rocketMQ.zip -d ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-${ROCKETMQ_VERSION} \
          && rm rocketMQ.zip

COPY runbroker.sh ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-${ROCKETMQ_VERSION}/bin/
COPY runserver.sh ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-${ROCKETMQ_VERSION}/bin/
#
#
#
COPY rocketMQ-scenario.jar ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-scenario
#
# Volumes
#
VOLUME /usr/local/rocketMQ-scenario/rocketMQ-4.2.0/volume/logs
VOLUME /usr/local/rocketMQ-scenario/rocketMQ-4.2.0/volume/store
VOLUME /usr/local/rocketMQ-scenario/agent
#
# ports
#
EXPOSE 8080
#
# entry point
#
#ENTRYPOINT /docker-entrypoint.sh
#
#
#
COPY startup.sh ${ROCKETMQ_SCENARIO_HOME}
#
#
#
CMD sh ${ROCKETMQ_SCENARIO_HOME}/startup.sh