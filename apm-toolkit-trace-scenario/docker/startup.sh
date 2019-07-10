#!/bin/sh

export AGENT_FILE_PATH=/usr/local/skywalking-apm-toolkit-trace-scenario/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    TOOLKIT_CASE_OPTS="$CATALINA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dskywalking.collector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER} -Dskywalking.agent.service_name=apm-toolkit-trace-scenario"
fi

java ${TOOLKIT_CASE_OPTS} -jar /usr/local/skywalking-apm-toolkit-trace-scenario/apm-toolkit-trace-scenario.jar
