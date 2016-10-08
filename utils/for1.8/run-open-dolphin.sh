#!/bin/sh
# run OpenDolphin Server and Client
#
# usage:
#  ./run-open-dolphin.sh command to launch client

JBOSS_HOST="127.0.0.1"
JMX_CONSOLE="http://${JBOSS_HOST}:8080/jmx-console/"
wait_open_dolphin_server_started () {
    while ! curl -s "$JMX_CONSOLE" | grep -q OpenDolphinServer; do
        sleep 1
    done
}

cd "$(dirname "$0")"/..

(
    wait_open_dolphin_server_started
    "$@"
) &

./jboss-4.2.2.GA/bin/run.sh -b "$JBOSS_HOST"
