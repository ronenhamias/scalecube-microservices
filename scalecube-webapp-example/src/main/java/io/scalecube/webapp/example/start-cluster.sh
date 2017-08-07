#!/bin/bash
VERSION="0.0.5-SNAPSHOT"

docker pull ronenna/scalecube:scalecube-seed-$VERSION
docker pull ronenna/scalecube:scalecube-account-$VERSION
docker pull ronenna/scalecube:scalecube-configuration-$VERSION
docker pull ronenna/scalecube:scalecube-account-gateway-$VERSION
docker pull ronenna/scalecube:scalecube-configuration-gateway-$VERSION
docker pull ronenna/scalecube:scalecube-configuration-gateway-$VERSION


function dockip() {
  echo $(docker inspect --format '{{ .NetworkSettings.IPAddress }}' $(docker ps -aq | head -n1))
}

docker run -d -t redis

REDIS_IP=$(dockip) && echo "redis ip: $REDIS_IP"

docker run -d -t ronenna/scalecube:scalecube-seed-0.0.5-SNAPSHOT

SEED_IP=$(dockip) && echo "seed ip: $SEED_IP"

docker run -d -e "SC_SEED_ADDRESS=$SEED_IP:4801" -t ronenna/scalecube:scalecube-seed-$VERSION

docker run -d -e "SC_SEED_ADDRESS=$SEED_IP:4801" -e "REDIS_ADDRESS=redis://$REDIS_IP:6379" -t ronenna/scalecube:scalecube-account-$VERSION

docker run -d -e "SC_SEED_ADDRESS=$SEED_IP:4801" -e "REDIS_ADDRESS=redis://$REDIS_IP:6379" -t ronenna/scalecube:scalecube-configuration-$VERSION

docker run -d -e "SC_SEED_ADDRESS=$SEED_IP:4801" -p 8081:8081 -t ronenna/scalecube:scalecube-account-gateway-$VERSION

docker run -d -e "SC_SEED_ADDRESS=$SEED_IP:4801" -p 8082:8081 -t ronenna/scalecube:scalecube-configuration-gateway-$VERSION
