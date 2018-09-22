#!/usr/bin/env bash

PRG="$0"
PRGDIR=`dirname "$PRG"`
[ -z "$MONTAN_PROVIDER_HOME" ] && MONTAN_PROVIDER_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

AGENT_FILE_PATH=$MONTAN_PROVIDER_HOME/agent

if [ -f "$AGENT_FILE_PATH/skywalking-agent.jar" ];then
    MOTAN_PROVIDER_OPTS=" -javaagent:$AGENT_FILE_PATH/skywalking-agent.jar -Dconfig=/usr/local/dubbox-provider/agent-config"
fi

_RUNJAVA=${JAVA_HOME}/bin/java
[ -z "$JAVA_HOME" ] && _RUNJAVA=`java`

CLASSPATH="$MONTAN_PROVIDER_HOME/config:$CLASSPATH"
for i in "$MONTAN_PROVIDER_HOME"/libs/*.jar
do
    CLASSPATH="$i:$CLASSPATH"
done

JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dskywalking.collector.discovery_check_interval=2 -Dskywalking.collector.servers=${COLLECTOR_SERVER} -Dskywalking.agent.application_code=motan-provider -Xms256m -Xmx256m -XX:PermSize=64M -XX:MaxPermSize=64"

$JAVA_HOME/bin/java $JAVA_OPTS -classpath "$CLASSPATH" $MOTAN_PROVIDER_OPTS test.apache.skywalking.apm.testcase.motan.provider.Application
