#!/bin/bash
echo "stopping grails..."

kill -9 `cat grails_pid.txt`
rm grails_pid.txt

echo "grails stopped..."