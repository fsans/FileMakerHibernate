#!/bin/sh

mvn install:install-file \
-Dfile=./src/main/resources/fmjdbc.jar \
-DgroupId=com.filemaker.jdbc.driver \
-DartifactId=fmjdbc \
-Dversion=21.0.2 \
-Dpackaging=jar \
-DgeneratePom=true
