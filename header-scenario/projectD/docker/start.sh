#!/bin/bash

export AGENT_FILE_PATH=/usr/local/header-scenario/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    JAVA_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar "
    JAVA_OPTS="$JAVA_OPTS -Dskywalking.collector.backend_service=${COLLECTOR_SERVER} "
    JAVA_OPTS="$JAVA_OPTS -Dskywalking.agent.service_name=projectD "
    JAVA_OPTS="$JAVA_OPTS -Dskywalking.agent.active_v2_header=true "
    JAVA_OPTS="$JAVA_OPTS -Dskywalking.agent.active_v1_header=true "
fi

java $JAVA_OPTS -jar /usr/local/header-scenario/projectD.jar