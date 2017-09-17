#!/bin/bash
ARTIFACT=$1

build(){
  docker images
  cd $TRAVIS_BUILD_DIR/$ARTIFACT
  MVN_VERSION=$(mvn -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive org.codehaus.mojo:exec-maven-plugin:1.3.1:exec)
  DOCKER_IMAGE=$DOCKER_NAMESPACE/$DOCKER_REPO:$ARTIFACT-$MVN_VERSION
  echo "docker image: " $DOCKER_IMAGE
  docker image build -t $DOCKER_IMAGE .
  docker images
  docker push $DOCKER_IMAGE
  cd $TRAVIS_BUILD_DIR
}

build
