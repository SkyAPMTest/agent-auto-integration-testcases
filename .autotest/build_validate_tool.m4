#!/bin/bash
#ARG_POSITIONAL_SINGLE([repo], [The repository of build project])
#ARG_POSITIONAL_SINGLE([branch], [The branch name of build project])
#ARG_POSITIONAL_SINGLE([target_dir], [The target directory])
#ARG_OPTIONAL_SINGLE([build], [], [skip to build project], "on")
#ARG_OPTIONAL_SINGLE()
#ARG_HELP()
#ARGBASH_GO
# [
PRG="$0"
PRGDIR=`dirname "$PRG"`
[ -z "$BUILD_HOME" ] && BUILD_HOME=`cd "$PRGDIR" >/dev/null; pwd`

#declare variables
WORKSPACE_DIR=${BUILD_HOME}/../workspace

${BUILD_HOME}/build_project.sh --build ${_arg_build} ${_arg_repo} ${_arg_branch} ${_arg_target_dir}

if [ ! -f "${_arg_target_dir}/target/skywalking-autotest.jar" ]; then
    echo "[ERROR] the validate tools is not exist. Please make sure the execute command without --no-build."
    exit -1
fi

echo "[INFO] copy validate tool jar"
cp ${_arg_target_dir}/target/skywalking-autotest.jar ${WORKSPACE_DIR}

# ]
