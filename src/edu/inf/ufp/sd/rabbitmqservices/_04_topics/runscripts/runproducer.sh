#!/usr/bin/env bash
#@REM ************************************************************************************
#@REM Description: run 
#@REM Author: Rui S. Moreira
#@REM Date: 10/04/2018
#@REM ************************************************************************************
#@REM Script usage: runproducer <routing_key> <message>
source ./setenv.sh producer

export ROUTING_KEY=$1
export MESSAGE=$2

echo ${ABSPATH2CLASSES}
cd ${ABSPATH2CLASSES}
#clear
#pwd
java -cp ${CLASSPATH} \
     ${JAVAPACKAGEROLEPATH}.${PRODUCER_CLASS_PREFIX} ${BROKER_HOST} ${BROKER_PORT} ${BROKER_EXCHANGE} ${ROUTING_KEY} ${MESSAGE}


cd ${ABSPATH2SRC}/${JAVASCRIPTSPATH}
#pwd
