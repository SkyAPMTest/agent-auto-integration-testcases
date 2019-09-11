#!/bin/sh

export AGENT_FILE_PATH=/usr/local/gateway-scenario/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    rm -rf ${AGENT_FILE_PATH}/plugins/apm-spring-annotation-plugin-*.jar
    SCENARIO_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER} -Dskywalking.agent.service_name=gateway-projectB"
fi

java $SCENARIO_OPTS -jar /usr/local/gateway-scenario/projectB.jar
