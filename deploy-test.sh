#!/bin/bash

Usage(){
	echo "Usage: agent-test.sh [OPTIONS]"
	echo "Options:"
	echo -e "    -p | --project projectName  \t\t Only test the case under the project. "
	echo -e "    -civ | --collector-image-version version \t\t Set mock collector image. "
	echo -e "    -b | --branch branch \t\t The branch name of Skywalking. "
	echo -e "    -r | --repo repo \t\t The repo of Skywalking. "
	echo -e "	 -tcb | --testcase-branch branchName "
	echo -e "    		--reportFileMode CONVERAGE | NONE"
	echo -e "	 --parallel_running_case_number number \t\t The parallel running case number."
  echo -e "  -i | --issueNo issueNo"
}

PRG="$0"
PRGDIR=`dirname "$PRG"`
[ -z "$AGENT_TEST_HOME" ] && AGENT_TEST_HOME=`cd "$PRGDIR" >/dev/null; pwd`

#
# the define env variables
#
TEST_PROJECT_NAME=""
MOCK_COLLECTOR_IMAGE_VERSION="3.2.6-2017"
AGENT_BRANCH_NAME="master"
AGENT_GIT_URL="https://github.com/apache/incubator-skywalking.git"
TESTCASE_BRANCH="master"
TEST_TOOLS_BRANCH="master"
REPORT_FILE_MODE="NONE"
PARALLEL_RUNNING_CASE_NUMBER=8
ISSUE_NO="UNKNOWN"
VALIDATE_LOG_URL_PREFIX="UNKNOWN"
#
# Parse the input parameters
#
while [[ $# -gt 0 ]]; do
	case "$1" in
		-p | --project )
			TEST_PROJECT_NAME=$2
			shift 2
			;;
		-r | --repo )
			AGENT_GIT_URL=$2
			shift 2
			;;
		-civ | --collector-image-version )
			MOCK_COLLECTOR_IMAGE_VERSION=$2
			shift 2
			;;
		-b | --branch )
			AGENT_BRANCH_NAME=$2
			shift 2
			;;
		-tcb | --testcase-branch )
			TESTCASE_BRANCH=$2
			shift 2
			;;
		--reportFileMode )
			REPORT_FILE_MODE=$2
			shift 2
			;;	
		--parallel_running_case_number )
			PARALLEL_RUNNING_CASE_NUMBER=$2
			shift 2
			;;
		-i | --issueNo )
		    ISSUE_NO=$2
		    shift 2
		    ;;
		--validateLogURL )
		    VALIDATE_LOG_URL_PREFIX=$2
		    shift 2
		    ;;
    --test-tools-branch )
        TEST_TOOLS_BRANCH=$2
        shift 2
        ;;
		* )
		    shift
		    break
	esac
done

if [ "${TEST_PROJECT_NAME}" != "" ]; then
    ${AGENT_TEST_HOME}/.autotest/autotest-deploy.sh --project "${TEST_PROJECT_NAME}" --collector-image-version "$MOCK_COLLECTOR_IMAGE_VERSION"
else
   ${AGENT_TEST_HOME}/.autotest/autotest-deploy.sh --collector-image-version "$MOCK_COLLECTOR_IMAGE_VERSION" 
fi

${AGENT_TEST_HOME}/.autotest/agent-test.sh --max-running-size ${PARALLEL_RUNNING_CASE_NUMBER} --branch "$AGENT_BRANCH_NAME" -r "$AGENT_GIT_URL" --reportFileMode ${REPORT_FILE_MODE} --testcase-branch "$TESTCASE_BRANCH" --issueNo ${ISSUE_NO} --validateLogURL $VALIDATE_LOG_URL_PREFIX --test-tools-branch $TEST_TOOLS_BRANCH

