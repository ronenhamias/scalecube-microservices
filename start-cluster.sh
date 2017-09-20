#!/bin/bash

DOCKER_REPO=scalecube/configuration
VERSION=0.0.5-SNAPSHOT

docker pull $DOCKER_REPO:scalecube-seed-0.0.5-SNAPSHOT
docker pull $DOCKER_REPO:scalecube-account-0.0.5-SNAPSHOT
docker pull $DOCKER_REPO:scalecube-configuration-0.0.5-SNAPSHOT
docker pull $DOCKER_REPO:scalecube-account-gateway-0.0.5-SNAPSHOT
docker pull $DOCKER_REPO:scalecube-configuration-gateway-0.0.5-SNAPSHOT
docker pull $DOCKER_REPO:scalecube-configuration-gateway-0.0.5-SNAPSHOT

dockip() {
  echo $(docker inspect --format '{{ .NetworkSettings.IPAddress }}' $(docker ps -aq | head -n1))
}

docker run -d -t redis

REDIS_IP=$(dockip) && echo "redis-ip: $REDIS_IP"

docker run -d -t DOCKER_REPO:scalecube-seed-$VERSION

SEED_IP=$(dockip) && echo "scalecube-seed-ip: $SEED_IP"

docker run -d -e "SC_SEED_ADDRESS=$SEED_IP:4801" -t $DOCKER_REPO:scalecube-seed-$VERSION

docker run -d -e "SC_SEED_ADDRESS=$SEED_IP:4801" -e "REDIS_ADDRESS=redis://$REDIS_IP:6379" -t $DOCKER_REPO:scalecube-account-$VERSION

docker run -d -e "SC_SEED_ADDRESS=$SEED_IP:4801" -e "REDIS_ADDRESS=redis://$REDIS_IP:6379" -t $DOCKER_REPO:scalecube-configuration-$VERSION

docker run -d -e "SC_SEED_ADDRESS=$SEED_IP:4801" -p 8081:8081 -t $DOCKER_REPO:scalecube-account-gateway-$VERSION

docker run -d -e "SC_SEED_ADDRESS=$SEED_IP:4801" -p 8082:8081 -t $DOCKER_REPO:scalecube-configuration-gateway-$VERSION

#docker ps --filter "status=exited" | grep '1 days ago' | awk '{print $1}' | xargs --no-run-if-empty docker rm
