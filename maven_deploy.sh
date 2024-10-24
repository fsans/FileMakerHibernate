#!/bin/sh

mvn install:install-file \
-Dfile=./target/FileMakerDialect-21.0.2.jar \
-DgroupId=com.filemaker.hibernate.dialect \
-DartifactId=filemakerdialect \
-Dversion=21.0.2 \
-Dpackaging=jar \
-DgeneratePom=true