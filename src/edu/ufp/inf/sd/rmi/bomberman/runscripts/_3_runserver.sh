#!/usr/bin/env bash
#@REM ************************************************************************************
#@REM Description: run HelloWorldServer
#@REM Author: Rui S. Moreira
#@REM Date: 20/02/2014
#@REM ************************************************************************************
#@REM Script usage: runsetup <role> (where role should be: server / client)

# Source the environment variables
source ./setenv.sh server

# Verify that the necessary environment variables are set
required_vars=(
    ABSPATH2CLASSES CLASSPATH SERVER_CODEBASE SERVER_RMI_HOST
    SERVER_SECURITY_POLICY JAVAPACKAGEROLE SERVER_CLASS_PREFIX
    SERVER_CLASS_POSTFIX REGISTRY_HOST REGISTRY_PORT
    SERVICE_NAME_ON_REGISTRY ABSPATH2SRC JAVASCRIPTSPATH
)

for var in "${required_vars[@]}"; do
    if [ -z "${!var}" ]; then
        echo "Error: ${var} is not set. Please check your setenv.sh."
        exit 1
    fi
done

# Navigate to the classes directory
if [ -d "${ABSPATH2CLASSES}" ]; then
    cd "${ABSPATH2CLASSES}" || { echo "Failed to change directory to ${ABSPATH2CLASSES}"; exit 1; }
else
    echo "Directory ${ABSPATH2CLASSES} does not exist."
    exit 1
fi

# Run the server
java -cp "${CLASSPATH}" \
    -Djava.rmi.server.codebase="${SERVER_CODEBASE}" \
    -Djava.rmi.server.hostname="${SERVER_RMI_HOST}" \
    -Djava.security.policy="${SERVER_SECURITY_POLICY}" \
    "${JAVAPACKAGEROLE}.${SERVER_CLASS_PREFIX}${SERVER_CLASS_POSTFIX}" "${REGISTRY_HOST}" "${REGISTRY_PORT}" "${SERVICE_NAME_ON_REGISTRY}"

# Navigate to the JavaScript path
if [ -d "${ABSPATH2SRC}/${JAVASCRIPTSPATH}" ]; then
    cd "${ABSPATH2SRC}/${JAVASCRIPTSPATH}" || { echo "Failed to change directory to ${ABSPATH2SRC}/${JAVASCRIPTSPATH}"; exit 1; }
else
    echo "Directory ${ABSPATH2SRC}/${JAVASCRIPTSPATH} does not exist."
    exit 1
fi

# Optionally clear the screen
# clear

# Optionally print the current directory
# pwd
