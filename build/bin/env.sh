#!/bin/bash

if [ "$JAVA_HOME" != "" ]; then
  JAVA_HOME=$JAVA_HOME
fi

if [ "$JAVA_HOME" = "" ]; then
  echo "Error: JAVA_HOME is not set."
  exit 1
fi

JAVA=$JAVA_HOME/bin/java
BASE_HOME=$BASE_DIR
SERVER_NAME="spark-jobserver"
APP_START_MAIN_CLASS="io.github.melin.spark.jobserver.SparkJobServerMain"

export CLASSPATH=$BASE_DIR/conf:$BASE_DIR/lib/*

logPath=""
function check_log_dir() {
    mkdir -p "/data/logs"
    if [ -w "/data/logs" ] && [ -r "/data/logs" ]; then
        logPath=/data/logs
        echo "log path: /data/logs/jobserver"
    else
        logPath=${HOME}/output
        echo "log path: ${HOME}/output"
    fi
}

if [ "$1" = "start" ] || [ "$1" = "log" ] || [ "$1" = "start_k8s" ]; then
  check_log_dir
fi

#UEAP jvm args
# jvm 参数 https://www.cnblogs.com/hongdada/p/10277782.html
BASE_APP_ARGS="`(IFS=" "; echo "${@:3}")`"

if [ "$2" = "production" ]; then
  BASE_JVM_ARGS_0="-Xms4g -Xmx4g -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=512m -server"
else
  BASE_JVM_ARGS_0="-Xms1g -Xmx2g -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -server"
fi
BASE_JVM_ARGS_1="-XX:ReservedCodeCacheSize=256m -XX:+UseCodeCacheFlushing -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled"
BASE_JVM_ARGS_2="-XX:+UseParNewGC -XX:+UseFastAccessorMethods"
BASE_JVM_ARGS_3="-XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8 -Djava.net.preferIPv4Stack=true"
APP_JVM_ARGS="$BASE_JVM_ARGS_0 $BASE_JVM_ARGS_1 $BASE_JVM_ARGS_2 $BASE_JVM_ARGS_3 -cp $CLASSPATH"
JPDA_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=31012"
