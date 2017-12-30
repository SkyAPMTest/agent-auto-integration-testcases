#!/bin/sh

export AGENT_FILE_PATH=/usr/local/skywalking-jettyserver-scenario/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    OKHTTP_SCENARIO_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.servers=${COLLECTOR_SERVER} -Dskywalking.agent.application_code=jettyserver-scenario "
fi

java $OKHTTP_SCENARIO_OPTS -jar /usr/local/skywalking-jettyserver-scenario/jettyserver-scenario.jar
