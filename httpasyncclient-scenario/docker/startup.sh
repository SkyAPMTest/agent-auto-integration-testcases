#!/bin/sh

export AGENT_FILE_PATH=/usr/local/httpasyncclient-scenario/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    HTTPASYNCCLIENT_SCENARIO_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.servers=${COLLECTOR_SERVER} -Dskywalking.agent.application_code=httpasyncclient-scenario -Xms256m -Xmx256m -XX:PermSize=64M -XX:MaxPermSize=64"
fi

java $HTTPASYNCCLIENT_SCENARIO_OPTS -Dmysql.host=${MYSQL_HOST} -Dmysql.username=${MYSQL_USERNAME} -Dmysql.password=${MYSQL_PASSWORD} -jar /usr/local/skywalking-httpasyncclient-scenario/httpasyncclient-scenario.jar
