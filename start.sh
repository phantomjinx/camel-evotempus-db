#!/bin/bash

rm -rf ${HOME}/evotempus/* && \
clear && \
mvn clean spring-boot:run
