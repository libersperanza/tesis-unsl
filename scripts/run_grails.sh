#!/bin/bash

. /Users/lsperanza/.sdkman/bin/sdkman-init.sh

sdk use grails 1.3.7
sdk use java 6u65

export GRAILS_OPTS="-Xdebug -Xmx8G -Xmx8G -XX:MaxPermSize=8G"

nohup grails run-app >/dev/null 2>&1 &
