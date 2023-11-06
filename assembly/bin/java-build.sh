#!/usr/bin/env bash

set -x
sbinDir=$(cd "$(dirname "$0")"; pwd)
chmod +x $sbinDir/supersonic-common.sh
source $sbinDir/supersonic-common.sh

cd $baseDir

#1. build backend java modules
rm -fr ${buildDir}/*.tar.gz
rm -fr dist

set +x

mvn -f $baseDir/../ clean package -DskipTests -Dcheckstyle.skip=true

#2. move package to build
cp $baseDir/../launchers/semantic/target/*.tar.gz ${buildDir}/supersonic-semantic.tar.gz
cp $baseDir/../launchers/chat/target/*.tar.gz ${buildDir}/supersonic-chat.tar.gz
cp $baseDir/../launchers/standalone/target/*.tar.gz ${buildDir}/supersonic-standalone.tar.gz

#6. reset runtime
rm -fr $runtimeDir/*
mkdir -p ${runtimeDir}
moveAllToRuntime
