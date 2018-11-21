#!/bin/bash

JAVA_OPTS=" -javaagent:/usr/local/header-scenario/apache-skywalking-apm-incubating/agent/skywalking-agent.jar "
JAVA_OPTS="$JAVA_OPTS -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dskywalking.collector.discovery_check_interval=2 -Dskywalking.collector.servers=${COLLECTOR_SERVER}"
JAVA_OPTS="$JAVA_OPTS -Dskywalking.agent.application_code=projectC"

java $JAVA_OPTS -jar /usr/local/header-scenario/projectC.jar
