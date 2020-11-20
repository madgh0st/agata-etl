#!/bin/bash
DATE="$(date +'%Y-%m-%d')"
TIME="$(date +'%H-%M-%S')"
DATETIME="${DATE}-${TIME}"

# Default values
step=raw-files
profile=prod
# latest | all | 2019-12-28
date_download="latest"

source functions.sh
handleInputParams "$@"

CURRENT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

if [ "${profile}" != "prod" ]; then
    suffix="_$profile"
fi

APP_NAME="AGATA${suffix}_$(basename "${CURRENT_DIR}")_${DATETIME}"

hdfs_log_path="/projects/AGATA${suffix}/data/logs/${DATE}/"
APP_LOGFILE_LOCAL="logs${suffix}/${APP_NAME}.log"
APP_LOGFILE_HDFS="${hdfs_log_path}/${APP_NAME}.log"

path_to_raw_files_config="${CURRENT_DIR}/ConfigAgata.xlsx"
application_properties="application-${profile}.properties"
path_to_application_properties="${CURRENT_DIR}/${application_properties}"
path_to_log4j_properties="${CURRENT_DIR}/log4j.properties"
path_to_white_list_do="${CURRENT_DIR}/white_list_do.xlsx"
path_to_pipelinelog="/projects/AGATA${suffix}/data/logs/pipline_status.txt"

mkdir -p "${CURRENT_DIR}/logs${suffix}"
mkdir -p "${CURRENT_DIR}/available_list_do"

hadoop fs -mkdir -p "/projects/AGATA/data/list_do/white_list_do/"
hadoop fs -mkdir -p "/projects/AGATA/data/list_do/available_list_do/"
hadoop fs -mkdir -p "${hdfs_log_path}"
hadoop fs -put -f "${path_to_white_list_do}" "/projects/AGATA/data/list_do/white_list_do/white_list_do.xlsx"

echo "Input param: ${profile} ${date_download} ${step} ${path_to_raw_files_config} ${path_to_application_properties}"

APP_JAR=$(find . -name "counterparty-etl-*-*.jar" | sort --version-sort | tail -1 | tr -d "\n")
APP_VERSION=$(echo "${APP_JAR}" |  awk -F"-" '{ print $3 }')
YARN_CLASSPATH=$(yarn classpath --glob | sed 's_:/usr/lib/hadoop/lib/slf4j\-log4j12\-\([^:]\)*__g')

SPARK_SUBMIT_PARAMS=(
  "--master=yarn"
  "--deploy-mode=cluster"
  "--files=${path_to_log4j_properties},${path_to_application_properties},${path_to_raw_files_config}"
  "--jars=hdfs:///projects/AGATA/java/lib/counterparty-etl-${APP_VERSION}-libs.jar"
  "--executor-cores=4"
  "--executor-memory=25g"
  "--num-executors=5"
  "--driver-memory=25g"
  "--driver-java-options=-Dlog4j.debug=false -Dlog4j.configuration=log4j.properties"
  "--conf=spark.driver.maxResultSize=3g"
  "--conf=spark.executor.extraJavaOptions=-Dlog4j.debug=false -Dlog4j.configuration=log4j.properties"
  "--conf=spark.memory.fraction=0.65"
  "--conf=spark.memory.storageFraction=0.05"
  "--conf=spark.yarn.maxAppAttempts=1"
  "--conf=spark.scheduler.listenerbus.eventqueue.capacity=100000"
  "--conf=spark.debug.maxToStringFields=100"
  "--conf=spark.network.timeout=10000000"
  "--conf=spark.speculation=false"
  "--conf=spark.sql.autoBroadcastJoinThreshold=$((50 * 1024 * 1024))" # 50 mb
  "--conf=spark.sql.broadcastTimeout=600"
  "--conf=spark.hadoop.yarn.application.classpath=${YARN_CLASSPATH}"
  "--conf=spark.hadoop.validateOutputSpecs=false"
)

# MAIN ETL
spark-submit --class "com.kpmg.agata.MainPipeline" "${SPARK_SUBMIT_PARAMS[@]}" --name "${APP_NAME}" \
"${APP_JAR}" "${date_download}" "${step}" "ConfigAgata.xlsx" "${application_properties}" 2>&1 | tee "${APP_LOGFILE_LOCAL}"

# LOGS ANALYSIS
last_application_id=$(yarn application -list -appStates ALL | grep "${APP_NAME}" | grep "SPARK" | awk '{print $1}' | sort -nr | head -n1)
yarn logs -applicationId "${last_application_id}" >> "${APP_LOGFILE_LOCAL}"
is_error=$(grep -i -e "error " -e "exception" "${APP_LOGFILE_LOCAL}" | grep -c -i -v -e "org.apache.spark.internal.Logging" -e "org.apache.spark.util.SignalUtils" -e "/hadoop/yarn/local/usercache/")

hadoop fs -put -f "${APP_LOGFILE_LOCAL}" "${APP_LOGFILE_HDFS}"
hdfs dfs -get "/projects/AGATA/data/list_do/available_list_do/*.csv" "${CURRENT_DIR}/available_list_do/available_list_do_${DATETIME}.csv"

spark-submit --class "com.kpmg.agata.LogParser" "${SPARK_SUBMIT_PARAMS[@]}" --name "${APP_NAME}_LOGS" \
"${APP_JAR}" "${application_properties}" "${APP_LOGFILE_HDFS}" "${hdfs_log_path}/${APP_NAME}.json"

if [ "${is_error}" -gt 0 ]; then
  echo "$(date +'%Y-%m-%d %H:%M:%S') FAILED ${APP_NAME}" | hadoop fs -appendToFile - "${path_to_pipelinelog}"
  echo "Script done with errors"
	exit 1
else
  echo "$(date +'%Y-%m-%d %H:%M:%S') OK ${APP_NAME}" | hadoop fs -appendToFile - "${path_to_pipelinelog}"
  echo "Script is succeeded"
fi

# MAPREDUCE
MAPRED_IN_DIR="hdfs:///projects/AGATA${suffix}/data/eventlog/counterparty"
MAPRED_OUT_DIR="hdfs:///projects/AGATA${suffix}/data/mapreduce/${APP_VERSION}"
MAPRED_OUT_TMP_DIR="${MAPRED_OUT_DIR}/tmp/$(date '+%Y-%m-%dT%H-%M-%S')"
MAPRED_OUT_RESULT_DIR="${MAPRED_OUT_DIR}/result/$(date '+%Y-%m-%dT%H-%M-%S')"
MAPRED_OUT_RESULT_LATEST_DIR="${MAPRED_OUT_DIR}/result_latest/$(date '+%Y-%m-%dT%H-%M-%S')"
LOG_LEVEL="INFO"

echo "Starting MapReduce job. Input dir: ${MAPRED_IN_DIR}. Output dir: ${MAPRED_OUT_DIR}"
hadoop jar "${APP_JAR}" "com.kpmg.agata.mapreduce.EventLogDriver" \
-D mapred.reduce.max.attempts=1 \
-D first.mapreduce.job.name="${APP_NAME}_MAPRED_FIRST" \
-D first.mapreduce.job.reduces=100 \
-D first.mapreduce.map.log.level="${LOG_LEVEL}" \
-D first.mapreduce.reduce.log.level="${LOG_LEVEL}" \
-D second.mapreduce.job.name="${APP_NAME}_MAPRED_SECOND" \
-D second.mapreduce.job.reduces=50 \
-D second.mapreduce.map.log.level="${LOG_LEVEL}" \
-D second.mapreduce.reduce.log.level="${LOG_LEVEL}" \
-D third.mapreduce.job.name="${APP_NAME}_MAPRED_THIRD" \
-D third.mapreduce.job.reduces=50 \
-D third.mapreduce.map.log.level="${LOG_LEVEL}" \
-D third.mapreduce.reduce.log.level="${LOG_LEVEL}" \
"${MAPRED_IN_DIR}" "${MAPRED_OUT_TMP_DIR}" "${MAPRED_OUT_RESULT_DIR}" "${MAPRED_OUT_RESULT_LATEST_DIR}"
