#!/bin/sh

echo "replace {ZK_ADDRESS} to $ZK_ADDRESS"
eval sed -i -e 's/\{ZK_ADDRESS\}/$ZK_ADDRESS/' /usr/local/tomcat/webapps/dubbox-case/WEB-INF/classes/dubbo-consumer.xml

cp /usr/local/tomcat/agent-config/sky-walking.config /usr/local/tomcat/agent/config/agent.config

exec "$@"
