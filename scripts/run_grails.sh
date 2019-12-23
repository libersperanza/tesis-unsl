#!/bin/bash

echo "starting grails..."

. $HOME/.sdkman/bin/sdkman-init.sh

sdk use grails 1.3.7
#sdk use java 6u65

export GRAILS_OPTS="-Xdebug -Xmx12G -Xmx12G -XX:MaxPermSize=12G"

nohup grails -Dserver.port=8090 run-app >/dev/null 2>&1 &
echo $! > grails_pid.txt

sleep 30s

echo "grails started..."