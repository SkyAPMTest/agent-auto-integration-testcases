#!/bin/bash

Usage(){
	echo "Usage: agent-test.sh [OPTIONS]"
	echo "Options:"
	echo -e "    -p | --project projectName  \t\t Only test the case under the project. "
	echo -e "    -civ | --collector-image-version version \t\t Set mock collector image"
}

PRG="$0"
PRGDIR=`dirname "$PRG"`
[ -z "$AGENT_TEST_HOME" ] && AGENT_TEST_HOME=`cd "$PRGDIR" >/dev/null; pwd`

#
# the define env variables
#
TEST_PROJECT_NAME=""
MOCK_COLLECTOR_IMAGE_VERSION="3.2.6-2017"
#
# Parse the input parameters
#
while [[ $# -gt 0 ]]; do
	case "$1" in
		-p | --project )
			TEST_PROJECT_NAME=$2
			shift 2
			;;
		-civ | --collector-image-version )
			MOCK_COLLECTOR_IMAGE_VERSION=$2
			shift 2
			;;
		* )
			shift
			break
	esac
done

${AGENT_TEST_HOME}/.autotest/autotest-deploy.sh --project "${TEST_PROJECT_NAME}" --collector-image-version "$MOCK_COLLECTOR_IMAGE_VERSION"

${AGENT_TEST_HOME}/.autotest/agent-test.sh -p --skipReport