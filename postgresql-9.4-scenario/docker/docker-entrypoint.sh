#!/bin/sh

echo "replace {POSTGRES_INSTANCE_HOST} to $POSTGRES_INSTANCE_HOST"
eval sed -i -e 's/\{POSTGRES_INSTANCE_HOST\}/$POSTGRES_INSTANCE_HOST/' /usr/local/tomcat/webapps/postgresql-scenario/WEB-INF/classes/jdbc.properties

echo "replace {POSTGRES_INSTANCE_USERNAME} to $POSTGRES_INSTANCE_USERNAME"
eval sed -i -e 's/\{POSTGRES_INSTANCE_USERNAME\}/$POSTGRES_INSTANCE_USERNAME/' /usr/local/tomcat/webapps/postgresql-scenario/WEB-INF/classes/jdbc.properties

echo "replace {POSTGRES_INSTANCE_PASSWORD} to $POSTGRES_INSTANCE_PASSWORD"
eval sed -i -e 's/\{POSTGRES_INSTANCE_PASSWORD\}/$POSTGRES_INSTANCE_PASSWORD/' /usr/local/tomcat/webapps/postgresql-scenario/WEB-INF/classes/jdbc.properties


exec "$@"
