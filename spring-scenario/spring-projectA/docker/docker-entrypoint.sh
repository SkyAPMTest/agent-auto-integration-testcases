#!/bin/sh


echo "replace {PROJECTB_URL} to $PROJECTB_URL"
eval sed -i -e 's/\{PROJECTB_URL\}/$PROJECTB_URL/' /usr/local/tomcat/webapps/resttemplate-case/WEB-INF/classes/spring-config.properties

echo "replace {PROJECTC_URL} to $PROJECTC_URL"
eval sed -i -e 's/\{PROJECTC_URL\}/$PROJECTC_URL/' /usr/local/tomcat/webapps/resttemplate-case/WEB-INF/classes/spring-config.properties

echo "replace {PROJECTD_URL} to $PROJECTD_URL"
eval sed -i -e 's/\{PROJECTD_URL\}/$PROJECTD_URL/' /usr/local/tomcat/webapps/resttemplate-case/WEB-INF/classes/spring-config.properties

exec "$@"
