#!/bin/bash

PULSAR_SCENARIO_HOME=/usr/local/pulsar-scenario
PULSAR_HOME=${PULSAR_SCENARIO_HOME}/apache-pulsar-${PULSAR_VERSION}

#start pulsar standalone
echo "start pulsar standalone"
(cd ${PULSAR_HOME} && bin/pulsar-daemon start standalone) > /dev/null

export AGENT_FILE_PATH=${PULSAR_SCENARIO_HOME}/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    PULSAR_SCENARIO_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER} -Dskywalking.agent.service_name=pulsar-scenario "
fi

java ${PULSAR_SCENARIO_OPTS} -Dservice.url=${PULSAR_SERVICE_URL} -jar ${PULSAR_SCENARIO_HOME}/pulsar-scenario.jar
