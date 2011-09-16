#!/bin/sh

MAVEN_OPTS="-d32" mvn -e exec:java -Dexec.mainClass="org.verdeterre.bluetooth.EchoClient" -Dexec.classpathScope=runtime
