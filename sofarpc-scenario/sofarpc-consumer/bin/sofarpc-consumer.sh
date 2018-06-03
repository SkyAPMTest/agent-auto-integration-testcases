#!/usr/bin/env bash

PRG="$0"
PRGDIR=`dirname "$PRG"`
[ -z "$SOFARPC_CONSUMER_HOME" ] && SOFARPC_CONSUMER_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

AGENT_FILE_PATH=$SOFARPC_CONSUMER_HOME/agent

if [ -f "$AGENT_FILE_PATH/skywalking-agent.jar" ];then
    SOFARPC_CONSUMER_OPTS=" -javaagent:$AGENT_FILE_PATH/skywalking-agent.jar -Dconfig=/usr/local/sofarpc-consumer/agent-config"
fi

_RUNJAVA=${JAVA_HOME}/bin/java
[ -z "$JAVA_HOME" ] && _RUNJAVA=`java`

CLASSPATH="$SOFARPC_CONSUMER_HOME/config:$CLASSPATH"
for i in "$SOFARPC_CONSUMER_HOME"/libs/*.jar
do
    CLASSPATH="$i:$CLASSPATH"
done

JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -Dskywalking.collector.grpc_channel_check_interval=2 -Dskywalking.collector.app_and_service_register_check_interval=2 -Dskywalking.collector.discovery_check_interval=2 -Dskywalking.collector.servers=${COLLECTOR_SERVER} -Dcom.alipay.sofa.rpc.registry.address=zookeeper://${ZK_ADDRESS} -Dskywalking.agent.application_code=sofarpc-consumer -Xms256m -Xmx256m -XX:PermSize=64M -XX:MaxPermSize=64"

echo $JAVA_OPTS
echo $SOFARPC_CONSUMER_OPTS
echo $CLASSPATH

$JAVA_HOME/bin/java $JAVA_OPTS -classpath "$CLASSPATH" $SOFARPC_CONSUMER_OPTS org.apache.skywaking.apm.testcase.sofarpc.ClientApplication
