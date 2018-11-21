#!/bin/sh

export AGENT_FILE_PATH=/usr/local/skywalking-mongodb-scenario/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    MONGODB_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER} -Dskywalking.agent.service_name=mongodb-scenario -Xms256m -Xmx256m -XX:PermSize=64M -XX:MaxPermSize=64"
fi

java $MONGODB_OPTS -Dmongodb.host=${MONGO_HOST} -jar /usr/local/skywalking-mongodb-scenario/mongodb-scenario.jar
