#!/bin/sh

export AGENT_FILE_PATH=/usr/local/resteasy-server2/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    SCENARIO_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER} -Dskywalking.agent.service_name=resteasy-server2"
fi

RESTEASY_PORT=${RESTEASY_PORT} java $SCENARIO_OPTS -jar /usr/local/resteasy-server2/resteasy-server2.jar