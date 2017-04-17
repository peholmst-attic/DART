#!/bin/sh

# Change these if needed to wherever you keep your configuration files
CONFIG=file://${PWD}/dart-resources-app-config.properties
LOGBACK=file://${PWD}/dart-resources-app-logback.xml

# This requires the 'java' executable to be on the path
java -Darchaius.configurationSource.additionalUrls=$CONFIG -Dlogback.configurationFile=$LOGBACK -jar ${project.artifactId}-${project.version}.jar
