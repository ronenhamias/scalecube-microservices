#!/bin/bash
USERNAME=$1
BUILD_DIR=$2
ARTIFACT=$3

build(){
  cd $BUILD_DIR/$ARTIFACT
  docker build -t $ARTIFACT:$ARTIFACT .
  docker images
  docker tag $ARTIFACT:$ARTIFACT $USERNAME/scalecube:$ARTIFACT
  docker push $USERNAME/scalecube:$ARTIFACT
  cd $BUILD_DIR
}

build