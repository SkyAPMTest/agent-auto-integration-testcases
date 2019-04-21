#!/bin/bash
#ARG_POSITIONAL_SINGLE([testcase_home], [The directory of testcase])
#ARG_POSITIONAL_SINGLE([target_dir], [The target director that the test report put on])
#ARG_OPTIONAL_SINGLE([agent_repo],[], [The agent repository URL], [https://github.com/apache/incubator-skywalking.git])
#ARG_OPTIONAL_SINGLE([agent_branch], [], [The branch name of agent repository], [master])
#ARG_OPTIONAL_SINGLE([testcase_repo], [], [The test case repository URL], [https://github.com/SkyAPMTest/agent-auto-integration-testcases.git])
#ARG_OPTIONAL_SINGLE([testcase_branch], [], [The branch name of test case repository], [master])
#ARG_OPTIONAL_SINGLE([testcase_commitid], [] , [The commit id of testcase repository], [])
#ARG_OPTIONAL_SINGLE([agent_commitid], [], [The commit id of agent repository], [])
#ARG_OPTIONAL_SINGLE([overwrite_readme], [], [Overwrite the README file], [off])
#ARG_OPTIONAL_SINGLE([log_dir], [], [The directory of validate log put in], [workspace/logs])
#ARG_OPTIONAL_SINGLE([validate_tool_jar_home], [], [The directory that validate tool jar put on, This directory relative to current script directory.], [workspace])
#ARG_OPTIONAL_SINGLE([upload_report], [] , [Upload the test report to Github], [off])
#ARG_OPTIONAL_SINGLE([issue_no], [], [The issue no that this report relative to], [UNKNOWN])
#ARG_OPTIONAL_SINGLE([validate_log_url_prefix], [], [The url prefix of validate log url])
#DEFINE_SCRIPT_DIR([BUILD_HOME])
#ARG_HELP()
#ARGBASH_GO
# [
getCommitter(){
	BRANCH_URL=$1
	proto="$(echo $BRANCH_URL | grep :// | sed -e's,^\(.*://\).*,\1,g')"
	url="$(echo ${BRANCH_URL/$proto/})"
	echo $url | grep / | cut -d '/' -f 2
}

TEST_TIME=`date "+%Y-%m-%d-%H-%M"`
TEST_TIME_YEAR=`echo $TEST_TIME | cut -d '-' -f 1`
TEST_TIME_MONTH_STR=`echo $TEST_TIME | cut -d '-' -f 2`
TEST_TIME_MONTH=$((10#$TEST_TIME_MONTH_STR))
TEST_TIME_MONTH=${TEST_TIME_MONTH#0}
TEST_CASES_BRANCH=${_arg_testcase_branch}
AGENT_BRANCH=${_arg_agent_branch}
TEST_CASES_DIR=`cd ${_arg_testcase_home} >/dev/null; pwd`
REPORT_DIR=`cd ${_arg_target_dir} >/dev/null; pwd`
VALIDATE_TOOL_JAR_HOME=`cd ${BUILD_HOME}/${_arg_validate_tool_jar_home} >/dev/null; pwd`
NORMALIZED_TEST_CASES_BRANCH=${TEST_CASES_BRANCH//\//-}
COMMITTER=`getCommitter ${_arg_agent_repo}`
TEST_CASES_COMMITID=${_arg_testcase_commitid}
AGENT_COMMIT=${_arg_agent_commitid}
ISSUE_NO=${_arg_issue_no}
VALIDATE_LOG_URL_PREFIX=${_arg_validate_log_url_prefix}

java -DtestDate="$TEST_TIME" \
	-DagentBranch="$AGENT_BRANCH" -DagentCommit="$AGENT_COMMIT" \
	-DtestCasePath="$TEST_CASES_DIR" -DreportFilePath="$REPORT_DIR" \
	-DcasesBranch="$NORMALIZED_TEST_CASES_BRANCH" -DcasesCommitId="${TEST_CASES_COMMITID}" \
	-Dcommitter="$COMMITTER"	\
	-jar ${VALIDATE_TOOL_JAR_HOME}/skywalking-autotest.jar > ${_arg_log_dir}/validate_${TEST_TIME}.log

if [ ! -f "$REPORT_DIR/${AGENT_GIT_BRANCH}" ]; then
	mkdir -p $REPORT_DIR/${AGENT_GIT_BRANCH}
fi

if [ "${_arg_overwrite_readme}" = "on" ]; then
	cp -f $REPORT_DIR/${TEST_TIME_YEAR}/${TEST_TIME_MONTH}/${COMMITTER}/testReport-${NORMALIZED_TEST_CASES_BRANCH}-${TEST_TIME}.md $REPORT_DIR/README.md
fi

if [ "${_arg_upload_report}" = "on" ]; then
    cd ${_arg_target_dir} && git add $REPORT_DIR/README.md && git add $REPORT_DIR/${TEST_TIME_YEAR}/${TEST_TIME_MONTH}/${COMMITTER}/testReport-${NORMALIZED_TEST_CASES_BRANCH}-${TEST_TIME}.md && git commit -m "push report report-${TEST_TIME}.md" .

    if [ ! -z "$GITHUB_ACCOUNT" ]; then
	    git config remote.origin.url https://${GITHUB_ACCOUNT}@github.com/SkywalkingTest/agent-integration-test-report.git
    fi

    git push origin master

    if [ "$ISSUE_NO" = "UNKNOWN" ]; then
        echo "issue no is empty, ignore to push comment"
    else
      curl --user ${GITHUB_ACCOUNT} -X POST -H "Content-Type: text/plain" -d "{\"body\":\"Here is the [test report](http://github.com/SkywalkingTest/agent-integration-test-report/tree/master/${TEST_TIME_YEAR}/${TEST_TIME_MONTH}/${COMMITTER}/testReport-${NORMALIZED_TEST_CASES_BRANCH}-${TEST_TIME}.md) and [validate logs](${VALIDATE_LOG_URL_PREFIX}/validate_${TEST_TIME}.log)\"}" https://api.github.com/repos/apache/incubator-skywalking/issues/${ISSUE_NO}/comments

      #echo "[INFO] Test report: http://github.com/SkywalkingTest/agent-integration-test-report/tree/master/${TEST_TIME_YEAR}/${TEST_TIME_MONTH}/${COMMITTER}/testReport-${NORMALIZED_TEST_CASES_BRANCH}-${TEST_TIME}.md"
      #echo "[INFO] Validate logs: ${VALIDATE_LOG_URL_PREFIX}/validate-$TEST_TIME.log"
    fi
fi

echo ""
echo ""
echo "[INFO] ---------------------------Here is the test report context -------------------------------------"
echo ""
cat $REPORT_DIR/${TEST_TIME_YEAR}/${TEST_TIME_MONTH}/${COMMITTER}/testReport-${NORMALIZED_TEST_CASES_BRANCH}-${TEST_TIME}.md
echo ""
echo "[INFO] -----------------------------------end the report-----------------------------------------------"
# ]
