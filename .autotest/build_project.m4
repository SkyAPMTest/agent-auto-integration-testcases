#!/bin/bash
#ARG_POSITIONAL_SINGLE([repo], [The repository of build project])
#ARG_POSITIONAL_SINGLE([branch], [The branch name of build project])
#ARG_POSITIONAL_SINGLE([target_dir], [The target directory])
#ARG_OPTIONAL_SINGLE([build], [], [skip to build project], [off])
#ARG_OPTIONAL_SINGLE()
#ARG_HELP()
#ARGBASH_GO
# [
PROJECT_DIR=${_arg_target_dir}
if [ ! -d "${PROJECT_DIR}" ]; then
    echo "[INFO] clone project[${_arg_name}] from ${_arg_repo}."
    git clone --depth 1 ${_arg_repo} ${PROJECT_DIR}
fi

cd ${PROJECT_DIR} && git fetch --tags --progress ${_arg_repo} +refs/heads/*:refs/remotes/origin/* && (git rev-parse origin/${_arg_branch}^{commit} | xargs git checkout -f ) && git submodule init && git submodule update

if [ "${_arg_build}" = "on" ]; then
    cd ${PROJECT_DIR} && mvn clean package -Dmaven.test.skip=true
fi

# ]
