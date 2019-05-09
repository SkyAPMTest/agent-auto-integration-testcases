#!/bin/bash
#
#ARG_POSITIONAL_SINGLE([agent_repo], [The agent repository URL to be run])
#ARG_POSITIONAL_SINGLE([agent_repo_branch], [The branch name of agent repository to be run])
#ARG_OPTIONAL_SINGLE([testcase_branch], [], [The branch of the testcase repository])
#ARG_OPTIONAL_REPEATED([scenario], [], [The scenarios to be run])
#ARG_OPTIONAL_SINGLE([issue_no], [], [The relate issue no], [UNKNOWN])
#ARG_OPTIONAL_BOOLEAN([build], [], [Skip build projects.], [on])
#ARG_OPTIONAL_BOOLEAN([report], [], [Skip report the testcase to GitHub], [off])
#ARG_OPTIONAL_BOOLEAN([clone_code], [], [Skip clone the code], [on])
#ARG_OPTIONAL_BOOLEAN([fetch_latest_code], [], [fetch latest code], [on])
#ARG_OPTIONAL_BOOLEAN([skip_single_mode_scenario], [], [Skip build the scenario with single mode], [on])
#ARG_OPTIONAL_SINGLE([collector_image_version],[], [The docker image version of mock collector], "6.0.0-2018")
#ARG_OPTIONAL_SINGLE([parallel_run_size], [], [The size of running testcase at the same time], 1)
#ARG_OPTIONAL_SINGLE([validate_log_url_prefix], [], [The url prefix of validate log url], [http://host:port/jenkins])
#DEFINE_SCRIPT_DIR([AGENT_TEST_HOME])
#ARG_HELP()
#ARGBASH_GO
# [

# declare variables
WORKSPACE_DIR=${AGENT_TEST_HOME}/workspace
SOURCE_CODE_DIR=${WORKSPACE_DIR}/source
AGENT_SOURCE_CODE=${SOURCE_CODE_DIR}/skywalking
VALIDATE_TOOL_SOURCE_CODE=${SOURCE_CODE_DIR}/validate-tool
AGENT_WITH_OPTIONAL_PLUGIN_DIR=${SOURCE_CODE_DIR}/agent-with-optional-plugins
REPORT_HOME=${WORKSPACE_DIR}/report
AGENT_DIR=${SOURCE_CODE_DIR}/agent
TESTCASES_HOME=${AGENT_TEST_HOME}/testcases
VALIDATE_TOOL_REPO=https://github.com/SkywalkingTest/agent-integration-testtool.git
VALIDATE_TOOL_REPO_BRANCH=master
OVERWRITE_README="on"
LOGS_DIR=${WORKSPACE_DIR}/logs
TESTCASE_REPO=`cd ${AGENT_TEST_HOME} && git config --get remote.origin.url`
TESTCASE_REPO_BRANCH=${_arg_testcase_branch}
if [ "${_arg_testcase_branch}" = "" ]; then
   TESTCASE_REPO_BRANCH=`cd ${AGENT_TEST_HOME} && git branch | grep \* | cut -d ' ' -f2`
fi

declare -a SCENARIOS
if [ ${#_arg_scenario[@]} -eq 0 ]; then
    for SCENARIO in `ls $AGENT_TEST_HOME`
    do
        if [ -f "${SCENARIO}/testcase.yml" ]; then
            SCENARIOS+=("${SCENARIO}")
        fi
    done
else
    SCENARIOS=("${_arg_scenario[@]}")
    OVERWRITE_README="off"
fi

echo "[INFO] Running parameteres:"
echo -e "  - Agent repository:\t\t${_arg_agent_repo}"
echo -e "  - Agent repository branch:\t${_arg_agent_repo_branch}"
echo -e "  - Testcase repository:\t${TESTCASE_REPO}"
echo -e "  - Testcase repository branch:\t${TESTCASE_REPO_BRANCH}"
echo -e "  - Issue No:\t\t\t${_arg_issue_no}"
echo -e "  - Build:\t\t\t${_arg_build}"
echo -e "  - Report:\t\t\t${_arg_report}"
echo -e "  - Image version of collector:\t${_arg_collector_image_version}"
echo -e "  - parallel running number:\t${_arg_parallel_run_size}"
echo -e "  - Scenarios:\t\t\t${SCENARIOS[@]}"

# build workspace
if [ "${_arg_clone_code}" = "on" ]; then
    rm -rf ${WORKSPACE_DIR} && mkdir -p ${WORKSPACE_DIR}
fi

rm -rf ${LOGS_DIR} && mkdir -p ${LOGS_DIR}

echo "[INFO] build workspace"
${AGENT_TEST_HOME}/.autotest/build_agent.sh --build ${_arg_build} --fetch_latest_code ${_arg_fetch_latest_code} --clone_code ${_arg_clone_code} ${_arg_agent_repo} ${_arg_agent_repo_branch} ${AGENT_SOURCE_CODE} && ${AGENT_TEST_HOME}/.autotest/build_validate_tool.sh --fetch_latest_code ${_arg_fetch_latest_code} --build ${_arg_build} --clone_code ${_arg_clone_code} ${VALIDATE_TOOL_REPO} ${VALIDATE_TOOL_REPO_BRANCH} ${VALIDATE_TOOL_SOURCE_CODE} && ${AGENT_TEST_HOME}/.autotest/build_report.sh --fetch_latest_code ${_arg_fetch_latest_code} --clone_code ${_arg_clone_code} ${REPORT_HOME}

AGENT_COMMIT_ID=$(cd $AGENT_SOURCE_CODE && git rev-parse HEAD)
TESTCASE_COMMIT_ID=$(cd $AGENT_TEST_HOME && git rev-parse HEAD)

# build testcase
echo "[INFO] build test case projects"
${AGENT_TEST_HOME}/build_testcases.sh --collector_image_version ${_arg_collector_image_version} --skip_single_mode ${_arg_skip_single_mode_scenario} ${AGENT_TEST_HOME} ${SCENARIOS[@]} > ${LOGS_DIR}/testcase-build.log

# run test_case
${AGENT_TEST_HOME}/run.sh -m ${_arg_parallel_run_size} ${TESTCASES_HOME} >/dev/null

# generate report
${AGENT_TEST_HOME}/generate-report.sh --agent_repo ${_arg_agent_repo} --agent_branch ${_arg_agent_repo_branch} --testcase_repo ${TESTCASE_REPO} --testcase_branch ${TESTCASE_REPO_BRANCH} --agent_commitid ${AGENT_COMMIT_ID} --testcase_commitid ${TESTCASE_COMMIT_ID} --overwrite_readme ${OVERWRITE_README} --upload_report ${_arg_report} --issue_no ${_arg_issue_no} --validate_log_url_prefix ${_arg_validate_log_url_prefix} ${TESTCASES_HOME} ${REPORT_HOME} > ${LOGS_DIR}/test_report.log

# ]
