#!/bin/sh

export AGENT_FILE_PATH=/usr/local/skywalking-lettuce-scenario/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    LETTUCE_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER} -Dskywalking.agent.service_name=lettuce-scenario -Dskywalking.plugin.lettuce.trace_dsl=true -Xms256m -Xmx256m -XX:PermSize=64M -XX:MaxPermSize=64"
fi

java $LETTUCE_OPTS -Dlettuce.host=${LETTUCE_HOST} -jar /usr/local/skywalking-lettuce-scenario/lettuce-scenario.jar
