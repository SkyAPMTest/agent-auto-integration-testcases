#!/usr/bin/env bash

PRG="$0"
PRGDIR=`dirname "$PRG"`
[ -z "$SERVICECOMB_CONSUMER_HOME" ] && SERVICECOMB_CONSUMER_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

AGENT_FILE_PATH=$SERVICECOMB_CONSUMER_HOME/agent

if [ -f "$AGENT_FILE_PATH/skywalking-agent.jar" ];then
    SERVICECOMB_CONSUMER_OPTS=" -javaagent:$AGENT_FILE_PATH/skywalking-agent.jar -Dconfig=/usr/local/servicecomb-consumer/agent-config"
fi

_RUNJAVA=${JAVA_HOME}/bin/java
[ -z "$JAVA_HOME" ] && _RUNJAVA=`java`

CLASSPATH="$SERVICECOMB_CONSUMER_HOME/config:$CLASSPATH"
for i in "$SERVICECOMB_CONSUMER_HOME"/libs/*.jar
do
    CLASSPATH="$i:$CLASSPATH"
done

JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dcollector.discovery_check_interval=2 -Dskywalking.collector.backend_service=${COLLECTOR_SERVER} -Dskywalking.agent.service_name=servicecomb-consumer -Xms256m -Xmx256m -XX:PermSize=64M -XX:MaxPermSize=64"

$JAVA_HOME/bin/java -Dvertx.disableDnsResolver=true $JAVA_OPTS $SERVICECOMB_CONSUMER_OPTS  -classpath "$CLASSPATH" test.apache.skywalking.apm.testcase.servicecomb.consumer.CodeFirstConsumerMain
