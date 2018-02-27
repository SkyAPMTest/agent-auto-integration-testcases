#!/usr/bin/env bash

echo "begin to start nameservice"
(cd ${ROCKETMQ_HOME}/bin && export JAVA_OPT=" -Duser.home=${ROCKETMQ_SCENARIO_HOME}/rocketMQ-${ROCKETMQ_VERSION}/volume" && nohup sh mqnamesrv &) > /dev/null
sleep 5
echo "begin to start broker"
(cd ${ROCKETMQ_HOME}/bin && export JAVA_OPT=" -Duser.home=${ROCKETMQ_SCENARIO_HOME}/rocketMQ-${ROCKETMQ_VERSION}/volume" && nohup sh mqbroker -n $ROCKETMQ_NAMESRV autoCreateTopicEnable=true &) > /dev/nul
echo "begin to start provider"

export AGENT_FILE_PATH=${ROCKETMQ_SCENARIO_HOME}/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    ROCKETMQ_PROVIDER_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.servers=${COLLECTOR_SERVER} -Dskywalking.agent.application_code=rocketMQ-scenario "
fi

java $ROCKETMQ_PROVIDER_OPTS -DrocketMQ.nameSrv=${ROCKETMQ_NAMESRV} -Dcom.rocketmq.sendMessageWithVIPChannel=false -jar ${ROCKETMQ_SCENARIO_HOME}/rocketMQ-scenario/rocketMQ-scenario.jar
