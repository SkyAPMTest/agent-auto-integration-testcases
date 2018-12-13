#!/bin/sh

export AGENT_FILE_PATH=/usr/local/webflux-scenario/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    SCENARIO_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER} -Dskywalking.agent.service_name=webflux-projectA"
fi

java $SCENARIO_OPTS -DprojectB.host=${PROJECTB_ADDRESS} -jar /usr/local/webflux-scenario/projectA.jar
