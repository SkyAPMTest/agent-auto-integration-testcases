#!/bin/sh

echo "replace {ZK_ADDRESS} to $ZK_ADDRESS"
eval sed -i -e 's/\{ZK_ADDRESS\}/$ZK_ADDRESS/' /usr/local/tomcat/webapps/motan-consumer/WEB-INF/classes/motan_client.xml

exec "$@"
