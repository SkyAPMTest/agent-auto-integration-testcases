#!/bin/bash

PRG="$0"
PRGDIR=`dirname "$PRG"`
[ -z "$AUTOTEST_HOME" ] && AUTOTEST_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

checkIfCaseProject(){
    CASE_PROJECT_DIR=$1
    if [ -f "$CASE_PROJECT_DIR/testcase.yml" ]; then
        return 0
    else
        return 1
    fi
}
#
# Iterate all testcase project
#
for TESTCASE_PROJECT in `ls $AUTOTEST_HOME`
do
    TESTCASE_PROJECT_DIR="${AUTOTEST_HOME}/${TESTCASE_PROJECT}"
    checkIfCaseProject $TESTCASE_PROJECT_DIR
    IS_TESTCASE_PROJECT=$?

    if [ "$IS_TESTCASE_PROJECT" = "0" ]; then
        echo "Start to build ${TESTCASE_PROJECT_DIR}"
        cd ${TESTCASE_PROJECT_DIR}
        mvn compile
    fi
done
