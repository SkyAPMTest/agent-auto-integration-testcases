#!/bin/bash

KAFKA_SCENARIO_HOME=/usr/local/kafka-scenario
KAFKA_HOME=${KAFKA_SCENARIO_HOME}/kafka_${SCALA_VERSION}-${KAFKA_VERSION}

#start kafka
echo "start kafka zk server"
(cd ${KAFKA_HOME} && nohup sh bin/zookeeper-server-start.sh config/zookeeper.properties &) > /dev/null
echo "start kafka server"
(cd ${KAFKA_HOME} && nohup sh bin/kafka-server-start.sh config/server.properties &) > /dev/null

export AGENT_FILE_PATH=${KAFKA_SCENARIO_HOME}/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    KAFKA_SCENARIO_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER} -Dskywalking.agent.service_name=kafka-scenario "
fi

java ${KAFKA_SCENARIO_OPTS} -Dbootstrap.servers=${KAFKA_BOOTSTRAP_SERVERS} -jar ${KAFKA_SCENARIO_HOME}/kafka-scenario.jar
