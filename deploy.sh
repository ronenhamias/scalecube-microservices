#!/bin/bash
docker login -u="$DOCKER_USERNAME" -p="$DOCKER_PASSWORD" 
bash docker_build.sh scalecube-seed
bash docker_build.sh scalecube-gateway-all 
bash docker_build.sh scalecube-configuration-gateway
bash docker_build.sh scalecube-configuration
bash docker_build.sh scalecube-account-gateway 
bash docker_build.sh scalecube-account
