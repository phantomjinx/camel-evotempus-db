#!/bin/bash

help() {
  echo "$0 [-e evotempus host]"
  exit 1
}

clear

while getopts ":d:e:h:p:u:" opt ; do
  case "$opt" in
    d) EVOTEMPUS_DEST_DIR=${OPTARG} ;;
    e) EVOTEMPUS_DB_HOST=${OPTARG} ;;
    u) EVOTEMPUS_DB_USER=${OPTARG} ;;
    p) EVOTEMPUS_DB_PASSWD=${OPTARG} ;;
    h) help
       ;;
    \\?) help
         ;;
  esac
done

shift `expr $OPTIND - 1`

if [ -z "${EVOTEMPUS_DB_HOST}" ]; then
  EVOTEMPUS_DB_HOST="localhost"
fi

if [ -z "${EVOTEMPUS_DEST_DIR}" ]; then
  EVOTEMPUS_DEST_DIR="/home/evotempus"
fi

echo "Evotempus DB Host: ${EVOTEMPUS_DB_HOST}"
echo "Evotempus DB User: ${EVOTEMPUS_DB_USER}"
echo "Evotempus Destination Directory: ${EVOTEMPUS_DEST_DIR}"

rm -rf ${EVOTEMPUS_DEST_DIR}
mkdir -p ${EVOTEMPUS_DEST_DIR}

JAR_FILE=$(find target -maxdepth 1 -name "*.jar")
if [ ! -f "${JAR_FILE}" ]; then
  echo "Using jar file: ${JAR_FILE}"
fi

java \
  -Devotempus.db.host=${EVOTEMPUS_DB_HOST} \
  -Devotempus.db.user=${EVOTEMPUS_DB_USER} \
  -Devotempus.db.passwd=${EVOTEMPUS_DB_PASSWD} \
  -Devotempus.dest.dir=${EVOTEMPUS_DEST_DIR} \
  -jar "${JAR_FILE}"
