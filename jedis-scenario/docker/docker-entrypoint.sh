#!/bin/sh

echo "replace {REDIS_HOST} to $REDIS_HOST"
eval sed -i -e 's/\{REDIS_HOST\}/$REDIS_HOST/' /usr/local/tomcat/webapps/jedis-standalone-case/WEB-INF/classes/jedis.properties

echo "replace {REDIS_PORT} to $REDIS_PORT"
eval sed -i -e 's/\{REDIS_PORT\}/$REDIS_PORT/' /usr/local/tomcat/webapps/jedis-standalone-case/WEB-INF/classes/jedis.properties

exec "$@"
