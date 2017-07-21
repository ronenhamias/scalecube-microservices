#!/bin/bash
docker login -u="$DOCKER_USERNAME" -p="$DOCKER_PASSWORD"; 
bash docker_build.sh $DOCKER_USERNAME $TRAVIS_BUILD_DIR scalecube-seed; 
bash docker_build.sh $DOCKER_USERNAME $TRAVIS_BUILD_DIR scalecube-gateway-all; 
bash docker_build.sh $DOCKER_USERNAME $TRAVIS_BUILD_DIR scalecube-configuration-gateway; 
bash docker_build.sh $DOCKER_USERNAME $TRAVIS_BUILD_DIR scalecube-configuration; 
bash docker_build.sh $DOCKER_USERNAME $TRAVIS_BUILD_DIR scalecube-account-gateway; 
bash docker_build.sh $DOCKER_USERNAME $TRAVIS_BUILD_DIR scalecube-account; 