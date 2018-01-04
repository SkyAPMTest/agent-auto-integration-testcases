#!/bin/sh

export AGENT_FILE_PATH=/usr/local/spring-restapi-scenario/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    RESTTEMPLATE_CASE_OPTS="$CATALINA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dskywalking.collector.discovery_check_interval=2 -Dskywalking.collector.servers=${COLLECTOR_SERVER} -Dskywalking.agent.application_code=restapi-scenario -Xms256m -Xmx256m -XX:PermSize=64M -XX:MaxPermSize=64"
fi

java ${RESTTEMPLATE_CASE_OPTS} -jar /usr/local/spring-restapi-scenario/spring-projectB.jar
