#!/bin/sh

export AGENT_FILE_PATH=/usr/local/skywalking-light4j-scenario/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    SCENARIO_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar=plugin.light4j.trace_handler_chain=true -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER} -Dskywalking.agent.service_name=light4j-projectA -Xms256m -Xmx256m -XX:PermSize=64M -XX:MaxPermSize=64"
fi

java $SCENARIO_OPTS -jar /usr/local/skywalking-light4j-scenario/projectA.jar