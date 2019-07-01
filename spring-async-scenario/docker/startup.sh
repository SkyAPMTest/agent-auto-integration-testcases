#!/bin/sh

export AGENT_FILE_PATH=/usr/local/skywalking-spring-async-scenario/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    SPRING_ASYNC_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER} -Dskywalking.agent.service_name=spring-async-scenario -Dskywalking.plugin.spring.async.trace_dsl=true -Xms256m -Xmx256m -XX:PermSize=64M -XX:MaxPermSize=64"
fi

java $SPRING_ASYNC_OPTS -Dspring-async.host=${SPRING_ASYNC_HOST} -jar /usr/local/skywalking-spring-async-scenario/spring-async-scenario.jar
