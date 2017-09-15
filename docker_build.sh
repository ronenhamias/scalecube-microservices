#!/bin/bash
USERNAME=$1
BUILD_DIR=$2
ARTIFACT=$3

build(){
  cd $BUILD_DIR/$ARTIFACT
  MVN_VERSION=$(mvn -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive org.codehaus.mojo:exec-maven-plugin:1.3.1:exec)
  docker build -t $ARTIFACT:$ARTIFACT .
  docker images
  docker tag $ARTIFACT:$ARTIFACT $DOCKER_NAMESPACE/$DOCKER_REPO:$ARTIFACT-$MVN_VERSION
  docker push $DOCKER_NAMESPACE/$DOCKER_REPO:$ARTIFACT-$MVN_VERSION
  cd $BUILD_DIR
}

build