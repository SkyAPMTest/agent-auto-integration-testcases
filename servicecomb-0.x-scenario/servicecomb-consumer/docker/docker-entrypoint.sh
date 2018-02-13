#!/bin/sh

echo "replace {SERVICE_CENTER_HOST} to $SERVICE_CENTER_HOST"
eval sed -i -e 's/\{SERVICE_CENTER_HOST\}/$SERVICE_CENTER_HOST/' /usr/local/servicecomb-consumer/config/microservice.yaml

echo "replace {CONSUMER_REST_BIND_HOST} to $CONSUMER_REST_BIND_HOST"
eval sed -i -e 's/\{CONSUMER_REST_BIND_HOST\}/$CONSUMER_REST_BIND_HOST/' /usr/local/servicecomb-consumer/config/microservice.yaml


exec "$@"
