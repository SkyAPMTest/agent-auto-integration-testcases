#!/bin/bash

PRG="$0"
PRGDIR=`dirname "$PRG"`
[ -z "$AUTOTEST_HOME" ] && AUTOTEST_HOME=`cd "$PRGDIR" >/dev/null; pwd`

#
# Set Auto test project environment
#
TEST_CASES_DIR=$AUTOTEST_HOME/testcases
echo "Clear test cases dir"
rm -rf $TEST_CASES_DIR
echo "Create test cases dir"
mkdir $TEST_CASES_DIR

checkIfCaseProject(){
	CASE_PROJECT_DIR=$1
	if [ -d "$CASE_PROJECT_DIR/config" ]; then
		return 0
	else
		return 1
	fi
}

function prop {
	FILE=$1
	KEY=$2
    grep "${KEY}" ${FILE}|cut -d'=' -f2
}
#
# Iterate all project in skywalking-autotest-scenario
#
for TESTCASE_PROJECT in `ls $AUTOTEST_HOME`
	do
		TESTCASE_PROJECT_DIR="${AUTOTEST_HOME}/${TESTCASE_PROJECT}"
		checkIfCaseProject $TESTCASE_PROJECT_DIR
		IS_TESTCASE_PROJECT=$?
		if [ "$IS_TESTCASE_PROJECT" = "0" ]; then
			echo "Begin to deploy ${TESTCASE_PROJECT}"
			#
			# source $TESTCASE_PROJECT_DIR/config/testcase.properties
			#
			TEST_CASE_DESC=$TESTCASE_PROJECT_DIR/config/testcase.desc
			OLD_IFS="$IFS"
			IFS=","
			#
			# read all support versions
			#
			SUPPORT_VERSIONS=($(prop $TEST_CASE_DESC "case.support.versions"))
			IFS="$OLD_IFS"
			#
			#
			TEST_FRAMEWORK=$(prop $TEST_CASE_DESC "case.test.framework")
			#
			# go to TESTCASE_PROJECT_DIR
			#
			cd ${TESTCASE_PROJECT_DIR}
			#
			# iterate all profiles in test case projects
			#
			for SUPPORT_VERSION in ${SUPPORT_VERSIONS[@]}
			do
			 echo "execute mvn package docker:build -P${TEST_FRAMEWORK}-${SUPPORT_VERSION}"
			 mvn clean package docker:build -P${TEST_FRAMEWORK}-${SUPPORT_VERSION} >/dev/null
			 EXECUTE_RESULT=$?
			 if [ "$EXECUTE_RESULT" = "0" ]; then
			 	echo "Build $TESTCASE_PROJECT:$SUPPORT_VERSION success."
			 else
			 	echo "Build $TESTCASE_PROJECT:$SUPPORT_VERSION failure."
			 fi

			 TEST_CASE_DIR=$TEST_CASES_DIR/$TESTCASE_PROJECT-$SUPPORT_VERSION
			 mkdir $TEST_CASE_DIR
			 #
			 # copy the test case files
			 #
			 cp $TESTCASE_PROJECT_DIR/config/expectedData.yaml $TEST_CASE_DIR
			 eval sed -e 's/\{CASES_IMAGE_VERSION\}/$SUPPORT_VERSION/' $TESTCASE_PROJECT_DIR/config/docker-compose.yml > $TEST_CASE_DIR/docker-compose.yml
			 cp $TESTCASE_PROJECT_DIR/config/testcase.desc $TEST_CASE_DIR/testcase.desc
			 echo "# The components of current test case used" >> $TEST_CASE_DIR/testcase.desc
			 echo "case.components=$TEST_FRAMEWORK-$SUPPORT_VERSION" >> $TEST_CASE_DIR/testcase.desc

			done
		else
			if [ -d "$TESTCASE_PROJECT" ]; then
				echo "$TESTCASE_PROJECT is not a test case project."
			fi
		fi
done

echo "Done!"

