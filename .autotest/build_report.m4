#!/bin/bash
#ARG_POSITIONAL_SINGLE([target_dir], [The target directory])
#ARG_OPTIONAL_SINGLE([clone_code], [], [skip to clone code], [off])
#ARG_OPTIONAL_SINGLE([fetch_latest_code], [], [fetch latest code], [on])
#DEFINE_SCRIPT_DIR([BUILD_HOME])
#ARG_HELP()
#ARGBASH_GO
# [
${BUILD_HOME}/build_project.sh --fetch_latest_code ${_arg_fetch_latest_code} --clone_code ${_arg_clone_code} https://github.com/SkywalkingTest/agent-integration-test-report.git master ${_arg_target_dir} && cd ${_arg_target_dir} && git checkout master
# ]
