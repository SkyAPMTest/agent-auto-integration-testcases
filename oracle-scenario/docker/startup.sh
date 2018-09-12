#!/bin/sh

export AGENT_FILE_PATH=/usr/local/oracle-scenario/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    ORACLE_SCENARIO_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER} -Dskywalking.agent.application_code=oracle-scenario -Xms256m -Xmx256m -XX:PermSize=64M -XX:MaxPermSize=64"
fi

java $ORACLE_SCENARIO_OPTS -Doracle.address=${ORACLE_ADDRESS} -jar /usr/local/skywalking-oracle-scenario/oracle-scenario.jar
