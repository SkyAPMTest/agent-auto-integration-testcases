#!/bin/bash
PRG="$0"
PRGDIR=`dirname "$PRG"`
[ -z "$BUILD_HOME" ] && BUILD_HOME=`cd "$PRGDIR" >/dev/null; pwd`

for file in `find ${BUILD_HOME} -type f -name \*.m4 |sed -e 's/\.m4$//'` 
do
    echo "[INFO] generate target file for ${file}.m4"
    argbash ${file}.m4 -o ${file}.sh
done
