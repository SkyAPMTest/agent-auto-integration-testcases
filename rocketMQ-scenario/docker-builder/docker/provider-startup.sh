#!/bin/sh

export AGENT_FILE_PATH=${ROCKETMQ_SCENARIO_HOME}/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    ROCKETMQ_PROVIDER_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.servers=${COLLECTOR_SERVER} -Dskywalking.agent.application_code=rocketMQ-provider "
fi

java $ROCKETMQ_PROVIDER_OPTS -DrocketMQ.nameSrv=${ROCKETMQ_NAMESRV} -jar ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-provider/rocketMQ-provider.jar
