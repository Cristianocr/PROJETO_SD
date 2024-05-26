#!/usr/bin/env bash
#@REM ************************************************************************************
#@REM Description: run previously all batch files
#@REM Author: Rui S. Moreira
#@REM Date: 20/02/2019
#@REM pwd: /Users/rui/Documents/NetBeansProjects/SD/src/edu/ufp/inf/sd/rmi/_01_helloworld
#@REM http://docs.oracle.com/javase/tutorial/rmi/running.html
#@REM ************************************************************************************

#@REM ======================== Use Shell Parameters ========================
#@REM Script usage: setenv <role> (where role should be: server / client)
export SCRIPT_ROLE=$1

#@REM =====================================================================================================
#@REM ======================== CHANGE BELOW ACCORDING YOUR PROJECT and PC SETTINGS ========================
#@REM =====================================================================================================
# @REM ==== PC STUFF ====
export JDK=/Users/cristianojoaquimsoaresdarocha/Library/Java/JavaVirtualMachines/corretto-1.8.0_402/Contents/Home
#@REM These vars will be used to check the output folder (where classes are generated)
export NETBEANS=NetBeans
export INTELLIJ=IntelliJ
export CURRENT_IDE=${INTELLIJ}
#export CURRENT_IDE=${NETBEANS}
export USERNAME=cristianojoaquimsoaresdarocha

#@REM ==== JAVA NAMING STUFF ====
export JAVAPROJ_NAME=PROJETO_SD
export JAVAPROJ=/Users/cristianojoaquimsoaresdarocha/Desktop/Faculdade/Faculdade_23_24/2_semestre/SD/PROJETO_SD

export PACKAGE=rmi
export PACKAGE_PREFIX=edu/ufp/inf/sd
export PACKAGE_PREFIX_FOLDERS=edu/ufp/inf/sd
export SERVICE_NAME_ON_REGISTRY=BombermanServices
export SERVICE_NAME_ON_REGISTRY_sub=SubjectRIService
export CLIENT_CLASS_PREFIX=Bomberman
export SERVER_CLASS_PREFIX=Bomberman
export CLIENT_CLASS_POSTFIX=Client
export SERVER_CLASS_POSTFIX=Server
export SERVANT_IMPL_CLASS_POSTFIX=Impl

#@REM ==== NETWORK STUFF ====
export MYLOCALIP=localhost
export REGISTRY_HOST=${MYLOCALIP}
export REGISTRY_PORT=1099
export SERVER_RMI_HOST=${REGISTRY_HOST}
export SERVER_RMI_PORT=1098
export SERVER_CODEBASE_HOST=${SERVER_RMI_HOST}
export SERVER_CODEBASE_PORT=8000
export CLIENT_RMI_HOST=${REGISTRY_HOST}
export CLIENT_RMI_PORT=1097
export CLIENT_CODEBASE_HOST=${CLIENT_RMI_HOST}
export CLIENT_CODEBASE_PORT=8000

#@REM =====================================================================================================
#@REM ======================== DO NOT CHANGE AFTER THIS POINT =============================================
#@REM =====================================================================================================
export JAVAPACKAGE=${PACKAGE_PREFIX}.${PACKAGE}
export JAVAPACKAGEROLE=${PACKAGE_PREFIX}.${PACKAGE}.${SCRIPT_ROLE}
export JAVAPACKAGEPATH=${PACKAGE_PREFIX_FOLDERS}/${PACKAGE}/${SCRIPT_ROLE}
export JAVASCRIPTSPATH=${PACKAGE_PREFIX_FOLDERS}/${PACKAGE}/runscripts
export JAVASECURITYPATH=${PACKAGE_PREFIX_FOLDERS}/${PACKAGE}/securitypolicies
export SERVICE_NAME=${SERVICE_PREFIX}Service
export SERVICE_URL=rmi://${REGISTRY_HOST}:${REGISTRY_PORT}/${SERVICE_NAME}

export SERVANT_ACTIVATABLE_IMPL_CLASS=${JAVAPACKAGEROLE}.${SERVER_CLASS_PREFIX}${SERVANT_ACTIVATABLE_IMPL_CLASS_POSTFIX}
export SERVANT_PERSISTENT_STATE_FILENAME=${SERVICE_PREFIX}Persistent.State

export PATH=${PATH}:${JDK}/bin

if [ "${CURRENT_IDE}" == "${NETBEANS}" ]; then
    export JAVAPROJ_CLASSES=build/classes/
    export JAVAPROJ_DIST=dist
    export JAVAPROJ_SRC=src
    export JAVAPROJ_DIST_LIB=lib
elif [ "${CURRENT_IDE}" == "${INTELLIJ}" ]; then
    export JAVAPROJ_CLASSES=out/production/${JAVAPROJ_NAME}/
    export JAVAPROJ_DIST=out/artifacts/${JAVAPROJ_NAME}
    export JAVAPROJ_SRC=src
    export JAVAPROJ_DIST_LIB=lib
fi

export JAVAPROJ_CLASSES_FOLDER=${JAVAPROJ}/${JAVAPROJ_CLASSES}
export JAVAPROJ_DIST_FOLDER=${JAVAPROJ}/${JAVAPROJ_DIST}
export JAVAPROJ_DIST_LIB_FOLDER=${JAVAPROJ}/${JAVAPROJ_DIST_LIB}
export JAVAPROJ_JAR_FILE=${JAVAPROJ_NAME}.jar
export MYSQL_CON_JAR=mysql-connector-java-5.1.38-bin.jar

export CLASSPATH=.:${JAVAPROJ_CLASSES_FOLDER}
export CLASSPATH=${CLASSPATH}:${JAVAPROJ_DIST_LIB}/amqp-client-5.11.0.jar
export CLASSPATH=${CLASSPATH}:${JAVAPROJ_DIST_LIB}/jackson-annotations-2.14.2.jar
export CLASSPATH=${CLASSPATH}:${JAVAPROJ_DIST_LIB}/jackson-core-2.9.9-javadoc.jar
export CLASSPATH=${CLASSPATH}:${JAVAPROJ_DIST_LIB}/jackson-core-2.14.2.jar
export CLASSPATH=${CLASSPATH}:${JAVAPROJ_DIST_LIB}/jackson-databind-2.14.2.jar
export CLASSPATH=${CLASSPATH}:${JAVAPROJ_DIST_LIB}/java-jwt-4.4.0.jar
export CLASSPATH=${CLASSPATH}:${JAVAPROJ_DIST_LIB}/javax.activation-1.2.0.jar
export CLASSPATH=${CLASSPATH}:${JAVAPROJ_DIST_LIB}/javax.mail-1.6.2.jar
export CLASSPATH=${CLASSPATH}:${JAVAPROJ_DIST_LIB}/jjwt-api-0.11.2.jar
export CLASSPATH=${CLASSPATH}:${JAVAPROJ_DIST_LIB}/jjwt-impl-0.11.2.jar
export CLASSPATH=${CLASSPATH}:${JAVAPROJ_DIST_LIB}/jjwt-jackson-0.11.2.jar
export CLASSPATH=${CLASSPATH}:${JAVAPROJ_DIST_LIB}/slf4j-api-1.7.30.jar
export CLASSPATH=${CLASSPATH}:${JAVAPROJ_DIST_LIB}/slf4j-simple-1.7.30.jar

export ABSPATH2CLASSES=${JAVAPROJ}/${JAVAPROJ_CLASSES}
export ABSPATH2SRC=${JAVAPROJ}/${JAVAPROJ_SRC}
export ABSPATH2DIST=${JAVAPROJ}/${JAVAPROJ_DIST}

export SERVER_CODEBASE=http://${SERVER_CODEBASE_HOST}:${SERVER_CODEBASE_PORT}/${JAVAPROJ_JAR_FILE}
export CLIENT_CODEBASE=http://${CLIENT_CODEBASE_HOST}:${CLIENT_CODEBASE_PORT}/${JAVAPROJ_JAR_FILE}

export SERVER_SECURITY_POLICY=file:///${JAVAPROJ}/${JAVAPROJ_SRC}/${JAVASECURITYPATH}/serverAllPermition.policy
export CLIENT_SECURITY_POLICY=file:///${JAVAPROJ}/${JAVAPROJ_SRC}/${JAVASECURITYPATH}/clientAllPermition.policy
export SETUP_SECURITY_POLICY=file:///${JAVAPROJ}/${JAVAPROJ_SRC}/${JAVASECURITYPATH}/setup.policy
export RMID_SECURITY_POLICY=file:///${JAVAPROJ}/${JAVAPROJ_SRC}/${JAVASECURITYPATH}/rmid.policy
export GROUP_SECURITY_POLICY=file:///${JAVAPROJ}/${JAVAPROJ_SRC}/${JAVASECURITYPATH}/group.policy
