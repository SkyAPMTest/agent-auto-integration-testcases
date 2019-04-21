#!/bin/bash
#ARG_POSITIONAL_SINGLE([testcase_dir],[The directory of testcase])
#ARG_OPTIONAL_SINGLE([parallel_run_size], [m], [The size of running testcase at the same time], 1)
#ARG_HELP()
#ARGBASH_GO
# [
#declare variables
TESTCASES_HOME=`cd "${_arg_testcase_dir}" >/dev/null; pwd`
RUNTIME_DIR=${TESTCASES_HOME}/.runtime
MAX_RUNNING_SIZE=${_arg_parallel_run_size}
declare -a TESTCASES

# 
mkdir -p $RUNTIME_DIR && rm -rf "$RUNTIME_DIR/*" && touch $RUNTIME_DIR/place_holder.rid

# declare functions
deployTestCase(){
	TEST_CASE=$1
	RUNNING_SIZE=$2
	CURRENT_RUNNING_INDEX=$3 

	COLLECTOR_OUTPUT_PORT=$((12800 + $CURRENT_RUNNING_INDEX * 100))
	SERVER_OUTPUT_PORT=$((18080 + $CURRENT_RUNNING_INDEX * 100))
	RECIEVE_DATA_URL=http://127.0.0.1:${COLLECTOR_OUTPUT_PORT}/receiveData
	echo "[INFO] ${TEST_CASE} collector port: ${COLLECTOR_OUTPUT_PORT}, service port: ${SERVER_OUTPUT_PORT}"
	CASE_DIR="${TESTCASES_HOME}/$TEST_CASE"
  eval sed -i -e 's/\{COLLECTOR_OUTPUT_PORT\}/$COLLECTOR_OUTPUT_PORT/' $CASE_DIR/docker-compose.yml # replace value of `{COLLECTOR_OUTPUT_PORT}` parameter in docker-compose.xml
	eval sed -i -e 's/\{SERVER_OUTPUT_PORT\}/$SERVER_OUTPUT_PORT/' $CASE_DIR/docker-compose.yml # replace value of `{SERVER_OUTPUT_PORT}` parameter in docker-compose.xml
	eval sed -i -e 's/\{SERVER_OUTPUT_PORT\}/$SERVER_OUTPUT_PORT/' $CASE_DIR/testcase.desc # replace value of `{SERVER_OUTPUT_PORT}` parameter in testcase.desc
	eval sed -i -e 's/\{SERVER_OUTPUT_PORT\}/$SERVER_OUTPUT_PORT/' $CASE_DIR/expectedData.yaml # replace value of `{SERVER_OUTPUT_PORT}` parameter in testcase.desc
	cd $CASE_DIR && docker-compose rm -s -f -v 
	
  docker-compose -f $CASE_DIR/docker-compose.yml up -d 
	sleep 60
   
	CASE_REQUEST_URL=$(grep "case.request_url" $CASE_DIR/testcase.desc | awk -F '=' '{print $2}')
	echo "[INFO] ${TEST_CASE}: $CASE_REQUEST_URL"
	curl -s $CASE_REQUEST_URL > /dev/null
	sleep 30

	curl -s $RECIEVE_DATA_URL > $CASE_DIR/actualData.yaml

	docker-compose -f ${CASE_DIR}/docker-compose.yml stop > /dev/null
	(docker network rm $(docker network ls | grep "bridge" | awk '/ / { print $1 }')) > /dev/null
	echo "$CURRENT_RUNNING_INDEX FINISHED" > $RUNTIME_DIR/$TEST_CASE.rid
}

for TESTCASE in `ls ${TESTCASES_HOME}`
do
    if [ -f "${TESTCASES_HOME}/${TESTCASE}/testcase.desc" ]; then
        TESTCASES+=("${TESTCASE}")
    fi
done

RUNNTING_INDEXES=()
for i in `seq 0 1 $MAX_RUNNING_SIZE` ;
do
	RUNNTING_INDEXES+=($i);
done

echo ${TESTCASES[@]}
while [ ${#TESTCASES[@]} -gt 0 ]; do
	FINISHED_INDEX_ARRAY=($(grep  -h 'FINISHED' $RUNTIME_DIR/* | awk -F ' ' '{print $1;}'))
	RUNNTING_INDEXES+=(${FINISHED_INDEX_ARRAY[@]})
	grep -l 'FINISHED' $RUNTIME_DIR/*.rid | xargs rm -rf
	#
	RUNNING_SIZE=`grep -l 'STARTED' $RUNTIME_DIR/* | wc -l`
	if [ $((MAX_RUNNING_SIZE - RUNNING_SIZE)) -eq 0 ]; then
		echo "[INFO] Remainder run size is 0. retry in 20 seconds."
		sleep 20
		continue
	fi
	#
	TEST_CASE=${TESTCASES[0]}
	TESTCASES=("${TESTCASES[@]:1}")
	RUNNING_INDEX=${RUNNTING_INDEXES[0]}
	RUNNTING_INDEXES=("${RUNNTING_INDEXES[@]:1}")
	#
	echo "$RUNNING_INDEX STARTED" > $RUNTIME_DIR/$TEST_CASE.rid
  echo ${TEST_CASE} ${RUNNING_SIZE} ${RUNNING_INDEX}
	deployTestCase $TEST_CASE $RUNNING_SIZE $RUNNING_INDEX &
	echo "${TEST_CASE} start to running"
done

RUNNING_SIZE=`grep -l 'STARTED' $RUNTIME_DIR/* | wc -l`
while [[ $RUNNING_SIZE -gt 0 ]]; do
	echo "[INFO] $RUNNING_SIZE containers are running, The validate program will be retried in 20 seconds."
	sleep 20;
	RUNNING_SIZE=`grep -l 'STARTED' $RUNTIME_DIR/* | wc -l`
done


echo "[INFO] clear runtime directory" && rm -rf $RUNTIME_DIR
# ]
