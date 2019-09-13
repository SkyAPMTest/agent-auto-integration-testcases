#!/bin/sh

export AGENT_FILE_PATH=/usr/local/skywalking-cassandra-scenario/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    CASSANDRA_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER} -Dskywalking.agent.service_name=cassandra-scenario -Xms256m -Xmx256m -XX:PermSize=64M -XX:MaxPermSize=64"
fi

java $CASSANDRA_OPTS -Dcassandra.host=${CASSANDRA_HOST} -Dcassandra.port=${CASSANDRA_PORT} -jar /usr/local/skywalking-cassandra-scenario/cassandra-scenario.jar
