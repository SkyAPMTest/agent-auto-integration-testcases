#!/bin/bash
#ARG_POSITIONAL_SINGLE([repo], [The repository of build project])
#ARG_POSITIONAL_SINGLE([branch], [The branch name of build project])
#ARG_POSITIONAL_SINGLE([target_dir], [The target directory])
#ARG_OPTIONAL_SINGLE([build], [], [skip to build project], [on])
#ARG_HELP()
#ARGBASH_GO
# [
PRG="$0"
PRGDIR=`dirname "$PRG"`
[ -z "$BUILD_HOME" ] && BUILD_HOME=`cd "$PRGDIR" >/dev/null; pwd`

#declare variables
AGENT_DIR=${BUILD_HOME}/../workspace/agent
AGENT_WITH_OPTIONAL_PLUGIN_DIR=${BUILD_HOME}/../workspace/agent-with-optional-plugins

${BUILD_HOME}/build_project.sh --build ${_arg_build} ${_arg_repo} ${_arg_branch} ${_arg_target_dir} 

if [ ! -d "${_arg_target_dir}/skywalking-agent" ]; then
    echo "[ERROR] the agent folder is not exist, Please make sure the command without --no-build."
    exit -1
fi

echo "[INFO] copy agent package"
mkdir -p ${AGENT_DIR} && cp -r ${_arg_target_dir}/skywalking-agent/* ${AGENT_DIR}
echo "[INFO] build agent package with optional plugins"
mkdir -p ${AGENT_WITH_OPTIONAL_PLUGIN_DIR} && cp -r ${_arg_target_dir}/skywalking-agent/* ${AGENT_WITH_OPTIONAL_PLUGIN_DIR} && cp -r ${AGENT_WITH_OPTIONAL_PLUGIN_DIR}/optional-plugins/* ${AGENT_WITH_OPTIONAL_PLUGIN_DIR}/plugins/

# ]
