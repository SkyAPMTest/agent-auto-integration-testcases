#!/bin/bash

ACTIVEMQ_SCENARIO_HOME=/usr/local/activemq-scenario
ACTIVEMQ_HOME=${ACTIVEMQ_SCENARIO_HOME}/apache-activemq-${ACTIVEMQ_VERSION}

#start activemq
echo "start activemq server"
(cd ${ACTIVEMQ_HOME} && nohup sh bin/activemq start &) > /dev/null

export AGENT_FILE_PATH=${ACTIVEMQ_SCENARIO_HOME}/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    ACTIVEMQ_SCENARIO_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER} -Dskywalking.agent.application_code=activemq-scenario "
fi

java ${ACTIVEMQ_SCENARIO_OPTS} -jar ${ACTIVEMQ_SCENARIO_HOME}/activemq-scenario.jar
