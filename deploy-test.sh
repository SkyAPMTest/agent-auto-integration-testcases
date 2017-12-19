#!/bin/bash

Usage(){
	echo "Usage: agent-test.sh [OPTIONS]"
	echo "Options:"
	echo -e "    -p | --project projectName  \t\t Only test the case under the project. "
}

PRG="$0"
PRGDIR=`dirname "$PRG"`
[ -z "$AGENT_TEST_HOME" ] && AGENT_TEST_HOME=`cd "$PRGDIR" >/dev/null; pwd`

REPORT_DATA=true
TEST_PROJECT_NAME=""

#
# Parse the input parameters
#
while [[ $# -gt 0 ]]; do
	case "$1" in
		-p | --project )
			TEST_PROJECT_NAME=$2
			shift 2
			;;
		* )
			shift
			break
	esac
done

${AGENT_TEST_HOME}/.autotest/autotest-deploy.sh --project "${TEST_PROJECT_NAME}"

${AGENT_TEST_HOME}/.autotest/agent-test.sh