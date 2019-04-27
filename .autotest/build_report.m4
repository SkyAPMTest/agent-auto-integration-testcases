#!/bin/bash
#ARG_POSITIONAL_SINGLE([target_dir], [The target directory])
#DEFINE_SCRIPT_DIR([BUILD_HOME])
#ARG_HELP()
#ARGBASH_GO
# [
${BUILD_HOME}/build_project.sh --build off https://github.com/SkywalkingTest/agent-integration-test-report.git master ${_arg_target_dir} && cd ${_arg_target_dir} && git checkout master
# ]
