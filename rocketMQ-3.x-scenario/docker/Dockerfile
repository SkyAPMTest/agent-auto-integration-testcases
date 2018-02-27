FROM openjdk:8-jdk

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    ROCKETMQ_VERSION=3.2.6 \
    ROCKETMQ_SCENARIO_HOME=/usr/local/rocketMQ-scenario \
    ROCKETMQ_NAMESRV=localhost:9876 \
    ROCKETMQ_HOME=/usr/local/rocketMQ-scenario/alibaba-rocketmq


WORKDIR  ${ROCKETMQ_SCENARIO_HOME}

RUN mkdir -p \
		${ROCKETMQ_SCENARIO_HOME}/rocketMQ-${ROCKETMQ_VERSION}/volume/logs \
	    ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-${ROCKETMQ_VERSION}/volume/store \
	    ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-scenario \
	    ${ROCKETMQ_SCENARIO_HOME}/logs \
	    ${ROCKETMQ_SCENARIO_HOME}/agent

RUN wget https://github.com/alibaba/rocketmq/releases/download/v${ROCKETMQ_VERSION}/alibaba-rocketmq-${ROCKETMQ_VERSION}.tar.gz \
          && tar zxvf alibaba-rocketmq-${ROCKETMQ_VERSION}.tar.gz -C ${ROCKETMQ_SCENARIO_HOME} \
          && rm alibaba-rocketmq-${ROCKETMQ_VERSION}.tar.gz

COPY runbroker.sh ${ROCKETMQ_SCENARIO_HOME}/alibaba-rocketmq/bin/
COPY runserver.sh ${ROCKETMQ_SCENARIO_HOME}/alibaba-rocketmq/bin/
#
#
#
COPY rocketMQ-scenario.jar ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-scenario
#
# Volumes
#
VOLUME /usr/local/rocketMQ-scenario/alibaba-rocketmq/volume/logs
VOLUME /usr/local/rocketMQ-scenario/alibaba-rocketmq/volume/store
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