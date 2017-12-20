#!/bin/sh

echo "replace {MYSQL_INSTANCE_HOST} to $MYSQL_INSTANCE_HOST"
eval sed -i -e 's/\{MYSQL_INSTANCE_HOST\}/$MYSQL_INSTANCE_HOST/' /usr/local/tomcat/webapps/mysql-case/WEB-INF/classes/jdbc.properties

echo "replace {MYSQL_INSTANCE_USERNAME} to $MYSQL_INSTANCE_USERNAME"
eval sed -i -e 's/\{MYSQL_INSTANCE_USERNAME\}/$MYSQL_INSTANCE_USERNAME/' /usr/local/tomcat/webapps/mysql-case/WEB-INF/classes/jdbc.properties

echo "replace {MYSQL_INSTANCE_PASSWORD} to $MYSQL_INSTANCE_PASSWORD"
eval sed -i -e 's/\{MYSQL_INSTANCE_PASSWORD\}/$MYSQL_INSTANCE_PASSWORD/' /usr/local/tomcat/webapps/mysql-case/WEB-INF/classes/jdbc.properties


exec "$@"
