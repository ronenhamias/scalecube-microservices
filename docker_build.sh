#!/bin/bash
ARTIFACT=$1

build(){
  echo "current dir:  $PWD, travis build dir: $TRAVIS_BUILD_DIR"
  cd $TRAVIS_BUILD_DIR/$ARTIFACT
  echo "current dir:  $PWD"
  MVN_VERSION=$(mvn -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive org.codehaus.mojo:exec-maven-plugin:1.3.1:exec)
  DOCKER_IMAGE=$DOCKER_NAMESPACE/$DOCKER_REPO:$ARTIFACT-$MVN_VERSION
  echo "docker image: " $DOCKER_IMAGE
  echo "target/lib dir: " 
  ls target/lib

  docker image build -t $DOCKER_IMAGE .
  docker images
  docker push $DOCKER_IMAGE
  cd $TRAVIS_BUILD_DIR
  echo "current dir:  $PWD"
}

build
