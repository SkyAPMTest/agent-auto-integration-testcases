#!/bin/bash
usage(){
	echo "Usage: agent-test.sh [OPTIONS]"
	echo "Options:"
	echo -e "  -r, --repo \t\t\t skywalking repository url"
	echo -e "  -b, --branch, -t, --tag \t skywalking repository branch or tag"
	echo -e "  -p, --only-pull-code \t\t only pull source code, not clone source code"
	echo -e "      --skipReport \t\t skip report "
	echo -e "      --skipBuild \t\t skip build"
	echo -e "      --reportFileMode  CONVERAGE | NONE \t\t skip build"
	echo -e "  --issueNo issueNo"
	echo -e "  --validateLogURL validate log url prefix"
}

# environment check
environmentCheck(){
	echo "Environment List: "
	# check git
	GIT_VERSION=$(git --version)
	if [ $? -ne 0 ];then
		echo " Failed to found git."
		exit 1
	else
		echo " git version: ${GIT_VERSION}"
	fi

	# check maven
	MAVE_VERSION=$(mvn -v | head -n 1)
	if [ $? -ne 0 ];then
		echo " Failed to found maven."
		exit 1
	else
		echo " maven version: $MAVE_VERSION"
	fi

	# check java
	[ -z "$JAVA_HOME" ] && EXECUTE_JAVA=${JAVA_HOME}/bin/java
	if [ "$EXECUTE_JAVA" = "" ]; then
		EXECUTE_JAVA=java
	fi
	JAVA_VERSION=$($EXECUTE_JAVA -version 2>&1 | head -n 1)
	if [ $? -ne 0 ];then
		echo " Failed to found java."
		exit 1
	else
		echo " java version: $JAVA_VERSION"
	fi
}

# clear workspace
clearWorkspace(){
	if [ "$PULL_CODE" = "false" ]; then
		rm -rf $WORKSPACE_DIR/*
	fi
}

# checkout source code
checkoutSourceCode(){
	REPO_URL=$1
	REPO_BRANCH=$2
	SOURCE_CODE_DIR=$3
	if [ "$PULL_CODE" = "false" ]; then
		git clone $REPO_URL "$SOURCE_CODE_DIR" 1>&2 > /dev/null
	fi

	eval "cd $SOURCE_CODE_DIR && git reset --hard && git checkout $REPO_BRANCH && git fetch origin && git pull origin $REPO_BRANCH" 1>&2 > /dev/null
	cd $SOURCE_CODE_DIR && LAST_COMMIT=$(git rev-parse HEAD)
	echo $LAST_COMMIT
}

buildProject(){
	PROJECT_DIR=$1;
	if [ "$SKIP_BUILD" = "false" ]; then
		cd $PROJECT_DIR
		mvn clean package -Dmaven.test.skip=true
		IS_BUILD_SUCCESS=$?
		if [ "$IS_BUILD_SUCCESS" = "0" ]; then
			echo "Build project success"
		else
			echo "Build project failed"
		fi
	fi
}

getCommitter(){
	BRANCH_URL=$1
	proto="$(echo $BRANCH_URL | grep :// | sed -e's,^\(.*://\).*,\1,g')"
	url="$(echo ${BRANCH_URL/$proto/})"
	echo $url | grep / | cut -d '/' -f 2
}

environmentCheck

#
# definetation env variables
#
TEST_TOOL_GIT_URL=https://github.com/SkywalkingTest/agent-integration-testtool.git
TEST_TOOL_GIT_BRANCH=master
AGENT_GIT_URL=https://github.com/apache/incubator-skywalking.git
AGENT_GIT_BRANCH=master
REPORT_GIT_URL=https://github.com/SkywalkingTest/agent-integration-test-report.git
REPORT_GIT_BRANCH=master
TEST_TIME=`date "+%Y-%m-%d-%H-%M"`
TEST_TIME_YEAR=`echo $TEST_TIME | cut -d '-' -f 1`
TEST_TIME_MONTH_STR=`echo $TEST_TIME | cut -d '-' -f 2`
TEST_TIME_MONTH=$((10#$TEST_TIME_MONTH_STR))
TEST_TIME_MONTH=${TEST_TIME_MONTH#0}
TEST_CASES_COMMITID=""
TEST_CASES_BRANCH=""
TEST_CASES=()
COMMITTER=`getCommitter $AGENT_GIT_URL`
TEST_CASES_STR=""
PULL_CODE=false
SKIP_REPORT=false
SKIP_BUILD=false
REPORT_FILE_MODE="CONVERAGE"
PRG="$0"
PRGDIR=`dirname "$PRG"`
[ -z "$AGENT_TEST_HOME" ] && AGENT_TEST_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`
WORKSPACE_DIR="$AGENT_TEST_HOME/workspace"
#
# log dir
#
LOGS_DIR="$WORKSPACE_DIR/logs"
#
SOURCE_DIR="$WORKSPACE_DIR/sources"
#
TEST_CASES_DIR="$AGENT_TEST_HOME/testcases" # Testcase dir
#
MAX_RUNNING_SIZE=2 # The max size of running testcase
#
ISSUE_NO=""
#
VALIDATE_LOG_URL_PREFIX=""
#
#
#
############## parse paremeters ##############
#	parse paremeters
##############################################
until [ $# -eq 0 ]
do
	case "$1" in
		-r | --repo )
			AGENT_GIT_URL=$2;
			COMMITTER=`getCommitter $AGENT_GIT_URL`
			shift 2;
			;;
		-b | --branch | -t | --tag )
			AGENT_GIT_BRANCH=$2;
			shift 2;
			;;
		-p | --only-pull-code )
			PULL_CODE=true;
			shift;
			;;
		--skipReport )
			SKIP_REPORT=true;
			shift;
			;;
		--max-running-size )
			MAX_RUNNING_SIZE=$2;
			shift 2;
			;;
		--skipBuild )
			SKIP_BUILD=true;
			shift;
			;;
		--reportFileMode )
			REPORT_FILE_MODE=$2;
			shift 2;
			;;
		--testcase-branch )
			TEST_CASES_BRANCH=$2;
			shift 2;
			;;
		--issueNo )
		    ISSUE_NO=$2
		    shift 2;
		    ;;
		--validateLogURL )
		    VALIDATE_LOG_URL_PREFIX=$2
		    shift 2;
		    ;;
		* )
			usage;
			exit 1;
			;;
	esac
done
#
#
#
echo ""
echo "------------------------------------------------------------------------"
echo "-                          Envrionment variables                       -"
echo "------------------------------------------------------------------------"
echo -e "The repository url of skywalking:  $AGENT_GIT_URL"
echo -e "The  branch  name  of skywalking:  $AGENT_GIT_BRANCH"
echo -e "The         test            date:  ${TEST_TIME}"
echo -e "The  branch  name  of   testcase:  ${TEST_CASES_BRANCH}"
echo "------------------------------------------------------------------------"
echo ""
echo ""
#
#
#
TEST_CASES_COMMITID=$(cd $AGENT_TEST_HOME && git rev-parse HEAD)
echo "current test case branch: ${TEST_CASES_BRANCH}. current test case commit id: ${TEST_CASES_COMMITID}"
#
#
#
echo "clear Workspace"
clearWorkspace
#
# create log directory
#
LOGS_DIR="$WORKSPACE_DIR/logs"
if [ ! -d "$LOGS_DIR" ];then
    mkdir -p "$LOGS_DIR"
fi
############## build agent ##############
#	1. checkout agent source code
#	2. switch branch
#	3. mvn build
#	4. copy agent to $AGENT_DIR
########################################
echo "clone agent source code"
AGENT_COMMIT=`checkoutSourceCode ${AGENT_GIT_URL} $AGENT_GIT_BRANCH $SOURCE_DIR/skywalking`
#
# init submodule
#
cd $SOURCE_DIR/skywalking && git submodule init && git submodule update
#
# To build ignore plug-in, we need to copy dependencies jar to ci-depencies foleder
#
DEPENDENCIES_JARS_FOLDERS=${AGENT_TEST_HOME}/dependencies-jars
#echo "clone agent"
buildProject $SOURCE_DIR/skywalking
echo "agent branch: ${AGENT_GIT_BRANCH}, agent commit: ${AGENT_COMMIT}"
#echo "checkout agent and mvn build"
AGENT_DIR="$WORKSPACE_DIR/agent"
AGENT_WITH_OPTIONAL_PLUGIN_DIR="$WORKSPACE_DIR/agent-with-optional-plugins"
if [ ! -f "${AGENT_DIR}" ]; then
	  mkdir -p ${AGENT_DIR}
    mkdir -p ${AGENT_WITH_OPTIONAL_PLUGIN_DIR}
fi
echo "copy agent jar to $AGENT_DIR"
#echo "copy agent"
cp -r $SOURCE_DIR/skywalking/skywalking-agent/* $AGENT_DIR/
#echo "copy agent with optional plugin"
cp -r $SOURCE_DIR/skywalking/skywalking-agent/* $AGENT_WITH_OPTIONAL_PLUGIN_DIR/
#
# Copy the optional plugins
#
cp -r $AGENT_WITH_OPTIONAL_PLUGIN_DIR/optional-plugins/* $AGENT_WITH_OPTIONAL_PLUGIN_DIR/plugins/
############ build test tool ###########
#	1. checkout test tool code
#	2. switch branch
#	3. mvn build
#	4. copy agent to $WORKSPACE_DIR
########################################
echo "clone test tool source code"
#echo "clone test tool and build"
echo "eval checkoutSourceCode $TEST_TOOL_GIT_URL $TEST_TOOL_GIT_BRANCH $SOURCE_DIR/test-tools"
checkoutSourceCode $TEST_TOOL_GIT_URL $TEST_TOOL_GIT_BRANCH $SOURCE_DIR/test-tools
echo "eval buildProject $SOURCE_DIR/test-tools"
buildProject $SOURCE_DIR/test-tools
echo "copy test tools to ${WORKSPACE_DIR}"
#echo "copy auto-test.jar"
cp ${SOURCE_DIR}/test-tools/target/skywalking-autotest.jar ${WORKSPACE_DIR}

######### downlod test cases ###########
#	1. checkout test tool code
#	2. switch branch
########################################
cd $TEST_CASES_DIR
#echo "clone test cases git url"
for TEST_CASE in `ls $TEST_CASES_DIR`
do
	if [ -d "$TEST_CASES_DIR/$TEST_CASE" ]; then
		TEST_CASES=(${TEST_CASES[*]} $TEST_CASE)
		TEST_CASES_STR="$TEST_CASES_STR,$TEST_CASE"
	fi
done
echo "Here is the test cases: ${TEST_CASES_STR}"

##### downlod report repository ########
#	1. checkout report repository
#	2. switch branch
########################################
echo "clone report repository"
REPORT_DIR="$WORKSPACE_DIR/report"
#echo "clone report "
checkoutSourceCode ${REPORT_GIT_URL} ${REPORT_GIT_BRANCH} ${REPORT_DIR}
#
#
#
RUNNTING_INDEXES=()
for i in `seq 0 1 $MAX_RUNNING_SIZE` ;
do
	RUNNTING_INDEXES+=($i);
done
#
#
#
deployTestCase(){
	TEST_CASE=$1
	RUNNING_SIZE=$2
	CURRENT_RUNNING_INDEX=$3 #
	#
	# calculate running port
	#

	COLLECTOR_OUTPUT_PORT=$((12800 + $CURRENT_RUNNING_INDEX * 100))
	SERVER_OUTPUT_PORT=$((18080 + $CURRENT_RUNNING_INDEX * 100))
	RECIEVE_DATA_URL=http://127.0.0.1:${COLLECTOR_OUTPUT_PORT}/receiveData
	echo "[ ${TEST_CASE} ]: collector running in ${COLLECTOR_OUTPUT_PORT}, The service running in ${SERVER_OUTPUT_PORT}"
	#
	# running time 
	#
	CASE_DIR="$TEST_CASES_DIR/$TEST_CASE"
	# replace the port in docker-compose.xml file and testcase.desc file
	#
	eval sed -i -e 's/\{COLLECTOR_OUTPUT_PORT\}/$COLLECTOR_OUTPUT_PORT/' $CASE_DIR/docker-compose.yml # replace value of `{COLLECTOR_OUTPUT_PORT}` parameter in docker-compose.xml
	eval sed -i -e 's/\{SERVER_OUTPUT_PORT\}/$SERVER_OUTPUT_PORT/' $CASE_DIR/docker-compose.yml # replace value of `{SERVER_OUTPUT_PORT}` parameter in docker-compose.xml
	eval sed -i -e 's/\{SERVER_OUTPUT_PORT\}/$SERVER_OUTPUT_PORT/' $CASE_DIR/testcase.desc # replace value of `{SERVER_OUTPUT_PORT}` parameter in testcase.desc
	eval sed -i -e 's/\{SERVER_OUTPUT_PORT\}/$SERVER_OUTPUT_PORT/' $CASE_DIR/expectedData.yaml # replace value of `{SERVER_OUTPUT_PORT}` parameter in testcase.desc
	cd $CASE_DIR && docker-compose rm -s -f -v 
	
	echo "start docker container"
	docker-compose -f $CASE_DIR/docker-compose.yml up -d
	sleep 60

	CASE_REQUEST_URL=$(grep "case.request_url" $CASE_DIR/testcase.desc | awk -F '=' '{print $2}')
	echo $CASE_REQUEST_URL
	curl -s $CASE_REQUEST_URL > /dev/null
	sleep 15

	curl -s $RECIEVE_DATA_URL > $CASE_DIR/actualData.yaml

	echo "stop docker container and remove the container network "
	docker-compose -f ${CASE_DIR}/docker-compose.yml stop > /dev/null
	(docker network rm $(docker network ls | grep "bridge" | awk '/ / { print $1 }')) > /dev/null
	#docker network prune -f
	#
	# remove the rid file
	#
	#RUNNTING_INDEXES+=($CURRENT_RUNNING_INDEX)
	echo "$CURRENT_RUNNING_INDEX FINISHED" > $RUNTIME_DIR/$TEST_CASE.rid
}
#
# Create runtime dir
#
RUNTIME_DIR=${AGENT_TEST_HOME}/.runtime
rm -rf "$RUNTIME_DIR/*"
mkdir -p $RUNTIME_DIR
#
#
#
while [ ${#TEST_CASES[@]} -gt 0 ]; do
	#
	#
	#
	FINISHED_INDEX_ARRAY=($(grep  -h 'FINISHED' $RUNTIME_DIR/* | awk -F ' ' '{print $1;}'))
	RUNNTING_INDEXES+=(${FINISHED_INDEX_ARRAY[@]})
	# remove finished files
	grep -l 'FINISHED' $RUNTIME_DIR/*.rid | xargs rm -rf
	#
	#
	#
	RUNNING_SIZE=`grep -l 'STARTED' $RUNTIME_DIR/* | wc -l`
	if [ $((MAX_RUNNING_SIZE - RUNNING_SIZE)) -eq 0 ]; then
		echo "Remainder run size is 0. retry in 20 seconds."
		sleep 20
		continue
	fi
	#
	#
	#
	TEST_CASE=${TEST_CASES[0]}
	TEST_CASES=("${TEST_CASES[@]:1}")
	RUNNING_INDEX=${RUNNTING_INDEXES[0]}
	RUNNTING_INDEXES=("${RUNNTING_INDEXES[@]:1}")
	#
	# create rid file
	#
	echo "$RUNNING_INDEX STARTED" > $RUNTIME_DIR/$TEST_CASE.rid
	deployTestCase $TEST_CASE $RUNNING_SIZE $RUNNING_INDEX &
	echo "${TEST_CASE} start to running"
done

RUNNING_SIZE=`grep -l 'STARTED' $RUNTIME_DIR/* | wc -l`
while [[ $RUNNING_SIZE -gt 0 ]]; do
	echo "$RUNNING_SIZE containers are running, The validate program will be retried in 40 seconds."
	sleep 40;
	RUNNING_SIZE=`grep -l 'STARTED' $RUNTIME_DIR/* | wc -l`
done

echo "normalize test branch name"
NORMALIZED_TEST_CASES_BRANCH=${TEST_CASES_BRANCH//\//-}
echo "generate report...."
java -DtestDate="$TEST_TIME" \
	-DagentBranch="$AGENT_GIT_BRANCH" -DagentCommit="$AGENT_COMMIT" \
	-DtestCasePath="$TEST_CASES_DIR" -DreportFilePath="$REPORT_DIR" \
	-DcasesBranch="$NORMALIZED_TEST_CASES_BRANCH" -DcasesCommitId="${TEST_CASES_COMMITID}" \
	-Dcommitter="$COMMITTER"	\
	-jar $WORKSPACE_DIR/skywalking-autotest.jar > $LOGS_DIR/validate-$TEST_TIME.log

if [ ! -f "$REPORT_DIR/${AGENT_GIT_BRANCH}" ]; then
	mkdir -p $REPORT_DIR/${AGENT_GIT_BRANCH}
fi

if [ "$REPORT_FILE_MODE" = "CONVERAGE" ]; then
	cp -f $REPORT_DIR/${TEST_TIME_YEAR}/${TEST_TIME_MONTH}/${COMMITTER}/testReport-${NORMALIZED_TEST_CASES_BRANCH}-${TEST_TIME}.md $REPORT_DIR/README.md
fi

if [ "$SKIP_REPORT" = "false" ]; then
	echo "push report...."
	cd $REPORT_DIR
	git add $REPORT_DIR/README.md
	git add $REPORT_DIR/${TEST_TIME_YEAR}/${TEST_TIME_MONTH}/${COMMITTER}/testReport-${NORMALIZED_TEST_CASES_BRANCH}-${TEST_TIME}.md 
	git commit -m "push report report-${TEST_TIME}.md" .

	if [ ! -z "$GITHUB_ACCOUNT" ]; then
		echo "set remote origin url"
		git config remote.origin.url https://${GITHUB_ACCOUNT}@github.com/SkywalkingTest/agent-integration-test-report.git
	fi

	git push origin master
else
	echo "skipt push report"
fi

if [ "$ISSUE_NO" = "UNKNOWN" ]; then
    echo "issue no is empty, ignore to push comment"
else
    curl --user ${GITHUB_ACCOUNT} -X POST -H "Content-Type: text/plain" -d "{\"body\":\"Here is the [test report](http://github.com/SkywalkingTest/agent-integration-test-report/tree/master/${TEST_TIME_YEAR}/${TEST_TIME_MONTH}/${COMMITTER}/testReport-${TEST_CASES_BRANCH}-${TEST_TIME}.md) and [validate logs](${VALIDATE_LOG_URL_PREFIX}/validate-$TEST_TIME.log)\"}" https://api.github.com/repos/apache/incubator-skywalking/issues/${ISSUE_NO}/comments
fi


#
# clear unused images
#
(docker images | grep '<none>' | awk '{print $3}' | xargs docker rmi -f) > /dev/null
(docker ps -a | awk '{print $1}' | xargs docker rm -f -v) > /dev/null
#(docker images | grep skywalking | grep -v 'skywalking/skywalking-mock-collector' | awk '{print $3}' | xargs docker rmi -f) > /dev/null
echo "clear images Done!"
#
#
#
echo "Build successfully!"

