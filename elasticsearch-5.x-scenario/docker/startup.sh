#!/bin/sh

export AGENT_FILE_PATH=/usr/local/skywalking-elasticsearch-scenario/agent
if [ -f "${AGENT_FILE_PATH}/skywalking-agent.jar" ]; then
    ELASTICSEARCH_OPTS=" -javaagent:${AGENT_FILE_PATH}/skywalking-agent.jar -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.servers=${COLLECTOR_SERVER} -Dskywalking.agent.application_code=elasticsearch-scenario -Dskywalking.plugin.elasticsearch.trace_dsl=true -Xms256m -Xmx256m -XX:PermSize=64M -XX:MaxPermSize=64"
fi

java $ELASTICSEARCH_OPTS -Delasticsearch.host=${ELASTICSEARCH_HOST} -jar /usr/local/skywalking-elasticsearch-scenario/elasticsearch-scenario.jar
