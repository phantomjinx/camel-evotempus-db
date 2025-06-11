#!/bin/bash

clear

while getopts ":d:e:h:p:u:" opt ; do
  case "$opt" in
    d) EVOTEMPUS_DEST_DIR=${OPTARG} ;;
    e) EVOTEMPUS_DB_HOST=${OPTARG} ;;
    p) EVOTEMPUS_DB_PASSWD=${OPTARG} ;;
    u) EVOTEMPUS_DB_USER=${OPTARG} ;;
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
  EVOTEMPUS_DEST_DIR="${HOME}/evotempus"
fi

echo "Evotempus DB Host: ${EVOTEMPUS_DB_HOST}"
echo "Evotempus DB User: ${EVOTEMPUS_DB_USER}"
echo "Evotempus DB Password: ${EVOTEMPUS_DB_PASSWD}"
echo "Evotempus Destination Directory: ${EVOTEMPUS_DEST_DIR}"

rm -rf ${EVOTEMPUS_DEST_DIR}
mkdir -p ${EVOTEMPUS_DEST_DIR}

mvn clean spring-boot:run \
  -Devotempus.db.host=${EVOTEMPUS_DB_HOST} \
  -Devotempus.db.user=${EVOTEMPUS_DB_USER} \
  -Devotempus.db.passwd=${EVOTEMPUS_DB_PASSWD} \
  -Devotempus.dest.dir=${EVOTEMPUS_DEST_DIR}
