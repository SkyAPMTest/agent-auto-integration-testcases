#!/usr/bin/env bash

export AGENT_FILE_PATH=${JDK_CROSS_THREAD_SCENARIO_HOME}/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    JDK_CROSS_THREAD_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.servers=${COLLECTOR_SERVER} -Dskywalking.agent.application_code=jdk-cross-thread-scenario "
fi

java $JDK_CROSS_THREAD_OPTS -jar ${JDK_CROSS_THREAD_SCENARIO_HOME}/jdk-cross-thread-scenario.jar
