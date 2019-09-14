#!/bin/bash

echo "starting grails..."

. $HOME/.sdkman/bin/sdkman-init.sh

sdk use grails 1.3.7
sdk use java 6u65

export GRAILS_OPTS="-Xdebug -Xmx12G -Xmx12G -XX:MaxPermSize=12G"

nohup grails run-app >/dev/null 2>&1 &

sleep 30s

echo "grails started..."