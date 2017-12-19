#!/bin/bash

parse_yaml() {
    local yaml_file=$1
    local s
    local w
    local fs

    s='[[:space:]]*'
    w='[a-zA-Z0-9_.-]*'
    fs="$(echo @|tr @ '\034')"

    (
        sed -ne '/^--/s|--||g; s|\"|\\\"|g; s/\s*$//g;' \
            -e "/#.*[\"\']/!s| #.*||g; /^#/s|#.*||g;" \
            -e "s|^\($s\)\($w\)$s:$s[\"']\(.*\)[\"']$s\$|\1$fs\2$fs\3|p" \
            -e "s|^\($s\)\($w\)$s[:-]$s\(.*\)$s\$|\1$fs\2$fs\3|p" |

        awk -F"$fs" '{
            indent = length($1)/2;
            if (length($2) == 0) { conj[indent]="+";} else {conj[indent]="";}
            vname[indent] = $2;
            for (i in vname) {if (i > indent) {delete vname[i]}}
                if (length($3) > 0) {
                    vn=""; for (i=0; i<indent; i++) {vn=(vn)(vname[i])("_")}
                    printf("%s%s%s=(\"%s\")\n", vn, $2, conj[indent-1],$3);
                }
            }' |
            
        sed -e 's/_=/+=/g' \
            -e '/\..*=/s|\.|_|' \
            -e '/\-.*=/s|\-|_|'

    ) < "$yaml_file"
}

cleanEnvVariables(){
	testcase_support_versions=""
}

PRG="$0"
PRGDIR=`dirname "$PRG"`
[ -z "$AUTOTEST_HOME" ] && AUTOTEST_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

#
# define env variables
#
TEST_PROJECT_NAME=""
#
# Parse the input parameters
#
until [ $# -eq 0 ]
do
	case "$1" in
		--project )
			TEST_PROJECT_NAME=$2
			shift 2;
			;;
		* )
			;;
	esac
done

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
	if [ -f "$CASE_PROJECT_DIR/testcase.yml" ]; then
		return 0
	else
		return 1
	fi
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

			if [ "$TEST_PROJECT_NAME" != "" ] && [ "$TESTCASE_PROJECT" != "$TEST_PROJECT_NAME" ]; then
				continue
			fi

			echo "Begin to deploy ${TESTCASE_PROJECT}"
			#
			# Read config file 
			#
			TEST_CASE_CONFIG_FILE=$TESTCASE_PROJECT_DIR/testcase.yml
			eval "$(parse_yaml "$TEST_CASE_CONFIG_FILE")"
			#
			# read all support versions
			#
			SUPPORT_VERSIONS=${testcase_support_versions[@]} # Read `testcase.support_versions` value from `testcase.yml`
			TEST_FRAMEWORK=${testcase_test_framework} # Read `testcase.test_framework` value from `testcase.yml`
			TEST_CASE_REQUEST_URL=${testcase_request_url} # Read `testcase.request_url` value from `testcase.yml`
			#
			# go to TESTCASE_PROJECT_DIR
			#
			cd ${TESTCASE_PROJECT_DIR}
			#
			# iterate all profiles in test case projects
			#
			for SUPPORT_VERSION in ${SUPPORT_VERSIONS[@]}
			do
			 echo "execute mvn package docker:build -P${TESTCASE_PROJECT}-${SUPPORT_VERSION}"
			 mvn clean package docker:build -P${TESTCASE_PROJECT}-${SUPPORT_VERSION} > /dev/null
			 
			 TEST_CASE_DIR=$TEST_CASES_DIR/$TESTCASE_PROJECT-$SUPPORT_VERSION
			 mkdir $TEST_CASE_DIR
			 #
			 # copy the test case files
			 #
			 cp $TESTCASE_PROJECT_DIR/config/expectedData.yaml $TEST_CASE_DIR
			 eval sed -e 's/\{CASES_IMAGE_VERSION\}/$SUPPORT_VERSION/' $TESTCASE_PROJECT_DIR/config/docker-compose.yml > $TEST_CASE_DIR/docker-compose.yml
			 #
			 # Generate testcase desc file
			 #
			 touch $TEST_CASE_DIR/testcase.desc
			 echo "case.testFramework=$TEST_FRAMEWORK" >> $TEST_CASE_DIR/testcase.desc
			 echo "case.testVersion=$SUPPORT_VERSION" >> $TEST_CASE_DIR/testcase.desc
			 echo "case.request_url=$TEST_CASE_REQUEST_URL" >> $TEST_CASE_DIR/testcase.desc
			 echo "case.projectName=${TESTCASE_PROJECT}" >> $TEST_CASE_DIR/testcase.desc
			 
			 #
			 # To avoid contaminate the env variables 
			 #
			 cleanEnvVariables

			 echo "Build $TESTCASE_PROJECT:$SUPPORT_VERSION success."
			done
		else
			if [ -d "$TESTCASE_PROJECT" ]; then
				echo "$TESTCASE_PROJECT is not a test case project."
			fi
		fi

done

echo "Done!"

