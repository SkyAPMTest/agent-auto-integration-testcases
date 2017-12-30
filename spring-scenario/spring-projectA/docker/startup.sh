#!/bin/sh

export AGENT_FILE_PATH=/usr/local/spring-scenario/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    RESTTEMPLATE_CASE_OPTS="$CATALINA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dskywalking.collector.discovery_check_interval=2 -Dskywalking.collector.servers=${COLLECTOR_SERVER} -Dskywalking.agent.application_code=resttemplate-scenario"
fi

java ${RESTTEMPLATE_CASE_OPTS} -DprojectB.url="http://${PROJECTB_URL}/restapi-case" -DprojectC.url="http://${PROJECTC_URL}/spring-projectc" -jar /usr/local/spring-scenario/spring-projectA.jar
