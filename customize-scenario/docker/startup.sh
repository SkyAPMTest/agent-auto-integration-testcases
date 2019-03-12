#!/bin/sh

export AGENT_FILE_PATH=/usr/local/skywalking-customize-scenario/agent
export CUSTOMIZE_ENHANCE_FILE_PATH=/usr/local/skywalking-customize-scenario/customize_enhance.xml
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    CUSTOMIZE_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.plugin.customize.enhance_file=${CUSTOMIZE_ENHANCE_FILE_PATH} -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER} -Dskywalking.agent.service_name=customize-scenario -Dskywalking.plugin.customize.trace_dsl=true -Xms256m -Xmx256m -XX:PermSize=64M -XX:MaxPermSize=64"
fi

java $CUSTOMIZE_OPTS -jar /usr/local/skywalking-customize-scenario/customize-scenario.jar
