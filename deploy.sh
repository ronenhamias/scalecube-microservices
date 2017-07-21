#!/bin/bash
if [ "$TRAVIS_BRANCH" = "master" -a "$TRAVIS_PULL_REQUEST" = "false" ]; docker login -u="$DOCKER_USERNAME" -p="$DOCKER_PASSWORD"; fi
if [ "$TRAVIS_BRANCH" = "master" -a "$TRAVIS_PULL_REQUEST" = "false" ]; bash docker_build.sh $DOCKER_USERNAME $TRAVIS_BUILD_DIR scalecube-seed; fi
if [ "$TRAVIS_BRANCH" = "master" -a "$TRAVIS_PULL_REQUEST" = "false" ]; bash docker_build.sh $DOCKER_USERNAME $TRAVIS_BUILD_DIR scalecube-gateway-all; fi
if [ "$TRAVIS_BRANCH" = "master" -a "$TRAVIS_PULL_REQUEST" = "false" ]; bash docker_build.sh $DOCKER_USERNAME $TRAVIS_BUILD_DIR scalecube-configuration-gateway; fi
if [ "$TRAVIS_BRANCH" = "master" -a "$TRAVIS_PULL_REQUEST" = "false" ]; bash docker_build.sh $DOCKER_USERNAME $TRAVIS_BUILD_DIR scalecube-configuration; fi
if [ "$TRAVIS_BRANCH" = "master" -a "$TRAVIS_PULL_REQUEST" = "false" ]; bash docker_build.sh $DOCKER_USERNAME $TRAVIS_BUILD_DIR scalecube-account-gateway; fi
if [ "$TRAVIS_BRANCH" = "master" -a "$TRAVIS_PULL_REQUEST" = "false" ]; bash docker_build.sh $DOCKER_USERNAME $TRAVIS_BUILD_DIR scalecube-account; fi