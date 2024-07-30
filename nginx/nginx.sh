#!/bin/sh

# Fail on error and undefined vars
set -eu

NGINX_HTML="/usr/share/nginx/html"
EVOTEMPUS_HTML="${NGINX_HTML}/evotempus"

generate_nginx_gateway_conf() {
  TEMPLATE=/nginx-gateway.conf.template

  # shellcheck disable=SC2016
  envsubst '
    $EVOTEMPUS_HTML
    ' < ${TEMPLATE} > /etc/nginx/conf.d/nginx.conf
}

echo Generating gateway NGINX configuration
generate_nginx_gateway_conf

# shellcheck disable=SC2181
if [ $? = 0 ]; then
  echo Starting NGINX...
  nginx -g 'daemon off;'
else
  echo Failed to configure correctly...
  exit 1
fi
