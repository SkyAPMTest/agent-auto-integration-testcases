#!/bin/sh

export AGENT_FILE_PATH=/usr/local/skywalking-spring-with-qualified-op-name-scenario/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    SCENARIO_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER} -Dskywalking.agent.service_name=spring-with-qualified-op-name-scenario -Dskywalking.plugin.springmvc.use_qualified_name_as_endpoint_name=true -Xms256m -Xmx256m -XX:PermSize=64M -XX:MaxPermSize=64"
fi

java $SCENARIO_OPTS -jar /usr/local/skywalking-spring-with-qualified-op-name-scenario/spring-with-qualified-op-name-scenario.jar
