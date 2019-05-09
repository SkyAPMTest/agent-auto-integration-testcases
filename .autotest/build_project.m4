#!/bin/bash
#ARG_POSITIONAL_SINGLE([repo], [The repository of build project])
#ARG_POSITIONAL_SINGLE([branch], [The branch name of build project])
#ARG_POSITIONAL_SINGLE([target_dir], [The target directory])
#ARG_OPTIONAL_SINGLE([clone_code], [], [skip to clone code], [off])
#ARG_OPTIONAL_SINGLE([fetch_latest_code], [], [fetch latest code], [on])
#ARG_OPTIONAL_SINGLE()
#ARG_HELP()
#ARGBASH_GO
# [
PROJECT_DIR=${_arg_target_dir}


if [ ! -d "${PROJECT_DIR}" ] || [ "${_arg_clone_code}" = "on" ]; then
  echo "[INFO] clone project[${_arg_name}] from ${_arg_repo}."
  rm -rf ${PROJECT_DIR} && git clone --depth 1 ${_arg_repo} ${PROJECT_DIR}
else
  echo "[INFO] skip to clone project[${_arg_repo}]."
fi

if [ "${_arg_fetch_latest_code}" = "on" ]; then
  cd ${PROJECT_DIR} && git fetch --tags --progress ${_arg_repo} +refs/heads/*:refs/remotes/origin/* && (git rev-parse origin/${_arg_branch}^{commit} | xargs git checkout -f ) && git submodule init && git submodule update
fi

cd ${PROJECT_DIR} && git checkout ${_arg_branch}
# ]
