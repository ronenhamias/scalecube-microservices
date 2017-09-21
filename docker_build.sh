#!/bin/bash
ARTIFACT=$1

build() {
  cd $TRAVIS_BUILD_DIR/$ARTIFACT
  
  MVN_VERSION=$(mvn -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive org.codehaus.mojo:exec-maven-plugin:1.3.1:exec)
  
  DOCKER_IMAGE=$DOCKER_NAMESPACE/$DOCKER_REPO:$ARTIFACT-$MVN_VERSION
  echo "pushing docker image: " $DOCKER_IMAGE
  docker push $DOCKER_IMAGE
  cd $TRAVIS_BUILD_DIR
  
}

build
