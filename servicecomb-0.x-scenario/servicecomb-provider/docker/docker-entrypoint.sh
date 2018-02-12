#!/usr/bin/env bash

echo "replace {SERVICE_CENTER_HOST} to $SERVICE_CENTER_HOST"
eval sed -i -e 's/\{SERVICE_CENTER_HOST\}/$SERVICE_CENTER_HOST/' /usr/local/servicecomb-provider/config/microservice.yaml

echo "replace {PROVIDER_REST_BIND_HOST} to $PROVIDER_REST_BIND_HOST"
eval sed -i -e 's/\{PROVIDER_REST_BIND_HOST\}/$PROVIDER_REST_BIND_HOST/' /usr/local/servicecomb-provider/config/microservice.yaml

exec "$@"
