#!/bin/bash
#ARG_POSITIONAL_SINGLE([scenario_home], [The directory of scenario])
#ARG_POSITIONAL_INF([scenarios], [The scenarios to be run])
#ARG_OPTIONAL_SINGLE([target_dir], [o], [The target directory of output, This folder relative to directory of testcase repository.], "testcases")
#ARG_OPTIONAL_SINGLE([agent_dir], [], [The directory of agent, This folder relative to directory of testcase repository.], "workspace/agent")
#ARG_OPTIONAL_SINGLE([agent_with_optional_dir], [], [The directory of agent that it contain the optional plugins, This folder relative to directory of testcase repository.], "workspace/agent-with-optional-plugins")
#ARG_OPTIONAL_SINGLE([collector_image_version], [], [The image version of collector], "6.0.0-2018")
#ARG_OPTIONAL_SINGLE([skip_single_mode], [], [Skip build the scenario with single mode], [on])
#ARG_OPTIONAL_BOOLEAN([skip_build], [], [Skip build the scenario project], [on])
#DEFINE_SCRIPT_DIR([AGENT_TEST_HOME])
#ARG_HELP()
#ARGBASH_GO
# [

# declare functions
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
	testcase_support_versions=()
    testcase_running_mode=""
    testcase_test_framework=""
    testcase_request_url=""
}

#declare variables
SCENARIOS_HOME=`cd ${_arg_scenario_home} >/dev/null; pwd`
CASES_HOME=${SCENARIOS_HOME}/${_arg_target_dir}
AGENT_DIR=${SCENARIOS_HOME}/${_arg_agent_dir}
AGENT_WITH_OPTIONAL_PLUGINS_DIR=${SCENARIOS_HOME}/${_arg_agent_with_optional_dir}
COLLECTOR_IMAGE_VERSION=${_arg_collector_image_version}
ESCAPE_AGENT_DIR=$(echo "$AGENT_DIR" |sed -e 's/\//\\\//g' )
ESCAPE_AGENT_WITH_OPTIONAL_PLUGINS_DIR=$(echo "$AGENT_WITH_OPTIONAL_PLUGINS_DIR" |sed -e 's/\//\\\//g' )

#
rm -rf ${CASES_HOME} && mkdir -p ${CASES_HOME}

for SCENARIO in ${_arg_scenarios[@]}
do
    SCENARIO_HOME=${SCENARIOS_HOME}/${SCENARIO}
    SCENARIO_CONFIG_FILE=${SCENARIO_HOME}/testcase.yml
    echo "[INFO] start to deploy ${SCENARIO_HOME}"

    eval "$(parse_yaml "$SCENARIO_CONFIG_FILE")"
    SUPPORT_VERSIONS=${testcase_support_versions[@]}
    TEST_FRAMEWORK=${testcase_test_framework}
    TEST_CASE_REQUEST_URL=${testcase_request_url}
    RUNNING_MODE=${testcase_running_mode}

    # skip the running mode is SINGLE
    if [ "$RUNNING_MODE" = "SINGLE" ] && [ "${_arg_skip_single_mode}" = "on" ] ; then
        echo "[WARNING] ${SCENARIO} running mode: ${RUNNING_MODE}. skip this project"
        cleanEnvVariables
        continue
    fi

    AGENT_FILE_PATH="${ESCAPE_AGENT_DIR}"
    if [ "$RUNNING_MODE" = "WITH_OPTIONAL" ]; then
        AGENT_FILE_PATH="${ESCAPE_AGENT_WITH_OPTIONAL_PLUGINS_DIR}"
    fi

    cd ${SCENARIO_HOME}
	for SUPPORT_VERSION in ${SUPPORT_VERSIONS[@]}
	do
    if [ "${_arg_skip_build}" = "on" ]; then
      echo "[INFO] execute mvn package docker:build -P${SCENARIO}-${SUPPORT_VERSION}"
      mvn clean package docker:build -P${SCENARIO}-${SUPPORT_VERSION}
    else
      echo "[INFO] skip build the case ${SCENARIO} - ${SUPPORT_VERSION}"
    fi

	  TEST_CASE_DIR=$CASES_HOME/$SCENARIO-$SUPPORT_VERSION && mkdir -p $TEST_CASE_DIR

		cp $SCENARIO_HOME/config/expectedData.yaml $TEST_CASE_DIR
		cp $SCENARIO_HOME/config/docker-compose.yml $TEST_CASE_DIR
		eval sed -i '' -e 's/\{CASES_IMAGE_VERSION\}/$SUPPORT_VERSION/' $TEST_CASE_DIR/docker-compose.yml
		eval sed -i '' -e 's/\{COLLECTOR_IMAGE_VERSION\}/$COLLECTOR_IMAGE_VERSION/' $TEST_CASE_DIR/docker-compose.yml
    eval sed -i -e 's/\{AGENT_FILE_PATH\}/$AGENT_FILE_PATH/' $TEST_CASE_DIR/docker-compose.yml
		touch $TEST_CASE_DIR/testcase.desc
		echo "case.testFramework=$TEST_FRAMEWORK" >> $TEST_CASE_DIR/testcase.desc
		echo "case.testComponents=$SUPPORT_VERSION" >> $TEST_CASE_DIR/testcase.desc
		echo "case.request_url=$TEST_CASE_REQUEST_URL" >> $TEST_CASE_DIR/testcase.desc
		echo "case.projectName=${TESTCASE_PROJECT}" >> $TEST_CASE_DIR/testcase.desc
		cleanEnvVariables
    echo "[INFO] build $SCENARIO:$SUPPORT_VERSION success."
	done
done



# ]
